package com.mohankumar27.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface SupervisorTools {

    interface StockTool {
        @UserMessage("Get price for {{symbol}}")
        @Agent(description = "Gets the current stock price for a given symbol.", outputKey = "price")
        String getPrice(@V("symbol") String symbol);
    }

    interface NewsTool {
        @UserMessage("Get news for {{company}}")
        @Agent(description = "Gets recent news headlines for a company.", outputKey = "news")
        String getNews(@V("company") String company);
    }
}
