package com.mohankumar27.langchain4j.basics;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class SimpleChatController {

    private final ChatModel chatModel;
    private final StreamingChatModel streamingChatModel;

    public SimpleChatController(ChatModel chatModel, StreamingChatModel streamingChatModel) {
        this.chatModel = chatModel;
        this.streamingChatModel = streamingChatModel;
    }

    @GetMapping("/simple")
    public String helloWorld(@RequestParam String query) {
        return chatModel.chat(query);
    }

    @GetMapping("system-chat")
    public String systemChat(@RequestParam String query) {
        // 1. Create the request with multiple messages
        ChatRequest request = ChatRequest.builder()
                .messages(
                        SystemMessage.from("You are a helpful assistant who speaks like a Pirate."),
                        UserMessage.from(query)
                )
                .build();

        // 2. Call the new .chat() method
        ChatResponse response = chatModel.chat(request);

        // 3. Extract the text
        return response.aiMessage().text();
    }

    @GetMapping("/creative-chat")
    public String creativeChat(@RequestParam String query) {
        // 1. Create parameters
        ChatRequestParameters params = ChatRequestParameters.builder()
                .temperature(0.8)
                .build();

        // 2. Build the request with messages AND parameters
        ChatRequest request = ChatRequest.builder()
                .messages(UserMessage.from(query))
                .parameters(params)
                .build();

        // 3. Execute
        ChatResponse response = chatModel.chat(request);
        return response.aiMessage().text();
    }

    @GetMapping("/prompt-chat")
    public String promptChat(@RequestParam String city) {
        // 1. Create the prompt template
        PromptTemplate template = PromptTemplate.from("I am visiting {{city}}. What are the top 3 destination places?");
        Prompt prompt = template.apply(Map.of("city", city));

        // 2. Create the request with the prompt
        ChatRequest request = ChatRequest.builder()
                .messages(
                        SystemMessage.from("You are a helpful Travel Assistant"),
                        UserMessage.from(prompt.text())
                )
                .build();

        // 3. Execute
        ChatResponse response = chatModel.chat(request);

        // 4. Extract the text
        return response.aiMessage().text();
    }


    @GetMapping(value = "/stream")
    public void manualStream(@RequestParam String query) {

        streamingChatModel.chat(query, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                // This prints tokens one by one as they arrive
                System.out.print(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                System.out.println("\nDone!");
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }
}
