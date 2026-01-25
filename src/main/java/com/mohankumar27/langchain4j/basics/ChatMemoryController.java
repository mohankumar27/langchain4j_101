package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.service.Assistant;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memory")
public class ChatMemoryController {
    private final Assistant assistant;

    public ChatMemoryController(Assistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/chat/{userId}")
    public String chat(@PathVariable String userId, @RequestParam String message) {
        // LangChain4j automatically retrieves the memory for this specific userId
        // adds the new message, sends the full context to the LLM,
        // and adds the LLM's response back to memory.
        return assistant.chat(userId, message);
    }
}
