package com.orioncode.mcp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orioncode.mcp.service.McpToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "orioncode.mcp.enabled", havingValue = "true", matchIfMissing = true)
public class McpController {

    private static final String TOOL_NAME = "search_collaborators";

    private final McpToolService mcpToolService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "${orioncode.mcp.path:/mcp}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> handleRpc(@RequestBody Map<String, Object> request) {
        Object id = request.get("id");
        String method = asText(request.get("method"));
        Map<String, Object> params = asMap(request.get("params"));

        if (method == null || method.isBlank()) {
            return ResponseEntity.ok(errorResponse(id, -32600, "Invalid Request", "Missing method"));
        }

        try {
            return switch (method) {
                case "initialize" -> ResponseEntity.ok(successResponse(id, initializeResult()));
                case "tools/list" -> ResponseEntity.ok(successResponse(id, toolsListResult()));
                case "tools/call" -> ResponseEntity.ok(handleToolCall(id, params));
                default -> ResponseEntity.ok(errorResponse(id, -32601, "Method not found", method));
            };
        } catch (Exception ex) {
            return ResponseEntity.ok(errorResponse(id, -32603, "Internal error", ex.getMessage()));
        }
    }

    private Map<String, Object> handleToolCall(Object id, Map<String, Object> params) throws JsonProcessingException {
        String toolName = asText(params.get("name"));
        if (!TOOL_NAME.equals(toolName)) {
            return errorResponse(id, -32602, "Invalid params", "Unknown tool: " + toolName);
        }

        Map<String, Object> arguments = asMap(params.get("arguments"));
        Map<String, Object> toolPayload = mcpToolService.searchCollaborators(arguments);

        String textPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(toolPayload);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("content", List.of(Map.of("type", "text", "text", textPayload)));
        result.put("structuredContent", toolPayload);
        return successResponse(id, result);
    }

    private Map<String, Object> initializeResult() {
        Map<String, Object> serverInfo = new LinkedHashMap<>();
        serverInfo.put("name", "orioncode-backoffice-mcp");
        serverInfo.put("version", "1.0.0");

        Map<String, Object> tools = new LinkedHashMap<>();
        tools.put("listChanged", false);

        Map<String, Object> capabilities = new LinkedHashMap<>();
        capabilities.put("tools", tools);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", "2024-11-05");
        result.put("serverInfo", serverInfo);
        result.put("capabilities", capabilities);
        return result;
    }

    private Map<String, Object> toolsListResult() {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("filter", Map.of("type", "string", "description", "Campo a filtrar: firstName, lastName, teamId, positionId o id"));
        properties.put("search", Map.of("type", "string", "description", "Texto a buscar"));
        properties.put("page", Map.of("type", "integer", "description", "Pagina inicial en 1", "default", 1));
        properties.put("size", Map.of("type", "integer", "description", "Tamano de pagina", "default", 20));
        properties.put("sortBy", Map.of("type", "string", "description", "Campo de orden", "default", "firstName"));
        properties.put("sortDir", Map.of("type", "string", "enum", List.of("asc", "desc"), "default", "asc"));

        Map<String, Object> inputSchema = new LinkedHashMap<>();
        inputSchema.put("type", "object");
        inputSchema.put("properties", properties);

        Map<String, Object> tool = new LinkedHashMap<>();
        tool.put("name", TOOL_NAME);
        tool.put("description", "Busca colaboradores en OrionCode con filtros y paginacion");
        tool.put("inputSchema", inputSchema);

        return Map.of("tools", List.of(tool));
    }

    private Map<String, Object> successResponse(Object id, Map<String, Object> result) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("result", result);
        return response;
    }

    private Map<String, Object> errorResponse(Object id, int code, String message, Object data) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", code);
        error.put("message", message);
        error.put("data", data);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        response.put("error", error);
        return response;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object value) {
        if (value instanceof Map<?, ?> rawMap) {
            return (Map<String, Object>) rawMap;
        }
        return Map.of();
    }

    private String asText(Object value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }
}

