package br.com.alura.rag_onebr.config;

import br.com.alura.rag_onebr.service.AssistenteFinanceiro;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    @Value("${gemini.api-key}")
    private String geminiApiKey;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(geminiApiKey)
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai/")
                .modelName("gemini-1.5-flash")
                .temperature(0.0)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(EmbeddingModel embeddingModel,
                                                         EmbeddingStore<TextSegment> embeddingStore) {
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(500, 50);
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }

    // --- AS NOVIDADES COMEÇAM AQUI ---

    /**
     * O "Buscador": É ele quem vai no banco de dados vetorial, pesquisa os pedaços
     * de PDF que mais combinam com a pergunta e devolve para a IA.
     */
    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore,
                                             EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3) // Traz os 3 pedaços de texto mais relevantes do PDF
                .minScore(0.6) // Grau mínimo de confiança (0.0 a 1.0)
                .build();
    }

    /**
     * O Agente Final: Junta o Cérebro (ChatModel) com os PDFs (ContentRetriever)
     * e implementa automaticamente a nossa interface AssistenteFinanceiro.
     */
    @Bean
    public AssistenteFinanceiro assistenteFinanceiro(ChatLanguageModel chatModel,
                                                     ContentRetriever contentRetriever) {
        return AiServices.builder(AssistenteFinanceiro.class)
                .chatLanguageModel(chatModel)
                .contentRetriever(contentRetriever)
                .build();
    }
}