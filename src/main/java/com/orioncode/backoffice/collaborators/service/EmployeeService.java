package com.orioncode.backoffice.collaborators.service;

import com.orioncode.backoffice.common.exception.ResourceNotFoundException;
import com.orioncode.backoffice.collaborators.dto.EmployeeRequestDTO;
import com.orioncode.backoffice.collaborators.dto.EmployeeResponseDTO;
import com.orioncode.backoffice.collaborators.entity.Employee;
import com.orioncode.backoffice.collaborators.repository.EmployeeRepository;
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
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador no encontrado con ID: " + id));
        return convertToDTO(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByTeam(String team) {
        return employeeRepository.findByTeam(team).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByPosition(String positionId) {
        return employeeRepository.findByPositionId(positionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        String employeeId = requestDTO.getId();
        if (employeeId == null || employeeId.trim().isEmpty()) {
            employeeId = UUID.randomUUID().toString();
        } else if (employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Ya existe un colaborador con el ID: " + employeeId);
        }

        Position position = positionRepository.findById(requestDTO.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + requestDTO.getPositionId()));

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setPosition(position);
        employee.setTeam(requestDTO.getTeam());
        employee.setTags(requestDTO.getTags() != null ? requestDTO.getTags() : new ArrayList<>());

        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(String id, EmployeeRequestDTO requestDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador no encontrado con ID: " + id));

        Position position = positionRepository.findById(requestDTO.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con ID: " + requestDTO.getPositionId()));

        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setPosition(position);
        employee.setTeam(requestDTO.getTeam());
        employee.setTags(requestDTO.getTags() != null ? new ArrayList<>(requestDTO.getTags()) : new ArrayList<>());

        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDTO(updatedEmployee);
    }

    @Transactional
    public void deleteEmployee(String id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Colaborador no encontrado con ID: " + id);
        }
        employeeRepository.deleteById(id);
    }

    private EmployeeResponseDTO convertToDTO(Employee employee) {
        PositionResponseDTO positionDTO = new PositionResponseDTO(
                employee.getPosition().getId(),
                employee.getPosition().getName(),
                employee.getPosition().getDescription(),
                employee.getPosition().getCreatedAt(),
                employee.getPosition().getUpdatedAt()
        );

        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                positionDTO,
                employee.getTeam(),
                employee.getTags(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}
