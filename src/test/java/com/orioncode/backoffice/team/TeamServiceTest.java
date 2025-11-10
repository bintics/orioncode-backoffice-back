package com.orioncode.backoffice.team;

import com.orioncode.backoffice.common.exception.ResourceNotFoundException;
import com.orioncode.backoffice.team.dto.TeamRequestDTO;
import com.orioncode.backoffice.team.dto.TeamResponseDTO;
import com.orioncode.backoffice.team.entity.Team;
import com.orioncode.backoffice.team.repository.TeamRepository;
import com.orioncode.backoffice.team.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();
    }

    @Test
    void testCreateTeam() {
        TeamRequestDTO requestDTO = new TeamRequestDTO("Development", "Development team");

        TeamResponseDTO responseDTO = teamService.createTeam(requestDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertEquals("Development", responseDTO.getName());
        assertEquals("Development team", responseDTO.getDescription());
        assertNotNull(responseDTO.getCreatedAt());
        assertNotNull(responseDTO.getUpdatedAt());
    }

    @Test
    void testCreateTeamWithDuplicateName() {
        TeamRequestDTO requestDTO1 = new TeamRequestDTO("Development", "Development team");
        teamService.createTeam(requestDTO1);

        TeamRequestDTO requestDTO2 = new TeamRequestDTO("Development", "Another description");

        assertThrows(IllegalArgumentException.class, () -> {
            teamService.createTeam(requestDTO2);
        });
    }

    @Test
    void testGetAllTeams() {
        teamService.createTeam(new TeamRequestDTO("Development", "Dev team"));
        teamService.createTeam(new TeamRequestDTO("QA", "QA team"));
        teamService.createTeam(new TeamRequestDTO("Design", "Design team"));

        List<TeamResponseDTO> teams = teamService.getAllTeams();

        assertEquals(3, teams.size());
    }

    @Test
    void testGetTeamById() {
        TeamResponseDTO created = teamService.createTeam(new TeamRequestDTO("Development", "Dev team"));

        TeamResponseDTO found = teamService.getTeamById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("Development", found.getName());
    }

    @Test
    void testGetTeamByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            teamService.getTeamById("999L");
        });
    }

    @Test
    void testGetTeamByName() {
        teamService.createTeam(new TeamRequestDTO("Development", "Dev team"));

        TeamResponseDTO found = teamService.getTeamByName("Development");

        assertNotNull(found);
        assertEquals("Development", found.getName());
    }

    @Test
    void testGetTeamByNameNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            teamService.getTeamByName("NonExistent");
        });
    }

    @Test
    void testUpdateTeam() {
        TeamResponseDTO created = teamService.createTeam(new TeamRequestDTO("Development", "Dev team"));

        TeamRequestDTO updateDTO = new TeamRequestDTO("Backend Development", "Backend team");
        TeamResponseDTO updated = teamService.updateTeam(created.getId(), updateDTO);

        assertNotNull(updated);
        assertEquals(created.getId(), updated.getId());
        assertEquals("Backend Development", updated.getName());
        assertEquals("Backend team", updated.getDescription());
    }

    @Test
    void testUpdateTeamNotFound() {
        TeamRequestDTO updateDTO = new TeamRequestDTO("Development", "Dev team");

        assertThrows(ResourceNotFoundException.class, () -> {
            teamService.updateTeam("999L", updateDTO);
        });
    }

    @Test
    void testUpdateTeamWithExistingName() {
        teamService.createTeam(new TeamRequestDTO("Development", "Dev team"));
        TeamResponseDTO team2 = teamService.createTeam(new TeamRequestDTO("QA", "QA team"));

        TeamRequestDTO updateDTO = new TeamRequestDTO("Development", "Updated QA team");

        assertThrows(IllegalArgumentException.class, () -> {
            teamService.updateTeam(team2.getId(), updateDTO);
        });
    }

    @Test
    void testDeleteTeam() {
        TeamResponseDTO created = teamService.createTeam(new TeamRequestDTO("Development", "Dev team"));

        teamService.deleteTeam(created.getId());

        assertThrows(ResourceNotFoundException.class, () -> {
            teamService.getTeamById(created.getId());
        });
    }

    @Test
    void testDeleteTeamNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            teamService.deleteTeam("999L");
        });
    }
}

