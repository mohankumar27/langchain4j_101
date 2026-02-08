package com.mohankumar27.langchain4j.agents;

import dev.langchain4j.agentic.declarative.SupervisorAgent;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;
import dev.langchain4j.service.V;

public interface SupervisorAssistant {

    @SupervisorAgent(
            responseStrategy = SupervisorResponseStrategy.SUMMARY,
            subAgents = {SupervisorTools.NewsTool.class, SupervisorTools.StockTool.class}
    )
    ResultWithAgenticScope<String> query(@V("request") String request);
}
