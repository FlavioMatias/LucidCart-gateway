package config

import (
	"log"

	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

func NewSQLite(path string, models ...interface{}) *gorm.DB {
	db, err := gorm.Open(sqlite.Open(path), &gorm.Config{})
	if err != nil {
		log.Fatalf("erro ao abrir sqlite: %v", err)
	}

	if err := db.AutoMigrate(models...); err != nil {
		log.Fatalf("erro ao migrar sqlite: %v", err)
	}

	return db
}
