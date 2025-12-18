package com.orioncode.backoffice.databases.objects.controller;

import com.orioncode.backoffice.databases.objects.dto.CreateDatabaseObjectRequestDTO;
import com.orioncode.backoffice.databases.objects.dto.DatabaseObjectRequestDTO;
import com.orioncode.backoffice.databases.objects.dto.DatabaseObjectResponse;
import com.orioncode.backoffice.databases.objects.dto.DatabaseObjectSearchResponseDTO;
import com.orioncode.backoffice.databases.objects.service.DatabaseObjectService;
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
@RequestMapping("/database-objects")
@RequiredArgsConstructor
@Tag(name = "Objetos de Base de Datos", description = "API para gestión de objetos de base de datos (tablas, vistas, procedimientos, funciones)")
public class DatabaseObjectController {

    private final DatabaseObjectService databaseObjectService;

    @GetMapping
    @Operation(
        summary = "Buscar objetos de base de datos con paginación",
        description = "Busca objetos de base de datos con filtro dinámico y paginación. " +
                     "El parámetro 'filter' indica el campo de búsqueda (name, type, schemaId, owner, description). " +
                     "El parámetro 'search' contiene el valor a buscar. " +
                     "La búsqueda es parcial y case-insensitive. " +
                     "La paginación usa numeración desde 1 (página 1 es la primera)."
    )
    public ResponseEntity<PageResponse<DatabaseObjectSearchResponseDTO>> search(
            @Parameter(description = "Campo sobre el que buscar: name, type, schemaId, owner, description")
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
        PageResponse<DatabaseObjectSearchResponseDTO> result = databaseObjectService.search(filter, search, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener objeto de base de datos por ID", description = "Retorna un objeto de base de datos específico por su ID")
    public ResponseEntity<DatabaseObjectResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(databaseObjectService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo objeto de base de datos", description = "Crea un nuevo objeto de base de datos")
    public ResponseEntity<DatabaseObjectResponse> create(@Valid @RequestBody CreateDatabaseObjectRequestDTO requestDTO) {
        DatabaseObjectResponse createdObject = databaseObjectService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdObject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar objeto de base de datos", description = "Actualiza un objeto de base de datos existente")
    public ResponseEntity<DatabaseObjectResponse> update(
            @PathVariable String id,
            @Valid @RequestBody DatabaseObjectRequestDTO requestDTO) {
        return ResponseEntity.ok(databaseObjectService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar objeto de base de datos", description = "Elimina un objeto de base de datos por su ID")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        databaseObjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

