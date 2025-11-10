package com.orioncode.backoffice.collaborators.service;

import com.orioncode.backoffice.common.exception.ResourceNotFoundException;
import com.orioncode.backoffice.collaborators.dto.CollaboratorRequestDTO;
import com.orioncode.backoffice.collaborators.dto.CollaboratorResponseDTO;
import com.orioncode.backoffice.collaborators.entity.Collaborator;
import com.orioncode.backoffice.collaborators.repository.CollaboratorRepository;
import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import com.orioncode.backoffice.position.entity.Position;
import com.orioncode.backoffice.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final PositionRepository positionRepository;

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
}
