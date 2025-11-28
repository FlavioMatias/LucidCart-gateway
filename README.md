<p align="center">
<img width="1456" height="679" alt="Frame 35" src="https://github.com/user-attachments/assets/88751af3-2cfe-4b25-8364-cff52aec1c09" />

</p>

# Lucid Cart

**Lucid Cart** is a modern e-commerce web application designed to provide a smooth shopping experience, secure data handling, and a robust architecture. With Lucid Cart, you can purchase top-quality products quickly and safely, through an intuitive interface focused on usability and performance.

---

## Technologies Used

- **Frontend:** Angular, Tailwind CSS  
- **Gateway/API:** Kotlin, Spring Boot  
- **OrderService:** Kotlin, Spring WS  
- **AuthService:** Go, GinGonic, JWT  
- **Containerization:** Docker  

---

## Service Architecture

| Service       | Technology                     | Purpose                                                                 |
|---------------|--------------------------------|-------------------------------------------------------------------------|
| **Frontend**  | Angular + Tailwind             | Responsive and user-friendly web interface                              |
| **Gateway**   | Kotlin + Spring Boot           | Routing, authentication, and coordination between services             |
| **OrderService** | Kotlin + Spring WS          | Manages orders, items, and shopping cart                                |
| **AuthService** | Go + Gin + JWT               | User registration, login, and profile photo upload                      |

> Each service can be run individually. Example to run AuthService:  
> ```bash
> docker build -f .docker/Dockerfile.auth -t authservice .
> docker run -p 8081:8081 authservice
> ```

---

## API Endpoints

### Authentication

- **POST `/api/v1/auth/signup`** – Create a new user  
  **Request:** `{ "email": "user@example.com", "password": "password123" }`  
  **Response:** `{ "id": 1, "email": "user@example.com" }`

- **POST `/api/v1/auth/signin`** – Authenticate user and return JWT  
  **Request:** `{ "email": "user@example.com", "password": "password123" }`  
  **Response:** `{ "token": "jwt-token-here" }`

- **POST `/api/v1/profile/photo`** – Upload profile photo  
  **Header:** `Authorization: Bearer {token}`  
  **Response:** `{ "photo_url": "photo_url.png" }`

---

### Address Endpoints

- **POST `/api/v1/address`** – Create address  
- **PUT `/api/v1/address`** – Update address  
- **DELETE `/api/v1/address`** – Delete address  
- **GET `/api/v1/address`** – Retrieve user address  

> All require header: `Authorization: Bearer {token}`

---

### Order Endpoints

#### Order Items

- **POST `/api/v1/orders/items`** – Add item  
- **PUT `/api/v1/orders/items/{id}`** – Update item  
- **DELETE `/api/v1/orders/items/{id}`** – Remove item  

#### Orders

- **GET `/api/v1/orders/{id}`** – Order details  
- **GET `/api/v1/orders`** – List orders (filters: status, userId, orderId)  
- **DELETE `/api/v1/orders/{id}`** – Delete order  

#### Shopping Cart

- **GET `/api/v1/orders/cart`** – Get user's cart  
- **POST `/api/v1/orders`** – Finalize order (`?id={orderId}`)  

> All require header: `Authorization: Bearer {token}`

---

### Swagger / Interactive Documentation

Swagger documentation is available after starting the Gateway:  

```

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

```

---

With **Lucid Cart**, online shopping becomes fast, secure, and effortless. The modular architecture allows easy maintenance, expansion, and integration with future services.
