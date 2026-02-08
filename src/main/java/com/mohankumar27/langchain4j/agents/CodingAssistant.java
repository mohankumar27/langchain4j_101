package com.mohankumar27.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.service.V;

public interface CodingAssistant {

    @Agent(outputKey = "code")
    ResultWithAgenticScope<String> writeCode(@V("spec")String spec, @V("feedback")String feedback);
}
