package com.orioncode.backoffice.collaborators.service;

import com.orioncode.backoffice.common.exception.ResourceNotFoundException;
import com.orioncode.backoffice.common.dto.PageResponse;
import com.orioncode.backoffice.common.dto.PaginationMetadata;
import com.orioncode.backoffice.common.dto.SearchMetadata;
import com.orioncode.backoffice.collaborators.dto.CollaboratorRequestDTO;
import com.orioncode.backoffice.collaborators.dto.CollaboratorResponseDTO;
import com.orioncode.backoffice.collaborators.dto.CollaboratorSearchResponseDTO;
import com.orioncode.backoffice.collaborators.dto.SimpleTeamDTO;
import com.orioncode.backoffice.collaborators.entity.Collaborator;
import com.orioncode.backoffice.collaborators.repository.CollaboratorRepository;
import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import com.orioncode.backoffice.position.entity.Position;
import com.orioncode.backoffice.position.repository.PositionRepository;
import com.orioncode.backoffice.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final PositionRepository positionRepository;
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public List<CollaboratorResponseDTO> getAllCollaborators() {
        return collaboratorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CollaboratorResponseDTO getCollaboratorById(String id) {
        Collaborator collaborator = collaboratorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador no encontrado con ID: " + id));
        return convertToDTO(collaborator);
    }

    @Transactional(readOnly = true)
    public List<CollaboratorResponseDTO> getCollaboratorsByTeam(String team) {
        return collaboratorRepository.findByTeam(team).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CollaboratorResponseDTO> getCollaboratorsByPosition(String positionId) {
        return collaboratorRepository.findByPositionId(positionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CollaboratorResponseDTO createCollaborator(CollaboratorRequestDTO requestDTO) {
        String collaboratorId = requestDTO.getId();
        if (collaboratorId == null || collaboratorId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del colaborador es requerido");
        }
        
        if (collaboratorRepository.existsById(collaboratorId)) {
            throw new IllegalArgumentException("Ya existe un colaborador con el ID: " + collaboratorId);
        }

        Position position = positionRepository.findById(requestDTO.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + requestDTO.getPositionId()));

        Collaborator collaborator = new Collaborator();
        collaborator.setId(collaboratorId);
        collaborator.setFirstName(requestDTO.getFirstName());
        collaborator.setLastName(requestDTO.getLastName());
        collaborator.setPosition(position);
        collaborator.setTeam(requestDTO.getTeam());
        collaborator.setTags(requestDTO.getTags() != null ? requestDTO.getTags() : new ArrayList<>());

        Collaborator savedCollaborator = collaboratorRepository.save(collaborator);
        return convertToDTO(savedCollaborator);
    }

    @Transactional
    public CollaboratorResponseDTO updateCollaborator(String id, CollaboratorRequestDTO requestDTO) {
        Collaborator collaborator = collaboratorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador no encontrado con ID: " + id));

        Position position = positionRepository.findById(requestDTO.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + requestDTO.getPositionId()));

        collaborator.setFirstName(requestDTO.getFirstName());
        collaborator.setLastName(requestDTO.getLastName());
        collaborator.setPosition(position);
        collaborator.setTeam(requestDTO.getTeam());
        collaborator.setTags(requestDTO.getTags() != null ? new ArrayList<>(requestDTO.getTags()) : new ArrayList<>());

        Collaborator updatedCollaborator = collaboratorRepository.save(collaborator);
        return convertToDTO(updatedCollaborator);
    }

    @Transactional
    public void deleteCollaborator(String id) {
        if (!collaboratorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Colaborador no encontrado con ID: " + id);
        }
        collaboratorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<CollaboratorSearchResponseDTO> searchCollaborators(
            String filter, String search, Pageable pageable) {

        Specification<Collaborator> spec = Specification.where(null);

        // Si se proporciona filter y search, aplicar filtro dinámico
        if (filter != null && !filter.trim().isEmpty() && search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";

            spec = spec.and((root, query, cb) -> {
                switch (filter.toLowerCase()) {
                    case "firstname":
                        return cb.like(cb.lower(root.get("firstName")), searchPattern);
                    case "lastname":
                        return cb.like(cb.lower(root.get("lastName")), searchPattern);
                    case "team":
                        return cb.like(cb.lower(root.get("team")), searchPattern);
                    case "position":
                        return cb.like(cb.lower(root.get("position").get("name")), searchPattern);
                    case "id":
                        return cb.like(cb.lower(root.get("id")), searchPattern);
                    default:
                        // Si el filter no es reconocido, buscar en todos los campos
                        return cb.or(
                            cb.like(cb.lower(root.get("firstName")), searchPattern),
                            cb.like(cb.lower(root.get("lastName")), searchPattern),
                            cb.like(cb.lower(root.get("team")), searchPattern),
                            cb.like(cb.lower(root.get("position").get("name")), searchPattern),
                            cb.like(cb.lower(root.get("id")), searchPattern)
                        );
                }
            });
        } else if (search != null && !search.trim().isEmpty()) {
            // Si solo se proporciona search sin filter, buscar en todos los campos
            String searchPattern = "%" + search.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("firstName")), searchPattern),
                cb.like(cb.lower(root.get("lastName")), searchPattern),
                cb.like(cb.lower(root.get("team")), searchPattern),
                cb.like(cb.lower(root.get("position").get("name")), searchPattern),
                cb.like(cb.lower(root.get("id")), searchPattern)
            ));
        }

        Page<Collaborator> page = collaboratorRepository.findAll(spec, pageable);

        List<CollaboratorSearchResponseDTO> data = page.getContent().stream()
                .map(this::convertToSearchDTO)
                .collect(Collectors.toList());

        PaginationMetadata pagination = new PaginationMetadata(
                page.getNumber() + 1, // Convertir de 0-indexed a 1-indexed
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        // Crear metadata con los filtros disponibles
        SearchMetadata metadata = new SearchMetadata(
                List.of("firstName", "lastName", "team", "position", "id")
        );

        return new PageResponse<>(data, pagination, metadata);
    }

    private CollaboratorResponseDTO convertToDTO(Collaborator collaborator) {
        PositionResponseDTO positionDTO = new PositionResponseDTO(
                collaborator.getPosition().getId(),
                collaborator.getPosition().getName(),
                collaborator.getPosition().getDescription(),
                collaborator.getPosition().getCreatedAt(),
                collaborator.getPosition().getUpdatedAt()
        );

        return new CollaboratorResponseDTO(
                collaborator.getId(),
                collaborator.getFirstName(),
                collaborator.getLastName(),
                positionDTO,
                collaborator.getTeam(),
                collaborator.getTags(),
                collaborator.getCreatedAt(),
                collaborator.getUpdatedAt()
        );
    }

    private CollaboratorSearchResponseDTO convertToSearchDTO(Collaborator collaborator) {
        // Obtener información del equipo si existe
        SimpleTeamDTO teamDTO = null;
        if (collaborator.getTeam() != null && !collaborator.getTeam().trim().isEmpty()) {
            teamDTO = teamRepository.findByName(collaborator.getTeam())
                    .map(team -> new SimpleTeamDTO(team.getId(), team.getName()))
                    .orElse(new SimpleTeamDTO(null, collaborator.getTeam()));
        }

        return new CollaboratorSearchResponseDTO(
                collaborator.getId(),
                collaborator.getFirstName(),
                collaborator.getLastName(),
                collaborator.getPosition().getName(),
                teamDTO,
                collaborator.getTags(),
                collaborator.getCreatedAt(),
                collaborator.getUpdatedAt()
        );
    }
}
