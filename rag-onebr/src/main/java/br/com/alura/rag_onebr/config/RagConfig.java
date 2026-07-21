package br.com.alura.rag_onebr.config;

import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    /**
     * 1. O Banco de Dados Vetorial:
     * Para este desafio, um banco em memória é perfeito e não exige infraestrutura extra.
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    /**
     * 2. O Ingestor:
     * É ele quem pega o Documento, corta em pedaços, converte em vetores (Embeddings) e salva no banco.
     */
    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(EmbeddingModel embeddingModel,
                                                         EmbeddingStore<TextSegment> embeddingStore) {

        // Regra de Chunking: Corta o texto a cada 500 caracteres (tokens).
        // O "overlap" de 50 caracteres garante que não cortemos uma frase ou ideia no meio.
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(500, 50);

        return EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel) // Injetado automaticamente pelo Spring Boot Starter
                .embeddingStore(embeddingStore)
                .build();
    }
}