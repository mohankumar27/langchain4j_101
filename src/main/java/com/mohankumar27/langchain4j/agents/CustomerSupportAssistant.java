package com.mohankumar27.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.service.V;

public interface CustomerSupportAssistant {

    @Agent(outputKey = "response")
    ResultWithAgenticScope<String> respondToQuery(@V("query") String query);
}
