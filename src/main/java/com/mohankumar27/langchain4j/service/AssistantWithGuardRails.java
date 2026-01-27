package com.mohankumar27.langchain4j.service;

import com.mohankumar27.langchain4j.guardrail.JsonFormatGuardrail;
import com.mohankumar27.langchain4j.guardrail.SensitiveTopicGuardrail;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.guardrail.OutputGuardrails;

public interface AssistantWithGuardRails {

    @InputGuardrails(SensitiveTopicGuardrail.class)
    @OutputGuardrails(value = JsonFormatGuardrail.class, maxRetries = 1) // 'maxRetries' -> maximum time to reprompt before failing
    String chat(String userMessage);
}
