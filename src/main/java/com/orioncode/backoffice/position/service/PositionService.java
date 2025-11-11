package com.orioncode.backoffice.position.service;

import com.orioncode.backoffice.common.exception.ResourceNotFoundException;
import com.orioncode.backoffice.common.dto.PageResponse;
import com.orioncode.backoffice.common.dto.PaginationMetadata;
import com.orioncode.backoffice.common.dto.SearchMetadata;
import com.orioncode.backoffice.position.dto.PositionRequestDTO;
import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import com.orioncode.backoffice.position.entity.Position;
import com.orioncode.backoffice.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    @Transactional(readOnly = true)
    public List<PositionResponseDTO> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PositionResponseDTO getPositionById(String id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + id));
        return convertToDTO(position);
    }

    @Transactional
    public PositionResponseDTO createPosition(PositionRequestDTO requestDTO) {
        String positionId = requestDTO.getId();
        if (positionId == null || positionId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del puesto es requerido");
        }
        
        if (positionRepository.existsById(positionId)) {
            throw new IllegalArgumentException("Ya existe un puesto con el ID: " + positionId);
        }
        
        if (positionRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe un puesto con el nombre: " + requestDTO.getName());
        }

        Position position = new Position();
        position.setId(positionId);
        position.setName(requestDTO.getName());
        position.setDescription(requestDTO.getDescription());

        Position savedPosition = positionRepository.save(position);
        return convertToDTO(savedPosition);
    }

    @Transactional
    public PositionResponseDTO updatePosition(String id, PositionRequestDTO requestDTO) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + id));

        if (!position.getName().equals(requestDTO.getName()) &&
            positionRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe un puesto con el nombre: " + requestDTO.getName());
        }

        position.setName(requestDTO.getName());
        position.setDescription(requestDTO.getDescription());

        Position updatedPosition = positionRepository.save(position);
        return convertToDTO(updatedPosition);
    }

    @Transactional
    public void deletePosition(String id) {
        if (!positionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Puesto no encontrado con ID: " + id);
        }
        positionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<PositionResponseDTO> searchPositions(
            String filter, String search, Pageable pageable) {

        Specification<Position> spec = Specification.where(null);

        // Si se proporciona filter y search, aplicar filtro dinámico
        if (filter != null && !filter.trim().isEmpty() && search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";

            spec = spec.and((root, query, cb) -> {
                switch (filter.toLowerCase()) {
                    case "name":
                        return cb.like(cb.lower(root.get("name")), searchPattern);
                    case "description":
                        return cb.like(cb.lower(root.get("description")), searchPattern);
                    case "id":
                        return cb.like(cb.lower(root.get("id")), searchPattern);
                    default:
                        // Si el filter no es reconocido, buscar en todos los campos
                        return cb.or(
                            cb.like(cb.lower(root.get("name")), searchPattern),
                            cb.like(cb.lower(root.get("description")), searchPattern),
                            cb.like(cb.lower(root.get("id")), searchPattern)
                        );
                }
            });
        } else if (search != null && !search.trim().isEmpty()) {
            // Si solo se proporciona search sin filter, buscar en todos los campos
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), searchPattern),
                cb.like(cb.lower(root.get("description")), searchPattern),
                cb.like(cb.lower(root.get("id")), searchPattern)
            ));
        }

        Page<Position> page = positionRepository.findAll(spec, pageable);

        List<PositionResponseDTO> data = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        PaginationMetadata pagination = new PaginationMetadata(
                page.getNumber() + 1, // Convertir de 0-indexed a 1-indexed
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        // Crear metadata con los filtros disponibles
        SearchMetadata metadata = new SearchMetadata(
                List.of("name", "description", "id")
        );

        return new PageResponse<>(data, pagination, metadata);
    }

    private PositionResponseDTO convertToDTO(Position position) {
        return new PositionResponseDTO(
                position.getId(),
                position.getName(),
                position.getDescription(),
                position.getCreatedAt(),
                position.getUpdatedAt()
        );
    }
}
