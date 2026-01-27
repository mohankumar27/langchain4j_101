package com.mohankumar27.langchain4j.guardrail;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;

public class JsonFormatGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String content = aiMessage.text();

        if (!content.trim().startsWith("{")) {
            // The LLM failed to return JSON.
            // We don't fail; we ask it to try again with specific feedback.
            return reprompt("invalid json format",
                    "You must return valid JSON starting with '{'. Do not include markdown text."
            );
        }

        return success();
    }
}
