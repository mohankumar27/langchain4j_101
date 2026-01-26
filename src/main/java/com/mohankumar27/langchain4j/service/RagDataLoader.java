package com.mohankumar27.langchain4j.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RagDataLoader implements ApplicationRunner {

    @Value("classpath:docs/company-policy.txt")
    private Resource policyResource;

    private final EmbeddingStoreIngestor ingestor;

    // Spring injects the Ingestor (which you configured in your Config class)
    public RagDataLoader(EmbeddingStoreIngestor ingestor) {
        this.ingestor = ingestor;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        System.out.println("🚀 Loading RAG documents...");

        // 1. Load the Document
        // LangChain4j has loaders for File, URL, S3, etc.
        Document document = FileSystemDocumentLoader.loadDocument(policyResource.getFile().getPath());
        // set Metadata
        document.metadata().putAll(Map.of("year", "2025", "department", "HR"));

        // 2. Ingest (Split -> Embed -> Store)
        ingestor.ingest(document);

        System.out.println("✅ RAG Ingestion Complete!");
    }
}
