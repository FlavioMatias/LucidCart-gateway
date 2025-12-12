package server

import (
	"log"

	"github.com/gin-gonic/gin"

	"github.com/FlavioMatias/LucidCart-gateway/internal/config"
	httpRouter "github.com/FlavioMatias/LucidCart-gateway/internal/http"
	"github.com/FlavioMatias/LucidCart-gateway/internal/product"
)

type Server struct {
	Router *gin.Engine
}

func NewServer() *Server {

	repo := product.NewMockRepository()
	service := product.NewService(repo)
	handler := product.NewHandler(service)
	wsHandler := product.NewProductWS(service)


	publicKey, err := config.LoadPublicKey("internal/config/public.pem")
	if err != nil {
		log.Fatalf("erro ao carregar public.pem: %v", err)
	}

	go product.StartOrderConsumer(service)

	router := httpRouter.NewRouter(handler, wsHandler, publicKey)

	return &Server{Router: router}
}

func (s *Server) Start(addr string) {
	log.Printf("🚀 Servidor de produtos iniciando em %s ...", addr)
	if err := s.Router.Run(addr); err != nil {
		log.Fatalf("erro ao iniciar servidor: %v", err)
	}
}
