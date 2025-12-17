package com.orioncode.backoffice.databases.servers.controller;

import com.orioncode.backoffice.databases.servers.dto.CreateDatabaseServerRequestDTO;
import com.orioncode.backoffice.databases.servers.dto.DatabaseServerRequestDTO;
import com.orioncode.backoffice.databases.servers.dto.DatabaseServerResponse;
import com.orioncode.backoffice.databases.servers.dto.DatabaseServerSearchResponseDTO;
import com.orioncode.backoffice.databases.servers.service.DatabaseServerService;
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
@RequestMapping("/database-servers")
@RequiredArgsConstructor
@Tag(name = "Servidores de Base de Datos", description = "API para gestión de servidores de base de datos")
public class DatabaseServerController {

    private final DatabaseServerService databaseServerService;

    @GetMapping
    @Operation(
        summary = "Buscar servidores con paginación",
        description = "Busca servidores de base de datos con filtro dinámico y paginación. " +
                     "El parámetro 'filter' indica el campo de búsqueda (name, engine, version, host, environment, description). " +
                     "El parámetro 'search' contiene el valor a buscar. " +
                     "La búsqueda es parcial y case-insensitive. " +
                     "La paginación usa numeración desde 1 (página 1 es la primera)."
    )
    public ResponseEntity<PageResponse<DatabaseServerSearchResponseDTO>> search(
            @Parameter(description = "Campo sobre el que buscar: name, engine, version, host, environment, description")
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
        PageResponse<DatabaseServerSearchResponseDTO> result = databaseServerService.search(filter, search, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servidor por ID", description = "Retorna un servidor de base de datos específico por su ID")
    public ResponseEntity<DatabaseServerResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(databaseServerService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo servidor", description = "Crea un nuevo servidor de base de datos")
    public ResponseEntity<DatabaseServerResponse> create(@Valid @RequestBody CreateDatabaseServerRequestDTO requestDTO) {
        DatabaseServerResponse createdServer = databaseServerService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdServer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servidor", description = "Actualiza un servidor de base de datos existente")
    public ResponseEntity<DatabaseServerResponse> update(
            @PathVariable String id,
            @Valid @RequestBody DatabaseServerRequestDTO requestDTO) {
        return ResponseEntity.ok(databaseServerService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar servidor", description = "Elimina un servidor de base de datos por su ID")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        databaseServerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

