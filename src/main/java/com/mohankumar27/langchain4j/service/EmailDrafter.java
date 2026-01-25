package com.mohankumar27.langchain4j.service;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface EmailDrafter {
    @UserMessage("Write a polite email to {{name}} regarding {{topic}}.")
    String draftEmail(@V("name") String name, @V("topic") String topic);
}
