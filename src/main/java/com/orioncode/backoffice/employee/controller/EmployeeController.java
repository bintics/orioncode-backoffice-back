package com.orioncode.backoffice.employee.controller;

import com.orioncode.backoffice.employee.dto.EmployeeRequestDTO;
import com.orioncode.backoffice.employee.dto.EmployeeResponseDTO;
import com.orioncode.backoffice.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Colaboradores", description = "API para gestión de colaboradores")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @Operation(summary = "Obtener todos los colaboradores", description = "Retorna una lista de todos los colaboradores")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener colaborador por ID", description = "Retorna un colaborador específico por su ID")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/team/{team}")
    @Operation(summary = "Obtener colaboradores por equipo", description = "Retorna todos los colaboradores de un equipo específico")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByTeam(@PathVariable String team) {
        return ResponseEntity.ok(employeeService.getEmployeesByTeam(team));
    }

    @GetMapping("/position/{positionId}")
    @Operation(summary = "Obtener colaboradores por puesto", description = "Retorna todos los colaboradores con un puesto específico")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByPosition(@PathVariable Long positionId) {
        return ResponseEntity.ok(employeeService.getEmployeesByPosition(positionId));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo colaborador", description = "Crea un nuevo colaborador")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO requestDTO) {
        EmployeeResponseDTO createdEmployee = employeeService.createEmployee(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar colaborador", description = "Actualiza un colaborador existente")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO requestDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar colaborador", description = "Elimina un colaborador por su ID")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
