package com.orioncode.backoffice.team.service;

import com.orioncode.backoffice.common.exception.ResourceNotFoundException;
import com.orioncode.backoffice.common.dto.PageResponse;
import com.orioncode.backoffice.common.dto.PaginationMetadata;
import com.orioncode.backoffice.common.dto.SearchMetadata;
import com.orioncode.backoffice.team.dto.TeamRequestDTO;
import com.orioncode.backoffice.team.dto.TeamResponseDTO;
import com.orioncode.backoffice.team.entity.Team;
import com.orioncode.backoffice.team.repository.TeamRepository;
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
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public List<TeamResponseDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamResponseDTO getTeamById(String id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
        return convertToResponseDTO(team);
    }

    @Transactional(readOnly = true)
    public TeamResponseDTO getTeamByName(String name) {
        Team team = teamRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with name: " + name));
        return convertToResponseDTO(team);
    }

    @Transactional
    public TeamResponseDTO createTeam(TeamRequestDTO requestDTO) {
        String teamId = requestDTO.getId();
        if (teamId == null || teamId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del equipo es requerido");
        }
        
        if (teamRepository.existsById(teamId)) {
            throw new IllegalArgumentException("Ya existe un equipo con el ID: " + teamId);
        }
        
        if (teamRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Team with name '" + requestDTO.getName() + "' already exists");
        }

        Team team = new Team();
        team.setId(teamId);
        team.setName(requestDTO.getName());
        team.setDescription(requestDTO.getDescription());

        Team savedTeam = teamRepository.save(team);
        return convertToResponseDTO(savedTeam);
    }

    @Transactional
    public TeamResponseDTO updateTeam(String id, TeamRequestDTO requestDTO) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        // Check if the new name already exists for another team
        if (!team.getName().equals(requestDTO.getName()) &&
            teamRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Team with name '" + requestDTO.getName() + "' already exists");
        }

        team.setName(requestDTO.getName());
        team.setDescription(requestDTO.getDescription());

        Team updatedTeam = teamRepository.save(team);
        return convertToResponseDTO(updatedTeam);
    }

    @Transactional
    public void deleteTeam(String id) {
        if (!teamRepository.existsById(id)) {
            throw new ResourceNotFoundException("Team not found with id: " + id);
        }
        teamRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageResponse<TeamResponseDTO> searchTeams(
            String filter, String search, Pageable pageable) {

        Specification<Team> spec = Specification.where(null);

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

        Page<Team> page = teamRepository.findAll(spec, pageable);

        List<TeamResponseDTO> data = page.getContent().stream()
                .map(this::convertToResponseDTO)
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

    private TeamResponseDTO convertToResponseDTO(Team team) {
        TeamResponseDTO dto = new TeamResponseDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setDescription(team.getDescription());
        dto.setCreatedAt(team.getCreatedAt());
        dto.setUpdatedAt(team.getUpdatedAt());
        return dto;
    }
}
