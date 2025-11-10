package com.orioncode.backoffice.position.service;

import com.orioncode.backoffice.common.exception.ResourceNotFoundException;
import com.orioncode.backoffice.position.dto.PositionRequestDTO;
import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import com.orioncode.backoffice.position.entity.Position;
import com.orioncode.backoffice.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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
