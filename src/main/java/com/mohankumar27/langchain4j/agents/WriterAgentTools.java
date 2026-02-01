package com.mohankumar27.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface WriterAgentTools {

    // Agent 1: The Writer
    // Reads 'topic' from scope, writes result to 'draft'
    interface Writer {
        @Agent(outputKey = "draft")
        @UserMessage("Write a concise 100-word blog post about {{topic}}.")
        String writeDraft(@V("topic") String topic);
    }

    // Agent 2: The Editor
    // Reads 'draft' from scope, writes result to 'critique'
    interface Editor {
        @Agent(outputKey = "critique")
        @UserMessage("""
        Critique the following draft. Point out logical errors and tone issues.
        Draft: {{draft}}
        """)
        String critiqueDraft(@V("draft") String draft);
    }


    // Agent 3: The Publisher
    // Reads 'draft' and 'critique', produces the final output to 'final_post'
    interface Publisher {
        @Agent(outputKey = "final_post")
        @UserMessage("""
        Rewrite the draft based on the critique. 
        Format it as clean HTML.
        
        Original Draft: {{draft}}
        Critique: {{critique}}
        """)
        String finalizePost(@V("draft") String draft, @V("critique") String critique);
    }
}
