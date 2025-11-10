package com.orioncode.backoffice.collaborators.controller;

import com.orioncode.backoffice.collaborators.dto.CollaboratorRequestDTO;
import com.orioncode.backoffice.collaborators.dto.CollaboratorResponseDTO;
import com.orioncode.backoffice.collaborators.service.CollaboratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collaborators")
@RequiredArgsConstructor
@Tag(name = "Colaboradores", description = "API para gestión de colaboradores")
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @GetMapping
    @Operation(summary = "Obtener todos los colaboradores", description = "Retorna una lista de todos los colaboradores")
    public ResponseEntity<List<CollaboratorResponseDTO>> getAllCollaborators() {
        return ResponseEntity.ok(collaboratorService.getAllCollaborators());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener colaborador por ID", description = "Retorna un colaborador específico por su ID")
    public ResponseEntity<CollaboratorResponseDTO> getCollaboratorById(@PathVariable String id) {
        return ResponseEntity.ok(collaboratorService.getCollaboratorById(id));
    }

    @GetMapping("/team/{team}")
    @Operation(summary = "Obtener colaboradores por equipo", description = "Retorna todos los colaboradores de un equipo específico")
    public ResponseEntity<List<CollaboratorResponseDTO>> getCollaboratorsByTeam(@PathVariable String team) {
        return ResponseEntity.ok(collaboratorService.getCollaboratorsByTeam(team));
    }

    @GetMapping("/position/{positionId}")
    @Operation(summary = "Obtener colaboradores por puesto", description = "Retorna todos los colaboradores con un puesto específico")
    public ResponseEntity<List<CollaboratorResponseDTO>> getCollaboratorsByPosition(@PathVariable String positionId) {
        return ResponseEntity.ok(collaboratorService.getCollaboratorsByPosition(positionId));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo colaborador", description = "Crea un nuevo colaborador")
    public ResponseEntity<CollaboratorResponseDTO> createCollaborator(@Valid @RequestBody CollaboratorRequestDTO requestDTO) {
        CollaboratorResponseDTO createdCollaborator = collaboratorService.createCollaborator(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCollaborator);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar colaborador", description = "Actualiza un colaborador existente")
    public ResponseEntity<CollaboratorResponseDTO> updateCollaborator(
            @PathVariable String id,
            @Valid @RequestBody CollaboratorRequestDTO requestDTO) {
        return ResponseEntity.ok(collaboratorService.updateCollaborator(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar colaborador", description = "Elimina un colaborador por su ID")
    public ResponseEntity<Void> deleteCollaborator(@PathVariable String id) {
        collaboratorService.deleteCollaborator(id);
        return ResponseEntity.noContent().build();
    }
}
