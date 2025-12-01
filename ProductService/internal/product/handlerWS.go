package product

import (
	"encoding/json"
	"log"
	"net/http"

	"github.com/gorilla/websocket"
)

var upgrader = websocket.Upgrader{
	CheckOrigin: func(r *http.Request) bool {
		return true // libera geral
	},
}

type ProductWS struct {
	service *Service
}

func NewProductWS(service *Service) *ProductWS {
	return &ProductWS{service: service}
}

func (p *ProductWS) HandleConn(w http.ResponseWriter, r *http.Request) {
	conn, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Println("[WS] Erro ao fazer upgrade:", err)
		return
	}
	defer conn.Close()

	log.Println("[WS] Cliente conectado")

	for {
		_, message, err := conn.ReadMessage()
		if err != nil {
			log.Println("[WS] Cliente desconectou:", err)
			break
		}

		query := string(message)
		log.Println("[WS] Recebido:", query)

		results := p.service.Search(query)

		for _, prod := range results {
			jsonData, err := json.Marshal(prod)
			if err != nil {
				log.Println("[WS] Erro ao converter JSON:", err)
				continue
			}

			if err := conn.WriteMessage(websocket.TextMessage, jsonData); err != nil {
				log.Println("[WS] Erro ao enviar:", err)
				break
			}
		}

		conn.WriteMessage(websocket.TextMessage, []byte("__END__"))
	}
}