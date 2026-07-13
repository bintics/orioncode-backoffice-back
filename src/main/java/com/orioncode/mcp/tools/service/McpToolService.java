package com.orioncode.mcp.tools.service;

import com.orioncode.backoffice.collaborators.dto.CollaboratorSearchResponseDTO;
import com.orioncode.backoffice.collaborators.service.CollaboratorService;
import com.orioncode.backoffice.position.dto.PositionResponseDTO;
import com.orioncode.backoffice.position.service.PositionService;
import com.orioncode.backoffice.team.dto.TeamResponseDTO;
import com.orioncode.backoffice.team.service.TeamService;
import com.orioncode.frontoffice.projects.dto.ProjectSearchResponseDTO;
import com.orioncode.frontoffice.projects.service.ProjectService;
import com.orioncode.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class McpToolService {

    private final CollaboratorService collaboratorService;
    private final ProjectService projectService;
    private final TeamService teamService;
    private final PositionService positionService;

    public Map<String, Object> searchCollaborators(Map<String, Object> arguments) {
        String filter = getString(arguments, "filter", null);
        String search = getString(arguments, "search", null);

        int page = Math.max(1, getInt(arguments, "page", 1));
        int size = Math.max(1, Math.min(100, getInt(arguments, "size", 20)));
        String sortBy = getString(arguments, "sortBy", "firstName");
        String sortDir = getString(arguments, "sortDir", "asc");

        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        PageResponse<CollaboratorSearchResponseDTO> response =
                collaboratorService.searchCollaborators(filter, search, pageable);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("data", response.getData());
        payload.put("pagination", response.getPagination());
        payload.put("metadata", response.getMetadata());
        return payload;
    }

    public Map<String, Object> searchProjects(Map<String, Object> arguments) {
        String filter = getString(arguments, "filter", null);
        String search = getString(arguments, "search", null);

        int page = Math.max(1, getInt(arguments, "page", 1));
        int size = Math.max(1, Math.min(100, getInt(arguments, "size", 20)));
        String sortBy = getString(arguments, "sortBy", "name");
        String sortDir = getString(arguments, "sortDir", "asc");

        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        PageResponse<ProjectSearchResponseDTO> response =
                projectService.searchProjects(filter, search, pageable);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("data", response.getData());
        payload.put("pagination", response.getPagination());
        payload.put("metadata", response.getMetadata());
        return payload;
    }

    public Map<String, Object> searchTeams(Map<String, Object> arguments) {
        String filter = getString(arguments, "filter", null);
        String search = getString(arguments, "search", null);

        int page = Math.max(1, getInt(arguments, "page", 1));
        int size = Math.max(1, Math.min(100, getInt(arguments, "size", 20)));
        String sortBy = getString(arguments, "sortBy", "name");
        String sortDir = getString(arguments, "sortDir", "asc");

        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        PageResponse<TeamResponseDTO> response =
                teamService.searchTeams(filter, search, pageable);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("data", response.getData());
        payload.put("pagination", response.getPagination());
        payload.put("metadata", response.getMetadata());
        return payload;
    }

    public Map<String, Object> searchPositions(Map<String, Object> arguments) {
        String filter = getString(arguments, "filter", null);
        String search = getString(arguments, "search", null);

        int page = Math.max(1, getInt(arguments, "page", 1));
        int size = Math.max(1, Math.min(100, getInt(arguments, "size", 20)));
        String sortBy = getString(arguments, "sortBy", "name");
        String sortDir = getString(arguments, "sortDir", "asc");

        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        PageResponse<PositionResponseDTO> response =
                positionService.searchPositions(filter, search, pageable);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("data", response.getData());
        payload.put("pagination", response.getPagination());
        payload.put("metadata", response.getMetadata());
        return payload;
    }

    private String getString(Map<String, Object> source, String key, String fallback) {
        if (source == null || !source.containsKey(key) || source.get(key) == null) {
            return fallback;
        }
        return String.valueOf(source.get(key));
    }

    private int getInt(Map<String, Object> source, String key, int fallback) {
        if (source == null || !source.containsKey(key) || source.get(key) == null) {
            return fallback;
        }

        Object rawValue = source.get(key);
        if (rawValue instanceof Number number) {
            return number.intValue();
        }

        try {
            return Integer.parseInt(String.valueOf(rawValue));
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }
}

