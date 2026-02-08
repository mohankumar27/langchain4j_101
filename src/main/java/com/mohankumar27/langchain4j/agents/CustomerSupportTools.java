package com.mohankumar27.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CustomerSupportTools {

    interface ClassifierAgent {
        @Agent(outputKey = "category")
        @UserMessage("Classify: '{{query}}'. Return exactly: REFUND or PRODUCT.")
        String classify(@V("query") String query);
    }

    interface RefundAgent {
        @Agent(outputKey = "response")
        @SystemMessage("You are a customer service support agent intended for responding tasks related to refund queries." +
                "Respond to customer query")
        String respondToQuery(String query);

    }

    interface ProductAgent {
        @Agent(outputKey = "response")
        @SystemMessage("You are a customer service support agent intended for responding tasks related to product queries." +
                "Respond to customer query")
        String respondToQuery(String query);
    }
}
