package com.orioncode.backoffice.team.controller;

import com.orioncode.shared.dto.PageResponse;
import com.orioncode.backoffice.team.dto.TeamRequestDTO;
import com.orioncode.backoffice.team.dto.TeamResponseDTO;
import com.orioncode.backoffice.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "API para gestión de equipos")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    @Operation(
        summary = "Buscar equipos con paginación",
        description = "Busca equipos con filtro dinámico y paginación. " +
                     "El parámetro 'filter' indica el campo de búsqueda (name, description, id). " +
                     "El parámetro 'search' contiene el valor a buscar. " +
                     "La búsqueda es parcial y case-insensitive. " +
                     "La paginación usa numeración desde 1 (página 1 es la primera)."
    )
    public ResponseEntity<PageResponse<TeamResponseDTO>> searchTeams(
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
        PageResponse<TeamResponseDTO> result = teamService.searchTeams(filter, search, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping(headers = "X-dropdown")
    @Operation(summary = "Obtener equipos para listas desplegables")
    public ResponseEntity<List<TeamResponseDTO>> getTeamsForDropdown(
            @Parameter(description = "Valor a buscar (búsqueda parcial, case-insensitive)")
            @RequestParam(required = false) String search
    ) {
        List<TeamResponseDTO> teams = teamService.getTeamsForDropdown(search);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener equipo por ID")
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable String id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Obtener equipo por nombre")
    public ResponseEntity<TeamResponseDTO> getTeamByName(@PathVariable String name) {
        return ResponseEntity.ok(teamService.getTeamByName(name));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo equipo")
    public ResponseEntity<TeamResponseDTO> createTeam(@RequestBody TeamRequestDTO requestDTO) {
        TeamResponseDTO createdTeam = teamService.createTeam(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un equipo existente")
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @PathVariable String id,
            @RequestBody TeamRequestDTO requestDTO) {
        return ResponseEntity.ok(teamService.updateTeam(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un equipo")
    public ResponseEntity<Void> deleteTeam(@PathVariable String id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}

