package com.orioncode.backoffice.collaborators.controller;

import com.orioncode.backoffice.collaborators.dto.CollaboratorRequestDTO;
import com.orioncode.backoffice.collaborators.dto.CollaboratorResponseDTO;
import com.orioncode.backoffice.collaborators.dto.CollaboratorSearchResponseDTO;
import com.orioncode.backoffice.collaborators.service.CollaboratorService;
import com.orioncode.backoffice.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collaborators")
@RequiredArgsConstructor
@Tag(name = "Colaboradores", description = "API para gestión de colaboradores")
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @GetMapping
    @Operation(
        summary = "Buscar colaboradores con paginación",
        description = "Busca colaboradores con filtro dinámico y paginación. " +
                     "El parámetro 'filter' indica el campo de búsqueda (firstName, lastName, team, position, id). " +
                     "El parámetro 'search' contiene el valor a buscar. " +
                     "La búsqueda es parcial y case-insensitive. " +
                     "La paginación usa numeración desde 1 (página 1 es la primera)."
    )
    public ResponseEntity<PageResponse<CollaboratorSearchResponseDTO>> searchCollaborators(
            @Parameter(description = "Campo sobre el que buscar: firstName, lastName, team, position, id")
            @RequestParam(required = false) String filter,
            @Parameter(description = "Valor a buscar (búsqueda parcial, case-insensitive)")
            @RequestParam(required = false) String search,
            @Parameter(description = "Número de página (inicia en 1)")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Campo para ordenar")
            @RequestParam(defaultValue = "firstName") String sortBy,
            @Parameter(description = "Dirección de ordenamiento (asc/desc)")
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

        // Convertir de 1-indexed a 0-indexed para Spring Data
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        PageResponse<CollaboratorSearchResponseDTO> result = collaboratorService.searchCollaborators(filter, search, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener colaborador por ID", description = "Retorna un colaborador específico por su ID")
    public ResponseEntity<CollaboratorResponseDTO> getCollaboratorById(@PathVariable String id) {
        return ResponseEntity.ok(collaboratorService.getCollaboratorById(id));
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
