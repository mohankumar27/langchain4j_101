package com.mohankumar27.langchain4j.config;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Configuration
public class McpConfig {

    @Value("${tavily.api.key}")
    private String tavilyApiKey;

    // 1. Define the Client as a Bean (so Spring can close it automatically)
    @Bean(destroyMethod = "close")
    public McpClient tavilyMcpClient() {
        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("npx", "-y", "tavily-mcp"))
                .environment(Map.of("TAVILY_API_KEY", tavilyApiKey))
                .logEvents(true)
                .build();

        return new DefaultMcpClient.Builder()
                .transport(transport)
                .toolExecutionTimeout(Duration.ofSeconds(60))
                .build();
    }

    // 2. Define the ToolProvider
    @Bean
    public ToolProvider tavilyToolProvider(McpClient mcpClient) {
        return McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();
    }
}
