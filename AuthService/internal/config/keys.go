package config

import (
	"crypto/rsa"
	"crypto/x509"
	"encoding/pem"
	"errors"
	"fmt"
	"os"
)

func LoadPrivateKey(path string) (*rsa.PrivateKey, error) {
    b, err := os.ReadFile(path)
    if err != nil {
        return nil, err
    }

    block, _ := pem.Decode(b)
    if block == nil {
        return nil, fmt.Errorf("chave privada inválida")
    }

    parsedKey, err := x509.ParsePKCS8PrivateKey(block.Bytes)
    if err != nil {
        return nil, err
    }

    key, ok := parsedKey.(*rsa.PrivateKey)
    if !ok {
        return nil, fmt.Errorf("chave privada não é RSA")
    }

    return key, nil
}


func LoadPublicKey(path string) (*rsa.PublicKey, error) {
    data, err := os.ReadFile(path)
    if err != nil {
        return nil, err
    }

    block, _ := pem.Decode(data)
    if block == nil {
        return nil, errors.New("pem inválido")
    }

    pub, err := x509.ParsePKIXPublicKey(block.Bytes)
    if err != nil {
        return nil, err
    }

    rsaPub, ok := pub.(*rsa.PublicKey)
    if !ok {
        return nil, errors.New("chave não é RSA")
    }

    return rsaPub, nil
}