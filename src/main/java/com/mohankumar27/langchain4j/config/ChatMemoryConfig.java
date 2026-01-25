package com.mohankumar27.langchain4j.config;

import com.mohankumar27.langchain4j.resource.PersistentStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {

    @Bean
    public ChatMemoryProvider chatMemoryProvider(PersistentStore store) {
        // This provider creates a new InMemory storage to store the ChatMemory for every conversation/user.
        // We use a MessageWindow to keep the last 10 messages.
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .build();

        // TokenWindowChatMemory Config
//        return memoryId -> TokenWindowChatMemory
//                .builder()
//                .id(memoryId)
//                .maxTokens(1000, new OpenAiTokenCountEstimator("gpt-4o"))
//                .build();

        // ChatMemory with persistent storage
//        return memoryId -> MessageWindowChatMemory.builder()
//                .id(memoryId)
//                .maxMessages(20)
//                .chatMemoryStore(store) // <--- Connect the DB here
//                .build();
    }
}
