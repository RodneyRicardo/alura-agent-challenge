package br.com.alura.rag_onebr.controller;

import br.com.alura.rag_onebr.service.AssistenteFinanceiro;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final AssistenteFinanceiro assistente;

    // Injeção de dependência do Spring Boot
    public ChatController(AssistenteFinanceiro assistente) {
        this.assistente = assistente;
    }

    @PostMapping
    public String conversar(@RequestBody String pergunta) {
        // Recebe a pergunta do usuário via requisição HTTP e repassa para a IA
        return assistente.responder(pergunta);
    }
}
