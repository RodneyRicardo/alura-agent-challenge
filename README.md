🤖 Assistente Financeiro IA - Challenge Alura
📖 Descrição Geral do Projeto
Este projeto é um agente inteligente financeiro desenvolvido para o Challenge da Alura. Ele utiliza Inteligência Artificial para responder a dúvidas de clientes de uma fintech, baseando-se exclusivamente em documentos internos (PDFs), como políticas de segurança, limites de PIX e taxas. O agente garante respostas precisas sem alucinações, atuando como um assistente de primeiro nível para o suporte ao cliente.

🏗️ Arquitetura da Solução
A aplicação foi construída utilizando o padrão RAG (Retrieval-Augmented Generation). O fluxo funciona da seguinte forma:

Ingestão: Os documentos em PDF são lidos e fatiados em pequenos segmentos de texto.
Vetorização (Embeddings): Cada segmento de texto é convertido em um vetor matemático utilizando um modelo local (All-MiniLM-L6-v2) e armazenado na memória.
Recuperação (Retrieval): Quando o usuário faz uma pergunta, a API busca no banco vetorial os trechos do PDF que mais se assemelham ao contexto da dúvida.
Geração (Generation): Os trechos encontrados são enviados ao LLM (Large Language Model) via API, que formula uma resposta natural baseada estritamente nos dados extraídos.
🛠️ Tecnologias e Ferramentas Utilizadas
Java 17+
Spring Boot 3 (Criação da API REST)
LangChain4j (Orquestração do RAG e integração com LLM)
Groq API (Provedor do modelo LLM llama-3.3-70b-versatile, utilizando o cliente OpenAI nativo)
Apache PDFBox (Leitura e extração de texto dos documentos PDF)
All-MiniLM-L6-v2 (Modelo ONNX para geração de Embeddings locais)
Maven (Gerenciamento de dependências)
🚀 Instruções para Executar o Projeto
Clone o repositório para sua máquina local:
Bash
git clone [https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git](https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git)
Acesse a pasta do projeto:
Bash
cd rag-onebr
Abra o arquivo src/main/resources/application.properties e insira sua chave de API da Groq:
Propeties

openai.api-key=sua_chave_da_groq_aqui
server.port=8080
Compile e inicie a aplicação através do Maven:
Bash
mvn spring-boot:run
(Ou execute a classe principal diretamente pela sua IDE).

A aplicação estará disponível na porta 8080.
💬 Exemplos de Uso Pergunta baseada no documento de Transações e Limites:

"Qual é o meu limite diário para transferências via PIX de madrugada?"

Requisição via Terminal (cURL):

curl -X POST http://localhost:8080/chat \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "Qual é o meu limite diário para transferências via PIX de madrugada?"
Resposta gerada pelo agente RAG:

"De acordo com as informações fornecidas, o seu limite diário para transferências via PIX de madrugada (período noturno, das 20h00 às 05h59) é de R$ 1.000,00."
