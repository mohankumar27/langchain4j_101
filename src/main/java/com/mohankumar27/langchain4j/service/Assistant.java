package com.mohankumar27.langchain4j.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {
    @SystemMessage("You are a helpful customer support agent. Be concise.")
    String chat(@MemoryId String userId, @UserMessage String userMessage);
}
