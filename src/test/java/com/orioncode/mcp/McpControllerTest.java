package com.orioncode.mcp;

import com.orioncode.backoffice.collaborators.dto.CollaboratorSearchResponseDTO;
import com.orioncode.backoffice.collaborators.service.CollaboratorService;
import com.orioncode.mcp.controller.McpController;
import com.orioncode.mcp.service.McpToolService;
import com.orioncode.shared.dto.PageResponse;
import com.orioncode.shared.dto.PaginationMetadata;
import com.orioncode.shared.dto.SearchMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(McpController.class)
@Import(McpToolService.class)
class McpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CollaboratorService collaboratorService;

    @Test
    void shouldReturnToolsList() throws Exception {
        String body = """
                {
                  "jsonrpc": "2.0",
                  "id": 1,
                  "method": "tools/list"
                }
                """;

        mockMvc.perform(post("/mcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.tools[0].name").value("search_collaborators"));
    }

    @Test
    void shouldCallSearchCollaboratorsTool() throws Exception {
        CollaboratorSearchResponseDTO collaborator = new CollaboratorSearchResponseDTO(
                "EMP001",
                "Juan",
                "Perez",
                "POS-001",
                "Team A",
                List.of("java"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PageResponse<CollaboratorSearchResponseDTO> pageResponse = new PageResponse<>(
                List.of(collaborator),
                new PaginationMetadata(1, 20, 1, 1),
                new SearchMetadata(List.of("firstName", "lastName", "teamId", "positionId"))
        );

        when(collaboratorService.searchCollaborators(
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any()
        )).thenReturn(pageResponse);

        String body = """
                {
                  "jsonrpc": "2.0",
                  "id": 2,
                  "method": "tools/call",
                  "params": {
                    "name": "search_collaborators",
                    "arguments": {
                      "search": "juan",
                      "page": 1,
                      "size": 20
                    }
                  }
                }
                """;

        mockMvc.perform(post("/mcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.structuredContent.data[0].id").value("EMP001"));
    }
}

