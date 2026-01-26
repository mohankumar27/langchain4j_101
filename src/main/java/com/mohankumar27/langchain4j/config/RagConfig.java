package com.mohankumar27.langchain4j.config;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    @Bean
    public EmbeddingModel embeddingModel(){
        // Create the Embedding Model
        // This model converts text into vectors.
        // You can use OpenAI, VertexAI, or a local in-process model (like all-minilm).
        // We use a local, free model to turn text into numbers (embeddings).
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(){
        // Create the Embedding Store (Vector Database)
        // For this tutorial, we use an in-memory store.
        // In production, you would use MongoDB Atlas, Pinecone, or PgVector [6].
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public EmbeddingStoreIngestor ingestor(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        // 1. Split into Chunks
        // We use a Recursive splitter to try and keep paragraphs together [5].
        // 300 tokens per chunk, with an overlap of 20 tokens to preserve context at the edges.
        DocumentSplitter splitter = DocumentSplitters.recursive(100, 10);

        // 2. Configure the Ingestor
        // LangChain4j provides a utility to wire this all together.
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }
}
