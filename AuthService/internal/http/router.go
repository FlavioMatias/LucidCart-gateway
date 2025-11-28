package http

import (
	"github.com/gin-gonic/gin"
	"github.com/FlavioMatias/LucidCart-gateway/internal/user"
	"github.com/FlavioMatias/LucidCart-gateway/internal/http/middleware"
	"crypto/rsa"
)


func NewRouter(userHandler *user.Handler, publicKey *rsa.PublicKey) *gin.Engine {
	r := gin.Default()

	api := r.Group("/api")
	v1 := api.Group("/v1")

	// ---------------------
	// ROTAS SEM AUTENTICAÇÃO
	// ---------------------
	auth := v1.Group("/auth")
	{
		auth.POST("/signup", userHandler.Signup)
		auth.POST("/signin", userHandler.Signin)
	}

	// ---------------------
	// ROTAS PROTEGIDAS
	// ---------------------
	protected := v1.Group("/")
	protected.Use(middleware.AuthMiddleware(publicKey))
	{
		protected.POST("/profile/photo", userHandler.UploadProfilePhoto)
	}

	return r
}
