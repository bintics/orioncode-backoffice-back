package com.orioncode.backoffice.position;

import com.orioncode.backoffice.position.dto.PositionRequestDTO;
import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import com.orioncode.backoffice.position.entity.Position;
import com.orioncode.backoffice.position.repository.PositionRepository;
import com.orioncode.backoffice.position.service.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PositionServiceTest {

    @Autowired
    private PositionService positionService;

    @Autowired
    private PositionRepository positionRepository;

    @BeforeEach
    void setUp() {
        positionRepository.deleteAll();
    }

    @Test
    void testCreatePosition() {
        PositionRequestDTO requestDTO = new PositionRequestDTO("Desarrollador", "Desarrollador de software");
        PositionResponseDTO responseDTO = positionService.createPosition(requestDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertEquals("Desarrollador", responseDTO.getName());
        assertEquals("Desarrollador de software", responseDTO.getDescription());
    }

    @Test
    void testGetAllPositions() {
        positionService.createPosition(new PositionRequestDTO("Desarrollador", "Desarrollador de software"));
        positionService.createPosition(new PositionRequestDTO("Gerente", "Gerente de proyecto"));

        List<PositionResponseDTO> positions = positionService.getAllPositions();

        assertEquals(2, positions.size());
    }

    @Test
    void testUpdatePosition() {
        PositionResponseDTO created = positionService.createPosition(new PositionRequestDTO("Desarrollador", "Dev"));
        
        PositionRequestDTO updateDTO = new PositionRequestDTO("Desarrollador Senior", "Senior Dev");
        PositionResponseDTO updated = positionService.updatePosition(created.getId(), updateDTO);

        assertEquals("Desarrollador Senior", updated.getName());
        assertEquals("Senior Dev", updated.getDescription());
    }

    @Test
    void testDeletePosition() {
        PositionResponseDTO created = positionService.createPosition(new PositionRequestDTO("Desarrollador", "Dev"));
        
        positionService.deletePosition(created.getId());
        
        assertEquals(0, positionService.getAllPositions().size());
    }
}
