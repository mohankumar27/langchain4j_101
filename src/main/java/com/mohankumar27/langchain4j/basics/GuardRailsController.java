package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.service.AssistantWithGuardRails;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guardrails")
public class GuardRailsController {

    private final AssistantWithGuardRails assistant;

    public GuardRailsController(ChatModel chatModel) {
        this.assistant = AiServices.builder(AssistantWithGuardRails.class)
                .chatModel(chatModel)
                .build();
    }

    @RequestMapping("/chat")
    public String chat(String query) {
        return assistant.chat(query);
    }
}
