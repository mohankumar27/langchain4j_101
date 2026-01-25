package com.mohankumar27.langchain4j.service;

import com.mohankumar27.langchain4j.entity.Customer;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface CustomerExtractor {
    @UserMessage("Extract information from this text: {{text}}")
    Customer extract(@V("text") String text);
}
