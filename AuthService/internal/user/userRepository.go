package user

import "gorm.io/gorm"

type Repository interface {
    Create(u *User) error
    FindByEmail(email string) (*User, error)
    UpdatePhoto(id uint, path string) error 
}

type repository struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) Repository {
	return &repository{db}
}

func (r *repository) Create(u *User) error {
	return r.db.Create(u).Error
}

func (r *repository) FindByEmail(email string) (*User, error) {
	var user User
	err := r.db.Where("email = ?", email).First(&user).Error
	return &user, err
}

func (r *repository) UpdatePhoto(id uint, path string) error {
    return r.db.Model(&User{}).Where("id = ?", id).
        Update("profile_photo_path", path).Error
}

