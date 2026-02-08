package com.mohankumar27.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CodingTools {

    interface Coder {
        @UserMessage("Write code for {{spec}}. Previous feedback: {{feedback}}")
        @Agent(outputKey = "code")
        String writeCode(@V("spec") String spec, @V("feedback") String feedback);
    }

    interface Reviewer {
        @UserMessage("Review this code: {{code}}. Return APPROVED or FAILED.")
        @Agent(outputKey = "review_status")
        String review(@V("code") String code);
    }

    interface Editor {
        @UserMessage("Edit this code: {{code}}")
        @Agent(outputKey = "code")
        String edit(@V("code") String code);
    }
}
