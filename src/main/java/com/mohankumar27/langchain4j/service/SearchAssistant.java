package com.mohankumar27.langchain4j.service;

import dev.langchain4j.service.SystemMessage;

public interface SearchAssistant {

    @SystemMessage("You are a Search assistant having access to search the internet. Use appropriate tools to answer the query")
    String chat(String userMessage);
}
