package com.orioncode.backoffice.team.controller;

import com.orioncode.backoffice.team.dto.TeamRequestDTO;
import com.orioncode.backoffice.team.dto.TeamResponseDTO;
import com.orioncode.backoffice.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @Operation(summary = "Obtener todos los equipos")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
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

