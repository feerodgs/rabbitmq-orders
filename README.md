# RabbitMQ Orders

Projeto prático de estudo sobre mensageria assíncrona com **Java**, **Spring Boot**, **Spring AMQP** e **RabbitMQ**.

A aplicação simula o início de um fluxo de processamento de pedidos: uma API REST recebe um pedido, publica um evento no RabbitMQ e um consumer processa essa mensagem de forma assíncrona.
    
## Objetivo

O objetivo deste projeto é praticar conceitos fundamentais de mensageria:

- Comunicação assíncrona
- Producer e Consumer
- Exchange, Queue, Binding e Routing Key
- Direct Exchange
- Spring AMQP
- `RabbitTemplate`
- `@RabbitListener`
- Conversão de objetos Java para JSON
- Acknowledgment automático
- Observabilidade com RabbitMQ Management UI

## Arquitetura atual

```text
Cliente / Postman
      |
      | POST /orders
      v
OrderController
      |
      | cria OrderCreatedEvent
      v
OrderProducer
      |
      | RabbitTemplate.convertAndSend(...)
      | exchange: orders.direct
      | routing key: order.created
      v
+---------------------------------------+
|               RabbitMQ                |
|                                       |
|  Exchange: orders.direct              |
|  Tipo: direct                         |
|             |                         |
|             | binding: order.created  |
|             v                         |
|  Queue: orders.processing             |
+---------------------------------------+
      |
      v
OrderProcessingConsumer
      |
      | @RabbitListener
      v
Processamento simulado + ACK automático
```

## Fluxo da mensagem

1. O cliente envia um `POST /orders`.
2. `OrderController` recebe o JSON como `CreateOrderRequest`.
3. O controller gera `orderId` e `eventId`.
4. O controller cria um `OrderCreatedEvent`.
5. `OrderProducer` publica o evento com `RabbitTemplate`.
6. A exchange `orders.direct` recebe a mensagem.
7. A routing key `order.created` corresponde ao binding configurado.
8. O RabbitMQ encaminha a mensagem para a fila `orders.processing`.
9. `OrderProcessingConsumer` recebe o evento com `@RabbitListener`.
10. Ao finalizar o processamento sem erro, o Spring envia o ACK automático.

## Topologia RabbitMQ

| Recurso | Nome | Descrição |
|---|---|---|
| Exchange | `orders.direct` | Exchange do tipo `direct` para eventos de pedidos |
| Queue | `orders.processing` | Fila que armazena pedidos aguardando processamento |
| Routing key | `order.created` | Chave usada para rotear o evento de pedido criado |

## Estrutura do projeto

```text
src/main/java/br/com/felipe/rabbitmq_orders
├── config
│   ├── RabbitMqConfig.java
│   └── RabbitMqNames.java
│
├── controller
│   └── OrderController.java
│
├── dto
│   ├── CreateOrderRequest.java
│   └── OrderCreatedEvent.java
│
├── messaging
│   ├── OrderProducer.java
│   └── OrderProcessingConsumer.java
│
└── RabbitmqOrdersApplication.java
```

## Tecnologias

- Java 17
- Spring Boot
- Spring Web
- Spring AMQP
- RabbitMQ
- Docker
- Maven
- Lombok
- Jakarta Validation

## Pré-requisitos

Antes de executar o projeto, tenha instalado:

- Java 17 ou superior
- Maven
- Docker Desktop
- Git

## Subindo o RabbitMQ

Execute o RabbitMQ com a interface de gerenciamento:

```bash
docker run -d --name rabbitmq-estudo \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=admin \
  -e RABBITMQ_DEFAULT_PASS=admin123 \
  rabbitmq:3-management
```

No PowerShell do Windows, você pode executar o comando em uma única linha:

```powershell
docker run -d --name rabbitmq-estudo -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin123 rabbitmq:3-management
```

Após iniciar o container, acesse o RabbitMQ Management UI:

```text
http://localhost:15672
```

Credenciais locais:

```text
Usuário: admin
Senha: admin123
```

## Configuração da aplicação

O arquivo `src/main/resources/application.yaml` usa a seguinte configuração local:

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin123
    virtual-host: /
```

A porta `5672` é usada pela aplicação Spring Boot para comunicação AMQP.

A porta `15672` é usada somente pela interface web de gerenciamento do RabbitMQ.

## Executando a aplicação

Clone o repositório:

```bash
git clone https://github.com/SEU_USUARIO/rabbitmq-orders.git
```

Acesse o diretório:

```bash
cd rabbitmq-orders
```

Suba o RabbitMQ usando Docker e execute a aplicação:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

## Testando a API

Envie uma requisição para criar um pedido:

```http
POST http://localhost:8080/orders
Content-Type: application/json
```

Body:

```json
{
  "customerId": "customer-1",
  "productId": "product-1",
  "quantity": 2
}
```

Resposta esperada:

```text
HTTP 202 Accepted
```

No console da aplicação, o consumer deve registrar algo semelhante a:

```text
Pedido enviado para processamento:
OrderCreatedEvent(eventId=..., orderId=..., customerId=customer-1, productId=product-1, quantity=2)
```

## Como observar no RabbitMQ

No Management UI, use estas abas:

| Aba | O que verificar |
|---|---|
| `Connections` | Se a aplicação Spring Boot está conectada ao RabbitMQ |
| `Channels` | Canais AMQP usados por producer e consumer |
| `Exchanges` | Exchange `orders.direct` e seus bindings |
| `Queues and Streams` | Fila `orders.processing`, mensagens `Ready`, `Unacked` e consumers |
| Detalhe da queue | Binding, consumer ativo e payload de mensagens pendentes |

Com a aplicação rodando e consumindo rapidamente, o esperado é:

```text
Ready: 0
Unacked: 0
Consumers: 1
```

## RabbitMqNames

A classe `RabbitMqNames` centraliza os nomes da infraestrutura RabbitMQ:

```java
ORDERS_EXCHANGE = "orders.direct";
ORDERS_QUEUE = "orders.processing";
ORDER_CREATED = "order.created";
```

Isso evita inconsistências entre os componentes.

Exemplo de problema evitado:

```text
Producer publica: order.created
Binding espera:   orders.created
```

Em uma `DirectExchange`, a correspondência entre routing key e binding key precisa ser exata.

## Próximos passos

- [ ] Adicionar validação com `@Valid`
- [ ] Simular validação de estoque
- [ ] Registrar status do pedido como `PROCESSING`
- [ ] Criar evento `OrderProcessedEvent`
- [ ] Adicionar `FanoutExchange` para Publish/Subscribe
- [ ] Criar consumer de e-mail
- [ ] Criar consumer de logística
- [ ] Implementar retry com backoff
- [ ] Configurar Dead Letter Queue (DLQ)
- [ ] Implementar consumidor idempotente
- [ ] Adicionar persistência com PostgreSQL
- [ ] Adicionar testes de integração com RabbitMQ
- [ ] Adicionar métricas e monitoramento

## Conceitos praticados

```text
Producer–Consumer
Point-to-Point
Message Broker
Direct Exchange
Binding
Routing Key
Queue
Consumer
Acknowledgment
Comunicação assíncrona
```

## Observação

Este é um projeto de estudo. A versão atual demonstra o fluxo básico de mensageria com RabbitMQ, mas ainda não implementa mecanismos necessários para produção, como retry, DLQ, idempotência, publisher confirms, transactional outbox, persistência e observabilidade completa.