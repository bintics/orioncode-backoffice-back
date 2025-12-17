package com.orioncode.backoffice.databases.schemas.controller;

import com.orioncode.backoffice.databases.schemas.dto.CreateDatabaseSchemaRequestDTO;
import com.orioncode.backoffice.databases.schemas.dto.DatabaseSchemaRequestDTO;
import com.orioncode.backoffice.databases.schemas.dto.DatabaseSchemaResponse;
import com.orioncode.backoffice.databases.schemas.dto.DatabaseSchemaSearchResponseDTO;
import com.orioncode.backoffice.databases.schemas.service.DatabaseSchemaService;
import com.orioncode.shared.dto.PageResponse;
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
@RequestMapping("/database-schemas")
@RequiredArgsConstructor
@Tag(name = "Schemas de Base de Datos", description = "API para gestión de schemas de base de datos")
public class DatabaseSchemaController {

    private final DatabaseSchemaService databaseSchemaService;

    @GetMapping
    @Operation(
        summary = "Buscar schemas con paginación",
        description = "Busca schemas de base de datos con filtro dinámico y paginación. " +
                     "El parámetro 'filter' indica el campo de búsqueda (name, serverId, description, owner). " +
                     "El parámetro 'search' contiene el valor a buscar. " +
                     "La búsqueda es parcial y case-insensitive. " +
                     "La paginación usa numeración desde 1 (página 1 es la primera)."
    )
    public ResponseEntity<PageResponse<DatabaseSchemaSearchResponseDTO>> search(
            @Parameter(description = "Campo sobre el que buscar: name, serverId, description, owner")
            @RequestParam(required = false) String filter,
            @Parameter(description = "Valor a buscar (búsqueda parcial, case-insensitive)")
            @RequestParam(required = false) String search,
            @Parameter(description = "Número de página (inicia en 1)")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Campo para ordenar")
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Dirección de ordenamiento (asc/desc)")
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

        // Convertir de 1-indexed a 0-indexed para Spring Data
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        PageResponse<DatabaseSchemaSearchResponseDTO> result = databaseSchemaService.search(filter, search, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener schema por ID", description = "Retorna un schema de base de datos específico por su ID")
    public ResponseEntity<DatabaseSchemaResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(databaseSchemaService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo schema", description = "Crea un nuevo schema de base de datos")
    public ResponseEntity<DatabaseSchemaResponse> create(@Valid @RequestBody CreateDatabaseSchemaRequestDTO requestDTO) {
        DatabaseSchemaResponse createdSchema = databaseSchemaService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchema);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar schema", description = "Actualiza un schema de base de datos existente")
    public ResponseEntity<DatabaseSchemaResponse> update(
            @PathVariable String id,
            @Valid @RequestBody DatabaseSchemaRequestDTO requestDTO) {
        return ResponseEntity.ok(databaseSchemaService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar schema", description = "Elimina un schema de base de datos por su ID")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        databaseSchemaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

