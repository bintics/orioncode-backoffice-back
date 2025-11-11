package com.orioncode.backoffice.position.controller;

import com.orioncode.backoffice.common.dto.PageResponse;
import com.orioncode.backoffice.position.dto.PositionRequestDTO;
import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import com.orioncode.backoffice.position.service.PositionService;
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
@RequestMapping("/positions")
@RequiredArgsConstructor
@Tag(name = "Puestos", description = "API para gestión de puestos de trabajo")
public class PositionController {

    private final PositionService positionService;

    @GetMapping
    @Operation(
        summary = "Buscar puestos con paginación",
        description = "Busca puestos con filtro dinámico y paginación. " +
                     "El parámetro 'filter' indica el campo de búsqueda (name, description, id). " +
                     "El parámetro 'search' contiene el valor a buscar. " +
                     "La búsqueda es parcial y case-insensitive. " +
                     "La paginación usa numeración desde 1 (página 1 es la primera)."
    )
    public ResponseEntity<PageResponse<PositionResponseDTO>> searchPositions(
            @Parameter(description = "Campo sobre el que buscar: name, description, id")
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
        PageResponse<PositionResponseDTO> result = positionService.searchPositions(filter, search, pageable);

        return ResponseEntity.ok(result);
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
