package com.mohankumar27.langchain4j.resource;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersistentStore implements ChatMemoryStore {

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // TODO: Load JSON from Database/Redis by memoryId
        return null;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        // TODO: Save List<ChatMessage> as JSON to Database/Redis
    }

    @Override
    public void deleteMessages(Object memoryId) {
        // TODO: Delete JSON from Database/Redis by memoryId
    }
}
