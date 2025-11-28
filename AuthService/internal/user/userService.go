package user

import (
	"crypto/rsa"
	"errors"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

type Service interface {
	Signup(email, password string) (*User, error)
	Login(email, password string) (string, error)
	UpdatePhoto(userId uint, path string) error
}

type service struct {
	repo       Repository
	privateKey *rsa.PrivateKey
	publicKey  *rsa.PublicKey
}

func NewService(repo Repository, privateKey *rsa.PrivateKey) Service {
	return &service{
		repo:       repo,
		privateKey: privateKey,
		publicKey:  &privateKey.PublicKey,
	}
}

func (s *service) Signup(email, password string) (*User, error) {
	// verificar se já existe
	exists, err := s.repo.FindByEmail(email)
	if err != nil && !errors.Is(err, gorm.ErrRecordNotFound) {
		// erro real do DB
		return nil, err
	}
	if exists != nil && exists.ID != 0 {
		return nil, errors.New("email já usado")
	}

	hash, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	if err != nil {
		return nil, err
	}

	u := &User{
		Email:    email,
		Password: string(hash),
	}

	if err := s.repo.Create(u); err != nil {
		return nil, err
	}

	return u, nil
}

func (s *service) Login(email, password string) (string, error) {
	u, err := s.repo.FindByEmail(email)
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return "", errors.New("usuário não encontrado")
		}
		return "", err // erro real do DB
	}

	if bcrypt.CompareHashAndPassword([]byte(u.Password), []byte(password)) != nil {
		return "", errors.New("senha incorreta")
	}

    token := jwt.NewWithClaims(jwt.SigningMethodRS256, jwt.MapClaims{
        "sub":   u.ID,
        "email": u.Email,
        "exp":   time.Now().Add(24 * time.Hour).Unix(),
    })
	signed, err := token.SignedString(s.privateKey)
	if err != nil {
		return "", err
	}

	return signed, nil
}

func (s *service) UpdatePhoto(userId uint, path string) error {
    return s.repo.UpdatePhoto(userId, path)
}
