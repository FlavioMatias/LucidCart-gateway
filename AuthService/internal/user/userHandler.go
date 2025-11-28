package user

import (
	"fmt"
	"net/http"
	"path/filepath"
	"time"

	"github.com/gin-gonic/gin"
)

type Handler struct {
	service Service
}

func NewHandler(s Service) *Handler {
	return &Handler{service: s}
}

func (h *Handler) Signup(c *gin.Context) {
	var dto SignUpDTO
	if err := c.ShouldBindJSON(&dto); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	user, err := h.service.Signup(dto.Email, dto.Password)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusCreated, gin.H{
		"id":    user.ID,
		"email": user.Email,
	})
}

func (h *Handler) Signin(c *gin.Context) {
	var dto SignInDTO
	if err := c.ShouldBindJSON(&dto); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	token, err := h.service.Login(dto.Email, dto.Password)
	if err != nil {
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"token": token,
	})
}

func (h *Handler) UploadProfilePhoto(c *gin.Context) {
	userId := c.GetInt("user_id") // vem do middleware JWT

	file, err := c.FormFile("photo")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "arquivo inválido"})
		return
	}

	ext := filepath.Ext(file.Filename)
	if ext == "" {
		ext = ".png"
	}

	filename := fmt.Sprintf("%d_%d%s", userId, time.Now().UnixNano(), ext)
	fullPath := "uploads/profile/" + filename

	if err := c.SaveUploadedFile(file, fullPath); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "erro ao salvar arquivo"})
		return
	}

	if err := h.service.UpdatePhoto(uint(userId), fullPath); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "erro ao registrar no banco"})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"photo_url": "/static/profile/" + filename,
	})
}
