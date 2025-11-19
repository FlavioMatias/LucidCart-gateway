package main

import (
	"github.com/FlavioMatias/LucidCart-gateway/internal/server"
)

func main() {
	s := server.NewServer()
	s.Start(":8080")
}
