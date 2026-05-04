package com.orioncode.mcp.tools.config;

import com.orioncode.mcp.tools.tools.CollaboratorMcpTool;
import com.orioncode.mcp.tools.tools.ProjectMcpTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    ToolCallbackProvider toolCallbackProvider(CollaboratorMcpTool collaboratorMcpTool, ProjectMcpTool projectMcpTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(collaboratorMcpTool, projectMcpTool)
                .build();
    }
}

