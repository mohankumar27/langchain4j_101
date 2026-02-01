package com.mohankumar27.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;

public interface TravelAssistant {

    @Agent(outputKey = "travel_options")
    ResultWithAgenticScope<String> travelPlan(String destination, String dates);
}
