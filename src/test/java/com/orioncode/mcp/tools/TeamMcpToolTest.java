package com.orioncode.mcp.tools;

import com.orioncode.mcp.tools.service.McpToolService;
import com.orioncode.mcp.tools.tools.TeamMcpTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = TeamMcpToolTest.Config.class)
class TeamMcpToolTest {

    @Autowired
    private TeamMcpTool teamMcpTool;

    @MockBean
    private McpToolService mcpToolService;

    @Test
    void shouldDelegateSearchToolToService() {
        Map<String, Object> expectedPayload = Map.of("data", Map.of("id", "team-001"));
        when(mcpToolService.searchTeams(Map.of(
                "filter", "name",
                "search", "platform",
                "page", 1,
                "size", 20,
                "sortBy", "name",
                "sortDir", "asc"
        ))).thenReturn(expectedPayload);

        Map<String, Object> result = teamMcpTool.searchTeams(
                "name",
                "platform",
                1,
                20,
                "name",
                "asc"
        );

        assertThat(result).isEqualTo(expectedPayload);
    }

    @TestConfiguration
    static class Config {
        @Bean
        TeamMcpTool teamMcpTool(McpToolService mcpToolService) {
            return new TeamMcpTool(mcpToolService);
        }
    }
}

