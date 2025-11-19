package user

type SignUpDTO struct {
    Email    string `json:"email" binding:"required,email"`
    Password string `json:"password" binding:"required,min=8"`
}

type SignInDTO struct {
    Email    string `json:"email" binding:"required,email"`
    Password string `json:"password" binding:"required"`
}