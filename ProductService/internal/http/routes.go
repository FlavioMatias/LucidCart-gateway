package http

import (
	"crypto/rsa"

	"github.com/FlavioMatias/LucidCart-gateway/internal/http/middleware"
	"github.com/FlavioMatias/LucidCart-gateway/internal/product"
	"github.com/gin-gonic/gin"
)

func NewRouter(productHandler *product.Handler, wsHandler *product.ProductWS, publicKey *rsa.PublicKey) *gin.Engine {
    r := gin.Default()

    r.RemoveExtraSlash = true
    api := r.Group("/api")
    v1 := api.Group("/v1")

    {
        products := v1.Group("/products")
        {
            products.GET("", productHandler.List)
            products.GET("/:id", productHandler.GetByID)
        }
    }

    ws := v1.Group("/ws")
    {
        ws.GET("/products", gin.WrapF(middleware.WSAuthMiddleware(publicKey, wsHandler.HandleConn)))
    }
    return r
}