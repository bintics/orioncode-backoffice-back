package com.orioncode.mcp.tools.tools;

import com.orioncode.mcp.tools.service.McpToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProjectMcpTool {

    private final McpToolService mcpToolService;

    @Tool(name = "search_projects", description = "Busca proyectos en OrionCode con filtros y paginacion")
    public Map<String, Object> searchProjects(
            @ToolParam(description = "Campo a filtrar: name, description, status, typeId, ownerId o id") String filter,
            @ToolParam(description = "Texto a buscar") String search,
            @ToolParam(description = "Pagina inicial en 1") Integer page,
            @ToolParam(description = "Tamano de pagina") Integer size,
            @ToolParam(description = "Campo de orden") String sortBy,
            @ToolParam(description = "Direccion de orden: asc o desc") String sortDir
    ) {
        Map<String, Object> arguments = new LinkedHashMap<>();
        arguments.put("filter", filter);
        arguments.put("search", search);
        arguments.put("page", page);
        arguments.put("size", size);
        arguments.put("sortBy", sortBy);
        arguments.put("sortDir", sortDir);
        return mcpToolService.searchProjects(arguments);
    }
}

