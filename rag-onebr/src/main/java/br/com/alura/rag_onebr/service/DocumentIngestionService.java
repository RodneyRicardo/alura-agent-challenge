package br.com.alura.rag_onebr.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DocumentIngestionService {

    private static final Logger log = LoggerFactory.getLogger(DocumentIngestionService.class);

    private final EmbeddingStoreIngestor ingestor;

    public DocumentIngestionService(EmbeddingStoreIngestor ingestor) {
        this.ingestor = ingestor;
    }

    @PostConstruct
    public void init() {
        log.info("Iniciando a leitura e vetorização dos PDFs...");

        // Lê todos os PDFs da pasta
        Path directoryPath = Paths.get("src/main/resources/docs/");
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(directoryPath, new ApachePdfBoxDocumentParser());

        // Faz o Chunking e salva no banco vetorial em memória
        ingestor.ingest(documents);

        log.info("Documentos ingeridos com sucesso! O Agente está pronto para responder perguntas.");
    }
}