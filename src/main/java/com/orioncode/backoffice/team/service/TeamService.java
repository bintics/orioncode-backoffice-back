package com.orioncode.backoffice.team.service;

import com.orioncode.backoffice.common.exception.ResourceNotFoundException;
import com.orioncode.backoffice.team.dto.TeamRequestDTO;
import com.orioncode.backoffice.team.dto.TeamResponseDTO;
import com.orioncode.backoffice.team.entity.Team;
import com.orioncode.backoffice.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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
        if (teamRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Team with name '" + requestDTO.getName() + "' already exists");
        }

        Team team = new Team();
        team.setId(UUID.randomUUID().toString());
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

