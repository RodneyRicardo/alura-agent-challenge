package br.com.alura.rag_onebr.service;

import dev.langchain4j.service.SystemMessage;

public interface AssistenteFinanceiro {

    @SystemMessage({
            "Você é um assistente virtual de uma instituição financeira.",
            "Sua função é responder dúvidas dos clientes baseando-se ESTRITAMENTE nos documentos fornecidos pelo sistema.",
            "Se a resposta não estiver no documento, diga educadamente que não possui essa informação.",
            "Nunca invente taxas, valores ou regras."
    })
    String responder(String perguntaUsuario);
}