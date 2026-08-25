**💳 Payment Gateway Application**

A full-stack Payment Gateway Application built using React.js and Spring Boot. The application supports multiple payment methods including UPI, Credit Card, Debit Card, and Net Banking, with separate handling for successful and failed transactions.

🚀 Features
💰 UPI Payment
💳 Credit Card Payment
💳 Debit Card Payment
🏦 Net Banking Payment
✅ Payment amount and payment-detail validation
🔄 Unique transaction ID generation
🔐 Card number masking
💾 Successful transaction persistence
❌ Failed transaction persistence
🔗 REST API integration
🧪 Unit Testing using JUnit 5 and Mockito
🔬 Integration Testing using Spring Boot Test and MockMvc
🗄️ MySQL database integration



**🛠️ Technologies Used**

**Backend**
Java 21
Spring Boot
Spring Web
Spring Data JPA
Hibernate
MySQL
Maven
JUnit 5
Mockito
AssertJ


**Frontend**
React.js
JavaScript
Axios
HTML
CSS

**🏗️ Application Architecture**
                    React.js Frontend
                           │
                           │ REST API
                           ▼
                  Payment Controller
                           │
                           ▼
             Payment Transaction Service
                           │
             ┌─────────────┼─────────────┐
             │             │             │
             ▼             ▼             ▼
            UPI         Card          Net Banking
                        Payment
             │             │             │
             └─────────────┼─────────────┘
                           │
                           ▼
                    Spring Data JPA
                           │
                           ▼
                    MySQL Database

                    
**💳 Supported Payment Methods**

**1. UPI**

The application validates the payment amount and UPI ID before processing the transaction.

{
  "amount": 1000,
  "method": "upi",
  "upi": "test@upi"
}

**2. Credit Card**

The application validates the card number and CVV. The card number is masked before being stored/displayed.

{
  "amount": 1000,
  "method": "credit",
  "card": "1234567890121234",
  "cvv": "123"
}

**3. Debit Card**
{
  "amount": 1000,
  "method": "debit",
  "card": "1234567890121234",
  "cvv": "123"
}

**4. Net Banking**
{
  "amount": 1000,
  "method": "netbanking",
  "bank": "HDFC Bank"
}

**🔄 Payment Processing Flow**
User
 │
 ▼
React.js Application
 │
 ▼
POST /api/payments
 │
 ▼
PaymentController
 │
 ▼
PaymentTransactionService
 │
 ▼
Select Payment Method
 │
 ├── UPI
 ├── Credit Card
 ├── Debit Card
 └── Net Banking
 │
 ▼
Payment Validation
 │
 ├── Valid
 │     │
 │     ▼
 │   Successful Payment
 │     │
 │     ▼
 │   Success Transaction
 │     │
 │     ▼
 │   MySQL
 │
 └── Invalid
       │
       ▼
     Failed Payment
       │
       ▼
     Failed Transaction
       │
       ▼
     MySQL

     
**📁 Backend Project Structure**
src
└── main
    ├── java
    │   └── com.interface_example.interfaceExample
    │       │
    │       ├── controller
    │       │   └── PaymentController.java
    │       │
    │       ├── dto
    │       │   └── PaymentRequest.java
    │       │
    │       ├── entity
    │       │   ├── SuccessPaymentTransaction.java
    │       │   └── FailedPaymentTransaction.java
    │       │
    │       ├── repository
    │       │   ├── SuccessPaymentTransactionRepository.java
    │       │   └── FailedPaymentTransactionRepository.java
    │       │
    │       ├── service
    │       │   ├── PaymentService.java
    │       │   ├── PaymentTransactionService.java
    │       │   ├── UPIPaymentService.java
    │       │   ├── CreditCardPaymentService.java
    │       │   ├── DebitCardPaymentService.java
    │       │   └── NetBankingPaymentService.java
    │       │
    │       └── util
    │           └── TransactionIdGenerator.java
    │
    └── resources
        └── application.properties

        
**🧪 Testing**

The application includes both Unit Testing and Integration Testing.

**Unit Testing**

Unit tests are implemented using:

JUnit 5
Mockito
AssertJ

Unit tests focus on individual payment gateway services.

UPIPaymentServiceTest
        │
        ▼
UPIPaymentService
        │
        ├── Mock TransactionIdGenerator
        ├── Mock Success Repository
        └── Mock Failed Repository

The tests cover scenarios such as:

Successful UPI payment
Invalid payment amount
Missing UPI ID
Blank UPI ID
Null payment request
Successful transaction persistence
Failed transaction persistence

Similar unit tests can be created for:

CreditCardPaymentService
DebitCardPaymentService
NetBankingPaymentService

**🔬 Integration Testing**

Integration tests verify the complete Spring Boot application flow.

MockMvc
   │
   ▼
PaymentController
   │
   ▼
PaymentTransactionService
   │
   ▼
Payment Gateway Service
   │
   ├── UPI
   ├── Credit Card
   ├── Debit Card
   └── Net Banking
   │
   ▼
Repository
   │
   ▼
MySQL Database

Integration tests verify:

REST API request and response
Successful payment processing
Failed payment processing
Transaction ID generation
Successful transaction persistence
Failed transaction persistence
Transaction retrieval


**🔐 Card Masking**

For card payments, the complete card number is masked before being displayed in the transaction message.

Example:
Original Card:
1234567890121234

Masked Card:
**** **** **** 1234


**🔗 API**

**Process Payment**
POST /api/payments

**Example UPI Request**

{
  "amount": 1000,
  "method": "upi",
  "upi": "test@upi"
}

**Example Successful Response**
{
  "success": true,
  "transactionId": "TXN-123456789ABC",
  "message": "UPI payment of ₹1000 to test@upi successful"
}



**🗄️ Database**

The application uses MySQL for transaction persistence.

Successful and failed transactions are maintained separately:

SuccessPaymentTransaction
        │
        ▼
Successful Payments


FailedPaymentTransaction
        │
        ▼
Failed Payments



**⚙️ Configuration**

Configure the MySQL database in:

src/main/resources/application.properties

**Example:**

spring.datasource.url=jdbc:mysql://localhost:3306/payment_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update

server.port=8080

Update the database name, username, and password according to your local environment.

**▶️ How to Run**

**1. Clone the Backend Repository**
git clone <your-backend-repository-url>

Navigate to the project:

cd payment-gateway-backend

**2. Configure MySQL**

Create the database:

CREATE DATABASE payment_db;

Then update your application.properties with your MySQL credentials.

**3. Run the Spring Boot Application**

Using Maven:

mvn spring-boot:run

Or using Maven Wrapper on Windows:

mvnw.cmd spring-boot:run

On Linux/Mac:

./mvnw spring-boot:run

The backend will run on:

http://localhost:8080

  
**🧪 Run Tests**

Run all unit and integration tests using Maven:

mvn test

To clean and run tests:

mvn clean test

To build the project:

mvn clean install


**🌐 Frontend Setup**

Clone the frontend repository:

git clone <your-frontend-repository-url>

Navigate to the React project:

cd payment-gateway-frontend

Install dependencies:

npm install

Start the React application:

npm start

Make sure the Spring Boot backend is running before making payment requests from the frontend.

**📌 Project Highlights**
Developed a full-stack payment gateway using React.js and Spring Boot.
Supports UPI, Credit Card, Debit Card, and Net Banking.
Implemented a common PaymentService interface for different payment methods.
Maintained business logic inside the Service layer.
Controller is responsible for handling HTTP requests and responses.
Implemented separate persistence for successful and failed transactions.
Added reusable transaction ID generation using TransactionIdGenerator.
Implemented card number masking for card transactions.
Integrated Spring Boot REST APIs with the React.js frontend.
Used Spring Data JPA and MySQL for transaction persistence.
Implemented JUnit 5 and Mockito unit tests.
Implemented Spring Boot MockMvc integration tests.
Built and managed the backend using Maven and pom.xml.
Developed using Java 21.


**👨‍💻 Author**

**Vishal Kasaudhan**

Java Developer | Spring Boot | React.js | MySQL
