package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.service.SearchAssistant;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp")
public class McpToolChatController {
    private final SearchAssistant assistant;

    public McpToolChatController(ChatModel chatModel, @Qualifier("tavilyToolProvider") ToolProvider tavilyToolProvider) {
        this.assistant = AiServices.builder(SearchAssistant.class)
                .chatModel(chatModel)
                .toolProvider(tavilyToolProvider)
                .build();
    }

    @RequestMapping("/chat")
    public String chat(@RequestParam String query) {
        return assistant.chat(query);
    }
}
