package com.orioncode.backoffice.position.controller;

import com.orioncode.backoffice.position.dto.PositionRequestDTO;
import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import com.orioncode.backoffice.position.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
@Tag(name = "Puestos", description = "API para gestión de puestos de trabajo")
public class PositionController {

    private final PositionService positionService;

    @GetMapping
    @Operation(summary = "Obtener todos los puestos", description = "Retorna una lista de todos los puestos disponibles")
    public ResponseEntity<List<PositionResponseDTO>> getAllPositions() {
        return ResponseEntity.ok(positionService.getAllPositions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener puesto por ID", description = "Retorna un puesto específico por su ID")
    public ResponseEntity<PositionResponseDTO> getPositionById(@PathVariable String id) {
        return ResponseEntity.ok(positionService.getPositionById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo puesto", description = "Crea un nuevo puesto de trabajo")
    public ResponseEntity<PositionResponseDTO> createPosition(@Valid @RequestBody PositionRequestDTO requestDTO) {
        PositionResponseDTO createdPosition = positionService.createPosition(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPosition);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar puesto", description = "Actualiza un puesto existente")
    public ResponseEntity<PositionResponseDTO> updatePosition(
            @PathVariable String id,
            @Valid @RequestBody PositionRequestDTO requestDTO) {
        return ResponseEntity.ok(positionService.updatePosition(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar puesto", description = "Elimina un puesto por su ID")
    public ResponseEntity<Void> deletePosition(@PathVariable String id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
