package com.orioncode.backoffice.collaborators;

import com.orioncode.backoffice.collaborators.dto.CollaboratorRequestDTO;
import com.orioncode.backoffice.collaborators.dto.CollaboratorResponseDTO;
import com.orioncode.backoffice.collaborators.repository.CollaboratorRepository;
import com.orioncode.backoffice.collaborators.service.CollaboratorService;
import com.orioncode.backoffice.position.dto.PositionRequestDTO;
import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import com.orioncode.backoffice.position.repository.PositionRepository;
import com.orioncode.backoffice.position.service.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CollaboratorServiceTest {

    @Autowired
    private CollaboratorService collaboratorService;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private PositionService positionService;

    @Autowired
    private PositionRepository positionRepository;

    private PositionResponseDTO position;

    @BeforeEach
    void setUp() {
        collaboratorRepository.deleteAll();
        positionRepository.deleteAll();
        
        position = positionService.createPosition(new PositionRequestDTO(UUID.randomUUID().toString(), "Desarrollador", "Desarrollador de software"));
    }

    @Test
    void testCreateCollaborator() {
        CollaboratorRequestDTO requestDTO = new CollaboratorRequestDTO(
                "EMP001",
                "Juan",
                "Pérez",
                position.getId(),
                "Team A",
                Arrays.asList("Java", "Spring")
        );
        
        CollaboratorResponseDTO responseDTO = collaboratorService.createCollaborator(requestDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertEquals("Juan", responseDTO.getFirstName());
        assertEquals("Pérez", responseDTO.getLastName());
        assertEquals("Team A", responseDTO.getTeamId());
        assertEquals(2, responseDTO.getTags().size());
    }

    @Test
    void testGetAllCollaborators() {
        collaboratorService.createCollaborator(new CollaboratorRequestDTO("EMP001", "Juan", "Pérez", position.getId(), "Team A", null));
        collaboratorService.createCollaborator(new CollaboratorRequestDTO("EMP002", "María", "García", position.getId(), "Team B", null));

        List<CollaboratorResponseDTO> collaborators = collaboratorService.getAllCollaborators();

        assertEquals(2, collaborators.size());
    }

    @Test
    void testUpdateCollaborator() {
        CollaboratorResponseDTO created = collaboratorService.createCollaborator(
                new CollaboratorRequestDTO("EMP001", "Juan", "Pérez", position.getId(), "Team A", null)
        );
        
        CollaboratorRequestDTO updateDTO = new CollaboratorRequestDTO(
                "EMP001-UPD",
                "Juan Carlos",
                "Pérez González",
                position.getId(),
                "Team B",
                Arrays.asList("Python")
        );
        
        CollaboratorResponseDTO updated = collaboratorService.updateCollaborator(created.getId(), updateDTO);

        assertEquals("Juan Carlos", updated.getFirstName());
        assertEquals("Team B", updated.getTeamId());
    }

    @Test
    void testDeleteCollaborator() {
        CollaboratorResponseDTO created = collaboratorService.createCollaborator(
                new CollaboratorRequestDTO("EMP001", "Juan", "Pérez", position.getId(), "Team A", null)
        );
        
        collaboratorService.deleteCollaborator(created.getId());
        
        assertEquals(0, collaboratorService.getAllCollaborators().size());
    }
}
