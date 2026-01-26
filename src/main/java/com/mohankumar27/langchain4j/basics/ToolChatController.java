package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.service.MathAssistant;
import com.mohankumar27.langchain4j.service.SimpleAssistant;
import com.mohankumar27.langchain4j.tools.CalculatorTool;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tools")
public class ToolChatController {
    private final MathAssistant mathAssistant;
    private final SimpleAssistant assistant;

    public ToolChatController(ChatModel model, SimpleAssistant assistant) {
        mathAssistant = AiServices.builder(MathAssistant.class)
                .chatModel(model) // Your configured OpenAiChatModel
                .tools(new CalculatorTool()) // <--- Register the tool instance here!
                .build();

        this.assistant = assistant;
    }

    @GetMapping("/math")
    public String solve(@RequestParam String problem) {
        // We simply pass the natural language string to the AI
        return mathAssistant.chat(problem);
    }

    @GetMapping("/weather")
    public String weather(@RequestParam String query) {
        return assistant.chat(query);
    }
}
