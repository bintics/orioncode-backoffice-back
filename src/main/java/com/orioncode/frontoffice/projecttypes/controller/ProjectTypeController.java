package com.orioncode.frontoffice.projecttypes.controller;

import com.orioncode.frontoffice.projecttypes.dto.ProjectTypeRequestDTO;
import com.orioncode.frontoffice.projecttypes.dto.ProjectTypeResponseDTO;
import com.orioncode.frontoffice.projecttypes.dto.ProjectTypeSearchResponseDTO;
import com.orioncode.frontoffice.projecttypes.service.ProjectTypeService;
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

import java.util.List;

@RestController
@RequestMapping("/project-types")
@RequiredArgsConstructor
@Tag(name = "Tipos de Proyecto", description = "API para gestión de tipos de proyecto")
public class ProjectTypeController {

    private final ProjectTypeService projectTypeService;

    @GetMapping
    @Operation(
        summary = "Buscar tipos de proyecto con paginación",
        description = "Busca tipos de proyecto con filtro dinámico y paginación. " +
                     "El parámetro 'filter' indica el campo de búsqueda (name, description). " +
                     "El parámetro 'search' contiene el valor a buscar. " +
                     "La búsqueda es parcial y case-insensitive. " +
                     "La paginación usa numeración desde 1 (página 1 es la primera)."
    )
    public ResponseEntity<PageResponse<ProjectTypeSearchResponseDTO>> searchProjectTypes(
            @Parameter(description = "Campo sobre el que buscar: name, description")
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

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        PageResponse<ProjectTypeSearchResponseDTO> result = projectTypeService.searchProjectTypes(filter, search, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos los tipos de proyecto", description = "Retorna todos los tipos de proyecto sin paginación")
    public ResponseEntity<List<ProjectTypeResponseDTO>> getAllProjectTypes() {
        return ResponseEntity.ok(projectTypeService.getAllProjectTypes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo de proyecto por ID", description = "Retorna un tipo de proyecto específico por su ID")
    public ResponseEntity<ProjectTypeResponseDTO> getProjectTypeById(@PathVariable String id) {
        return ResponseEntity.ok(projectTypeService.getProjectTypeById(id));
    }

    @PostMapping("/batch")
    @Operation(
        summary = "Obtener múltiples tipos de proyecto por IDs",
        description = "Retorna los detalles de múltiples tipos de proyecto dado un array de IDs. " +
                     "Los IDs que no existan serán ignorados silenciosamente. " +
                     "Usa POST en lugar de GET para evitar limitaciones de longitud de URL y " +
                     "permitir la consulta de cientos o miles de tipos de proyecto en una sola petición."
    )
    public ResponseEntity<List<ProjectTypeResponseDTO>> getProjectTypesByIds(
            @Parameter(description = "Lista de IDs de tipos de proyecto a consultar", required = true)
            @RequestBody @Valid List<@jakarta.validation.constraints.NotBlank String> ids) {
        List<ProjectTypeResponseDTO> projectTypes = projectTypeService.getProjectTypesByIds(ids);
        return ResponseEntity.ok(projectTypes);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Obtener tipo de proyecto por nombre", description = "Retorna un tipo de proyecto específico por su nombre")
    public ResponseEntity<ProjectTypeResponseDTO> getProjectTypeByName(@PathVariable String name) {
        return ResponseEntity.ok(projectTypeService.getProjectTypeByName(name));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo tipo de proyecto", description = "Crea un nuevo tipo de proyecto. El ID debe ser proporcionado por el cliente.")
    public ResponseEntity<ProjectTypeResponseDTO> createProjectType(@Valid @RequestBody ProjectTypeRequestDTO requestDTO) {
        ProjectTypeResponseDTO createdProjectType = projectTypeService.createProjectType(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectType);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de proyecto", description = "Actualiza un tipo de proyecto existente")
    public ResponseEntity<ProjectTypeResponseDTO> updateProjectType(
            @PathVariable String id,
            @Valid @RequestBody ProjectTypeRequestDTO requestDTO) {
        return ResponseEntity.ok(projectTypeService.updateProjectType(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de proyecto", description = "Elimina un tipo de proyecto por su ID")
    public ResponseEntity<Void> deleteProjectType(@PathVariable String id) {
        projectTypeService.deleteProjectType(id);
        return ResponseEntity.noContent().build();
    }
}

