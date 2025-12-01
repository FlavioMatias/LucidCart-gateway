package middleware

import (
	"crypto/rsa"
	"errors"
	"net/http"

	"github.com/golang-jwt/jwt/v5"
)

func WSAuthMiddleware(publicKey *rsa.PublicKey, next http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {

		tokenStr := r.URL.Query().Get("token")
		if tokenStr == "" {
			http.Error(w, "token ausente", http.StatusUnauthorized)
			return
		}
		token, err := jwt.Parse(tokenStr, func(t *jwt.Token) (interface{}, error) {
			if _, ok := t.Method.(*jwt.SigningMethodRSA); !ok {
				return nil, errors.New("algoritmo inválido")
			}
			return publicKey, nil
		})

		if err != nil || !token.Valid {
			http.Error(w, "token inválido", http.StatusUnauthorized)
			return
		}

		if claims, ok := token.Claims.(jwt.MapClaims); ok {
			if sub, ok := claims["sub"].(string); ok {
				r.Header.Set("user_id", sub)
			}
		}

		next(w, r)
	}
}
