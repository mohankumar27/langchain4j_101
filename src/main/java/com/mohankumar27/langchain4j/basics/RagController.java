package com.mohankumar27.langchain4j.basics;

import com.mohankumar27.langchain4j.service.RagAssistant;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final RagAssistant assistant;

    public RagController(ChatModel chatModel, EmbeddingModel embeddingModel,
                         EmbeddingStore<TextSegment> embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore) // The store we populated above
                .embeddingModel(embeddingModel) // The same model used for ingestion
                .maxResults(2)                  // Only bring back the top 2 matches
                .minScore(0.7)                  // Ignore irrelevant results (score 0.0 to 1.0)
                .build();

        this.assistant = AiServices.builder(RagAssistant.class)
                .chatModel(chatModel)
                .contentRetriever(contentRetriever)
                .build();
    }

    // Return relevant document chunks based on matching search query
    @GetMapping("/manual")
    public List<String> manual(@RequestParam String query) {
        // 1. Embed the query
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        // Define a filter: Department must be HR AND year must be 2023
        Filter metadataFilter = metadataKey("department").isEqualTo("HR")
                .and(metadataKey("year").isEqualTo("2025"));

        // 2. Search the store for the 2 most relevant segments
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(2) // Only bring back the top 2 matches
                .minScore(0.7) // Ignore irrelevant results (score 0.0 to 1.0)
//                .filter(metadataFilter)
                .build();
        EmbeddingSearchResult<TextSegment> relevantMatches = embeddingStore.search(request);

        return relevantMatches.matches()
                .stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.toList());
    }

    // Gets relevant document chunks based on matching search query, passes as context to LLM and gets the result
    @GetMapping("/chat")
    public String chat(@RequestParam String query) {
        return assistant.chat(query);
    }
}
