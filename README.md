# BookBazaar-Backend

BookBazaar is an e-commerce website project build with Spring Boot backend and React frontend. The backend communicates with the Google Books API to fetch book information, while the frontend provides user and admin interfaces. This is the backend spring boot rest api. Frontend link is provided [here](https://github.com/Jawahirullah481/BookBazaar-Frontend).

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Pages](#pages)
  - [User Pages](#user-pages)
  - [Admin Pages](#admin-pages)

## Features

- User authentication (login, signup, logout)
- User pages: Home, Cart, Orders, Favourites, Book Details, Invoice
- Admin pages: Login, All Books, All Users, All Orders, Account Settings

## Technologies Used

- Spring Boot 3.1.5
- React 18.2.0
- H2 Database
- Spring Data JPA
- Axios
- Google Books API

## Getting Started

To run the project locally, follow these steps:

1. Clone the repository(backend rest api):

   ```git clone https://github.com/your-username/BookBazaar-Backend.git```


## Api Endpoints

1. **Authentication Endpoints**
   * POST /users/signup
   * POST /users/login
   * POST /admin/login
  
     
2. **Books Endpoints**
   * GET /books
   * GET /books/info/{isbn}
   * GET /books/popularBooks
   * GET /books/search?q={searchText}
   * GET /books/view/{isbn}
   * GET /books/filter?q={searchText}&author={author}&category={category}

3. **User Endpoints**
   * GET /users/{userid}/books/{isbn}
   * GET /users/{userid}/favourites
   * PUT /users/{userid}/favourites/{isbn}
   * DELETE /users/{userid}/favourites/{isbn}
   * GET /users/{userid}/cart
   * PUT /users/{userid}/cart/{isbn}
   * DELETE /users/{userid}/cart/{isbn}
   * PUT /users/{userid}/cart/{isbn}/{quantity}
   * GET /users/{userid}/orders
   * POST /users/{userid}/buy/{isbn}
   * GET /users/{userid}/buy/cart
   * POST /users/{userid}/buy/cart
   * GET /users/{userid}/userdetails
   * PUT /users/{userid}/userdetails
   * GET /users/{userid}/address
   * POST /users/{userid}/address

4. **Admin Endpoints**
   * GET /admin/books?page={page}&count={count}
   * GET /admin/books/count
   * GET /admin/books/search?isbn={isbn}
   * PUT /admin/books/{isbn}/{price}/{quantity}
   * GET /admin/users?page={page}&count={count}
   * GET /admin/users/count
   * GET /admin/users/search?userid={userid}
   * GET /admin/orders?page={page}&count={count}
   * GET /admin/orders/count
   * GET /admin/orders/pending?page={page}&count={count}
   * GET /admin/orders/search?orderid={orderid}
   * GET /admin/orders/{orderid}/items
   * PUT /admin/orders/{orderid}/status/{status}
   * GET /admin/account
   

## Pages

### User Pages

1. **Home:**
   - Overview of available books.

2. **Cart:**
   - Manage your shopping cart.

3. **Orders:**
   - View your order history.

4. **Favourites:**
   - Manage your favorite books.

5. **Book Details:**
   - Detailed information about a specific book.

6. **Invoice Page:**
   - View order items and buy.

7. **Login, Logout, and Signup:**
   - User authentication.


### Admin Pages

1. **Login:**
   - Admin authentication.

2. **All Books:**
   - Manage all available books.

3. **All Users:**
   - View users details.

4. **All Orders:**
   - View and manage all orders.

5. **Admin Account Settings:**
   - Update admin account details.
