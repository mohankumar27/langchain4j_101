package com.mohankumar27.langchain4j.service;

import dev.langchain4j.service.SystemMessage;

public interface RagAssistant {

    @SystemMessage("You are a helpful assistant.")
    String chat(String query);
}
