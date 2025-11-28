package server

import (
	"log"
	"os"

	"github.com/gin-gonic/gin"
	"github.com/FlavioMatias/LucidCart-gateway/internal/config"
	"github.com/FlavioMatias/LucidCart-gateway/internal/user"
	httpRouter "github.com/FlavioMatias/LucidCart-gateway/internal/http"
)

type Server struct {
	Router *gin.Engine
}

func NewServer() *Server {
	// garantir onde salvar imagens
	if err := os.MkdirAll("uploads/profile", 0755); err != nil {
		log.Fatalf("erro ao criar pasta uploads/profile: %v", err)
	}

	// banco
	db := config.NewSQLite("data.db", &user.User{})

	privateKey, err := config.LoadPrivateKey("internal/config/keys/private.pem")
	if err != nil {
		log.Fatalf("erro ao carregar private.pem: %v", err)
	}

	publicKey, err := config.LoadPublicKey("internal/config/keys/public.pem")
	if err != nil {
		log.Fatalf("erro ao carregar public.pem: %v", err)
	}

	repo := user.NewRepository(db)
	svc := user.NewService(repo, privateKey)
	handler := user.NewHandler(svc)

	router := httpRouter.NewRouter(handler, publicKey)

	return &Server{Router: router}
}

func (s *Server) Start(addr string) {
	log.Printf("🚀 Servidor iniciando em %s ...", addr)
	if err := s.Router.Run(addr); err != nil {
		log.Fatalf("erro ao iniciar servidor: %v", err)
	}
}
