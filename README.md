# Payment Gateway Backend

A Spring Boot based Payment Gateway Backend application that provides a REST API for processing payments through multiple payment methods such as **UPI, Credit Card, Debit Card, and Net Banking**.

The application is designed using a service-based architecture where the payment method is selected dynamically based on the request.

---

## 🚀 Technologies Used

* Java 21
* Spring Boot 4.1.1
* Spring Web
* Spring Security
* Spring Data JPA
* MySQL
* Maven
* H2 Database
* REST API
* Git & GitHub

---

## 📁 Project Structure

```text
paymentGateway
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.interface_example.paymentGateway
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       ├── entity
│   │   │       ├── repository
│   │   │       ├── request
│   │   │       └── service
│   │   │
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│
├── pom.xml
├── .gitignore
└── README.md
```

---

## ⚙️ Requirements

Before running the project, make sure you have:

* JDK 21 or higher
* Maven
* MySQL
* STS / Eclipse / IntelliJ IDEA
* Git

Check Java:

```bash
java -version
```

Check Maven:

```bash
mvn -version
```

---

## 🔧 Configuration

Configure your database details in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/payment_gateway
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

Replace `YOUR_PASSWORD` with your local MySQL password.

> Do not commit real database passwords, API keys, or other secrets to GitHub.

---

## ▶️ How to Run

### Using STS

1. Open the project in STS.
2. Right-click the project.
3. Select:

```text
Run As → Spring Boot App
```

The application will start on:

```text
http://localhost:8080
```

### Using Maven

Run:

```bash
mvn clean
```

Then:

```bash
mvn spring-boot:run
```

---

## 🔐 Security

Spring Security is configured for the application.

The security configuration is located at:

```text
config/SecurityConfig.java
```

The configuration controls access to the application's REST endpoints and handles security-related settings such as CORS.

---

## 💳 Payment API

### Process Payment

**Endpoint:**

```http
POST /api/payments
```

**Base URL:**

```text
http://localhost:8080
```

### Example Request

```json
{
    "amount": 1500,
    "paymentMethod": "UPI"
}
```

Depending on the implementation of the request model, additional payment information may be required.

### Example Payment Methods

```text
UPI
CREDIT_CARD
DEBIT_CARD
NET_BANKING
```

---

## 🔄 Payment Processing Flow

```text
Client
   |
   v
Payment REST API
   |
   v
Payment Controller
   |
   v
Payment Service
   |
   +----> UPI Payment Service
   |
   +----> Credit Card Payment Service
   |
   +----> Debit Card Payment Service
   |
   +----> Net Banking Payment Service
   |
   v
Payment Response
```

The application selects the appropriate payment service based on the payment method provided by the client.

---

## 🧩 Payment Services

The backend contains separate services for different payment methods.

### UPI Payment

Handles payments made through UPI.

### Credit Card Payment

Handles credit card payment processing.

### Debit Card Payment

Handles debit card payment processing.

### Net Banking Payment

Handles net banking payment processing.

---

## 🌐 CORS

CORS is configured in the backend to allow requests from the frontend application.

This is useful when the React frontend and Spring Boot backend are running on different ports.

Example:

```text
Frontend:
http://localhost:3001

Backend:
http://localhost:8080
```

---

## 🧪 Testing

The project includes Spring Boot testing dependencies.

Run tests using:

```bash
mvn test
```

---

## 🛠️ Build the Application

Create the executable JAR:

```bash
mvn clean package
```

The generated JAR will be available inside:

```text
target/
```

Run the JAR using:

```bash
java -jar target/paymentGateway-0.0.1-SNAPSHOT.jar
```

---

## 📌 API Testing

You can test the REST APIs using:

* Postman
* Insomnia
* Thunder Client
* Frontend application

Example:

```http
POST http://localhost:8080/api/payments
Content-Type: application/json
```

Request:

```json
{
    "amount": 1500,
    "paymentMethod": "UPI"
}
```

---

## 📦 Maven Commands

Clean the project:

```bash
mvn clean
```

Compile:

```bash
mvn compile
```

Run tests:

```bash
mvn test
```

Package the application:

```bash
mvn package
```

Run the application:

```bash
mvn spring-boot:run
```

---

## 🔄 Git Commands

To update the GitHub repository:

```bash
git status
git add .
git commit -m "Update payment gateway backend"
git pull origin main
git push origin main
```

---

## 👨‍💻 Author

**Vishal Kasaudhan**

Java / Spring Boot Developer

GitHub:

```text
https://github.com/vishalkasaudhan123
```

---

## 📄 License

This project is created for learning, development, and demonstration purposes.
