package com.orioncode.mcp.tools;

import com.orioncode.mcp.tools.service.McpToolService;
import com.orioncode.mcp.tools.tools.CollaboratorMcpTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = CollaboratorMcpToolTest.Config.class)
class CollaboratorMcpToolTest {

    @Autowired
    private CollaboratorMcpTool collaboratorMcpTool;

    @MockBean
    private McpToolService mcpToolService;

    @Test
    void shouldDelegateSearchToolToService() {
        Map<String, Object> expectedPayload = Map.of("data", Map.of("id", "EMP001"));
        when(mcpToolService.searchCollaborators(Map.of(
                "filter", "firstName",
                "search", "juan",
                "page", 1,
                "size", 20,
                "sortBy", "firstName",
                "sortDir", "asc"
        ))).thenReturn(expectedPayload);

        Map<String, Object> result = collaboratorMcpTool.searchCollaborators(
                "firstName",
                "juan",
                1,
                20,
                "firstName",
                "asc"
        );

        assertThat(result).isEqualTo(expectedPayload);
    }

    @TestConfiguration
    static class Config {
        @Bean
        CollaboratorMcpTool collaboratorMcpTool(McpToolService mcpToolService) {
            return new CollaboratorMcpTool(mcpToolService);
        }
    }
}

