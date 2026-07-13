package com.orioncode.mcp.tools.config;

import com.orioncode.mcp.tools.tools.CollaboratorMcpTool;
import com.orioncode.mcp.tools.tools.PositionMcpTool;
import com.orioncode.mcp.tools.tools.ProjectMcpTool;
import com.orioncode.mcp.tools.tools.TeamMcpTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    ToolCallbackProvider toolCallbackProvider(
            CollaboratorMcpTool collaboratorMcpTool,
            ProjectMcpTool projectMcpTool,
            TeamMcpTool teamMcpTool,
            PositionMcpTool positionMcpTool
    ) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(collaboratorMcpTool, projectMcpTool, teamMcpTool, positionMcpTool)
                .build();
    }
}

