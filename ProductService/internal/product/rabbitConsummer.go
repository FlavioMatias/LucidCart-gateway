package product

import (
	"encoding/json"
	"fmt"
	"log"

	amqp "github.com/rabbitmq/amqp091-go"
)

type OrderCreatedEvent struct {
	OrderId int `json:"orderId"`
	UserId  int `json:"userId"`
	Items   []struct {
		ProductId int `json:"productId"`
		Quantity  int `json:"quantity"`
	} `json:"items"`
}

func StartOrderConsumer(service *Service) {
	conn, err := amqp.Dial("amqp://admin:admin@rabbitmq:5672/")
	if err != nil {
		log.Fatalf("Erro conectando ao RabbitMQ: %v", err)
	}

	ch, err := conn.Channel()
	if err != nil {
		log.Fatalf("Erro abrindo canal: %v", err)
	}

	exchangeName := "orders.topic"
	queueName := "product.reservation.q"
	routingKey := "order.created"

	// 1) Declare EXCHANGE (topic)
	err = ch.ExchangeDeclare(
		exchangeName,
		"topic",
		true,
		false,
		false,
		false,
		nil,
	)
	if err != nil {
		log.Fatalf("Erro declarando exchange: %v", err)
	}

	// 2) Declare QUEUE
	_, err = ch.QueueDeclare(
		queueName,
		true,
		false,
		false,
		false,
		nil,
	)
	if err != nil {
		log.Fatalf("Erro declarando queue: %v", err)
	}

	// 3) BINDING QUEUE -> EXCHANGE
	err = ch.QueueBind(
		queueName,
		routingKey,
		exchangeName,
		false,
		nil,
	)
	if err != nil {
		log.Fatalf("Erro no binding: %v", err)
	}

	fmt.Println("🐇✔ Ready → Listening order.created")

	msgs, err := ch.Consume(
		queueName,
		"",
		true,
		false,
		false,
		false,
		nil,
	)
	if err != nil {
		log.Fatalf("Erro iniciando consumer: %v", err)
	}

	go func() {
		for msg := range msgs {

			var event OrderCreatedEvent
			err := json.Unmarshal(msg.Body, &event)
			if err != nil {
				log.Printf("Erro lendo evento: %v", err)
				continue
			}

			fmt.Println("Pedido recebido no ProductService!")
			fmt.Printf("Pedido %d do usuário %d\n", event.OrderId, event.UserId)

			for _, item := range event.Items {
				fmt.Printf("Reservado → Produto %d | Qtd: %d\n",
					item.ProductId, item.Quantity)
			}

			fmt.Println("Reserva simulada com sucesso.")
		}
	}()

	select {}
}
