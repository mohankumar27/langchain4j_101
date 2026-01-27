package com.mohankumar27.langchain4j.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

public class SensitiveTopicGuardrail implements InputGuardrail {

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();

        // Simple keyword blocking (in production, use appropriate model configured for validating sensitive topic)
        if (text.contains("politics") || text.contains("hack")) {
            // Stop execution immediately
            return fatal("I cannot discuss this topic.");
        }

        return success();
    }
}
