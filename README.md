# multigenesystask
# Customer Order Management System

## Description
This is a Spring Boot application that manages customers and orders. It includes authentication and authorization using JWT, allowing different user roles (ADMIN and USER) with specific permissions.

## Features
- Customer management (CRUD operations)
- Order management (CRUD operations)
- JWT-based authentication and authorization
- Custom API endpoints for order statistics

## Prerequisites
Before running this application, ensure you have the following installed:

- [Java 11]
- [Maven]
- [MySQL Workbench]
- And required dependencies (Spring Boot, Spring Security, JWT, Hibernate, etc.)

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/itsganesh1997/multigenesystas.git
   ```
2. Navigate to the project directory:
   ```sh
   cd multigenesystas
   ```
3. Configure the database:
   - Update `application.properties` file with your database credentials.
   - Run migrations if applicable.

4. Build the application:
   ```sh
   mvn clean install
   ```

## Running the Application
To start the Spring Boot application, run:
```sh
mvn spring-boot:run
```
My application runs on `http://localhost:2001`.

## API Endpoints
### Authentication
- `POST /api/auth/login` - User login and JWT token generation
- `POST /api/auth/adminRegister` - Register a new user (Admin)

### Customer APIs (Admins Only)
- `GET /api/task/getAllcustomers` - Get all customers
- `GET /api/task/getCustomerByCustomerId` - Get a specific customer
- `POST /api/task/getAllCustomers` - Create a new customer
- `PUT /api/task/saveOrUpdateCustomer` - save Or Update a customer
- `DELETE /api/task/deleteCustomerById` - Delete a customer

### Order APIs
- `POST /api/task/placeOrders` - Place an order (Users only)
- `GET /api/task/getAllOrders` - View all orders (Admins only)
- `GET /api/task/getOrdersByCustomerId` - View order details (Admins and specific Users)
- `PUT /api/task/updateOrderStatus` - Update order status (Admins only)
- `GET /api/task/getAllOrdersByDates` - Get orders within a date range (Admins only)
- `GET /api/task/getTotalAmountSpentByCustomer` - Get total amount spent by a customer (Admins and the specific customer)

## Security & Authentication
- Implements **JWT-based authentication**.
- Users have roles: `ADMIN` and `USER`.
- Admins can manage all customers and orders.
- Users can only view and place their own orders.

## Guidelines
1. Follow clean code principles and best practices.
2. Avoid hardcoding sensitive data like JWT secrets or database credentials.
3. Provide meaningful responses for all API calls.
4. Ensure proper exception handling and logging


