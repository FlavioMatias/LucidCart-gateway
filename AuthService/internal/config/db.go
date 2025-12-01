package config

import (
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

func NewSQLiteMemory(models ...interface{}) *gorm.DB {
    db, err := gorm.Open(sqlite.Open(":memory:"), &gorm.Config{})
    if err != nil {
        panic(err)
    }

    if err := db.AutoMigrate(models...); err != nil {
        panic(err)
    }

    return db
}