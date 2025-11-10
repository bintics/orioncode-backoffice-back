package com.orioncode.backoffice.collaborators;

import com.orioncode.backoffice.collaborators.dto.EmployeeRequestDTO;
import com.orioncode.backoffice.collaborators.dto.EmployeeResponseDTO;
import com.orioncode.backoffice.collaborators.repository.EmployeeRepository;
import com.orioncode.backoffice.collaborators.service.EmployeeService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionService positionService;

    @Autowired
    private PositionRepository positionRepository;

    private PositionResponseDTO position;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        positionRepository.deleteAll();
        
        position = positionService.createPosition(new PositionRequestDTO("Desarrollador", "Desarrollador de software"));
    }

    @Test
    void testCreateEmployee() {
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO(
                "EMP001",
                "Juan",
                "Pérez",
                position.getId(),
                "Team A",
                Arrays.asList("Java", "Spring")
        );
        
        EmployeeResponseDTO responseDTO = employeeService.createEmployee(requestDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertEquals("Juan", responseDTO.getFirstName());
        assertEquals("Pérez", responseDTO.getLastName());
        assertEquals("Team A", responseDTO.getTeam());
        assertEquals(2, responseDTO.getTags().size());
    }

    @Test
    void testGetAllEmployees() {
        employeeService.createEmployee(new EmployeeRequestDTO("EMP001", "Juan", "Pérez", position.getId(), "Team A", null));
        employeeService.createEmployee(new EmployeeRequestDTO("EMP002", "María", "García", position.getId(), "Team B", null));

        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();

        assertEquals(2, employees.size());
    }

    @Test
    void testGetEmployeesByTeam() {
        employeeService.createEmployee(new EmployeeRequestDTO("EMP001", "Juan", "Pérez", position.getId(), "Team A", null));
        employeeService.createEmployee(new EmployeeRequestDTO("EMP002", "María", "García", position.getId(), "Team A", null));
        employeeService.createEmployee(new EmployeeRequestDTO("EMP003", "Pedro", "López", position.getId(), "Team B", null));

        List<EmployeeResponseDTO> teamAEmployees = employeeService.getEmployeesByTeam("Team A");

        assertEquals(2, teamAEmployees.size());
    }

    @Test
    void testUpdateEmployee() {
        EmployeeResponseDTO created = employeeService.createEmployee(
                new EmployeeRequestDTO("EMP001", "Juan", "Pérez", position.getId(), "Team A", null)
        );
        
        EmployeeRequestDTO updateDTO = new EmployeeRequestDTO(
                "EMP001-UPD",
                "Juan Carlos",
                "Pérez González",
                position.getId(),
                "Team B",
                Arrays.asList("Python")
        );
        
        EmployeeResponseDTO updated = employeeService.updateEmployee(created.getId(), updateDTO);

        assertEquals("Juan Carlos", updated.getFirstName());
        assertEquals("Team B", updated.getTeam());
    }

    @Test
    void testDeleteEmployee() {
        EmployeeResponseDTO created = employeeService.createEmployee(
                new EmployeeRequestDTO("EMP001", "Juan", "Pérez", position.getId(), "Team A", null)
        );
        
        employeeService.deleteEmployee(created.getId());
        
        assertEquals(0, employeeService.getAllEmployees().size());
    }
}
