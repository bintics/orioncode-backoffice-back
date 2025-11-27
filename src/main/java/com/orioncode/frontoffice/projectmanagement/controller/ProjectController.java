package com.orioncode.frontoffice.projectmanagement.controller;

import com.orioncode.frontoffice.projectmanagement.dto.ProjectRequestDTO;
import com.orioncode.frontoffice.projectmanagement.dto.ProjectResponse;
import com.orioncode.frontoffice.projectmanagement.dto.ProjectSearchResponseDTO;
import com.orioncode.frontoffice.projectmanagement.service.ProjectService;
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
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Proyectos", description = "API para gestión de proyectos")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @Operation(
        summary = "Buscar proyectos con paginación",
        description = "Busca proyectos con filtro dinámico y paginación. " +
                     "El parámetro 'filter' indica el campo de búsqueda (name, description, status, ownerId). " +
                     "El parámetro 'search' contiene el valor a buscar. " +
                     "La búsqueda es parcial y case-insensitive. " +
                     "La paginación usa numeración desde 1 (página 1 es la primera)."
    )
    public ResponseEntity<PageResponse<ProjectSearchResponseDTO>> searchProjects(
            @Parameter(description = "Campo sobre el que buscar: name, description, status, ownerId")
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
        PageResponse<ProjectSearchResponseDTO> result = projectService.searchProjects(filter, search, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener proyecto por ID", description = "Retorna un proyecto específico por su ID")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo proyecto", description = "Crea un nuevo proyecto")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequestDTO requestDTO) {
        ProjectResponse createdProject = projectService.createProject(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proyecto", description = "Actualiza un proyecto existente")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDTO requestDTO) {
        return ResponseEntity.ok(projectService.updateProject(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proyecto", description = "Elimina un proyecto por su ID")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}

