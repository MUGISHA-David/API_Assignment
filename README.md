# API_Assignment

## RESTful API Assignment - Spring Boot

This single Spring Boot project contains all questions (1–5 + bonus) implemented using simple in-memory lists and REST controllers only (no service/repository layers).

### How to Run

- **Prerequisites**: JDK 17+, Maven, Internet access for dependency download.
- From the project root directory run:

```bash
mvn spring-boot:run
```

- The application starts on port `8082` by default.

You can test endpoints using Postman or a browser for `GET` requests.

---

### Question 1 – Library Book Management API

**Base path**: `/api/books`

- **GET** `/api/books`  
  - Get all books (3 sample books initialized).

- **GET** `/api/books/{id}`  
  - Get a specific book by id.  
  - **Responses**: `200 OK` with book, `404 NOT FOUND` if missing.

- **GET** `/api/books/search?title={title}`  
  - Search books whose title contains the given text (case-insensitive).

- **POST** `/api/books`  
  - Create a new book.  
  - **Body** (JSON): `Book` object without `id` (assigned by API).  
  - **Response**: `201 CREATED` with created book.

- **DELETE** `/api/books/{id}`  
  - Delete book by id.  
  - **Responses**: `204 NO CONTENT` when deleted, `404 NOT FOUND` otherwise.

---

### Question 2 – Student Registration API

**Base path**: `/api/students`

- **GET** `/api/students`  
  - Get all students (5 samples with different majors and GPAs).

- **GET** `/api/students/{studentId}`  
  - Get a student by id.  
  - **Responses**: `200 OK`, `404 NOT FOUND`.

- **GET** `/api/students/major/{major}`  
  - Get all students with the given major (e.g. `Computer Science`).

- **GET** `/api/students/filter?gpa={minGpa}`  
  - Get students with GPA ≥ `minGpa` (e.g. `3.5`).

- **POST** `/api/students`  
  - Register a new student.  
  - **Response**: `201 CREATED`.

- **PUT** `/api/students/{studentId}`  
  - Update an existing student.  
  - **Responses**: `200 OK` with updated student, `404 NOT FOUND`.

---

### Question 3 – Restaurant Menu API

**Base path**: `/api/menu`

- **GET** `/api/menu`  
  - Get all menu items (8 sample items across Appetizer/Main Course/Dessert/Beverage).

- **GET** `/api/menu/{id}`  
  - Get a specific menu item by id.  
  - **Responses**: `200 OK`, `404 NOT FOUND`.

- **GET** `/api/menu/category/{category}`  
  - Get menu items by category (case-insensitive).

- **GET** `/api/menu/available?available=true|false`  
  - Filter items by availability. Default is `true`.

- **GET** `/api/menu/search?name={name}`  
  - Search items by name (contains, case-insensitive).

- **POST** `/api/menu`  
  - Add a new menu item.  
  - **Response**: `201 CREATED`.

- **PUT** `/api/menu/{id}/availability`  
  - Toggle `available` flag for given item.  
  - **Responses**: `200 OK` with updated item, `404 NOT FOUND`.

- **DELETE** `/api/menu/{id}`  
  - Remove an item.  
  - **Responses**: `204 NO CONTENT`, `404 NOT FOUND`.

---

### Question 4 – E‑Commerce Product API

**Base path**: `/api/products`

- **GET** `/api/products`  
  - Get all products (10 sample products with various categories/brands/prices).  
  - Optional pagination: `/api/products?page={page}&limit={limit}` (0‑based page index).

- **GET** `/api/products/{productId}`  
  - Get product by id.  
  - **Responses**: `200 OK`, `404 NOT FOUND`.

- **GET** `/api/products/category/{category}`  
  - Get products by category.

- **GET** `/api/products/brand/{brand}`  
  - Get products by brand.

- **GET** `/api/products/search?keyword={keyword}`  
  - Search products where `name` or `description` contains `keyword`.

- **GET** `/api/products/price-range?min={min}&max={max}`  
  - Products with price between `min` and `max` (inclusive).

- **GET** `/api/products/in-stock`  
  - Products where `stockQuantity > 0`.

- **POST** `/api/products`  
  - Add new product.  
  - **Response**: `201 CREATED`.

- **PUT** `/api/products/{productId}`  
  - Update all fields of a product.  
  - **Responses**: `200 OK`, `404 NOT FOUND`.

- **PATCH** `/api/products/{productId}/stock?quantity={quantity}`  
  - Set `stockQuantity` for a product.  
  - **Responses**: `200 OK`, `404 NOT FOUND`.

- **DELETE** `/api/products/{productId}`  
  - Delete product.  
  - **Responses**: `204 NO CONTENT`, `404 NOT FOUND`.

---

### Question 5 – Task Management API

**Base path**: `/api/tasks`

- **GET** `/api/tasks`  
  - Get all tasks (sample tasks with different priorities/statuses).

- **GET** `/api/tasks/{taskId}`  
  - Get a task by id.  
  - **Responses**: `200 OK`, `404 NOT FOUND`.

- **GET** `/api/tasks/status?completed=true|false`  
  - Filter tasks by completion status.

- **GET** `/api/tasks/priority/{priority}`  
  - Filter tasks by priority (`LOW`, `MEDIUM`, `HIGH`).

- **POST** `/api/tasks`  
  - Create a new task.  
  - **Response**: `201 CREATED`.

- **PUT** `/api/tasks/{taskId}`  
  - Update a task (all fields).  
  - **Responses**: `200 OK`, `404 NOT FOUND`.

- **PATCH** `/api/tasks/{taskId}/complete`  
  - Mark a task as completed.  
  - **Responses**: `200 OK`, `404 NOT FOUND`.

- **DELETE** `/api/tasks/{taskId}`  
  - Delete a task.  
  - **Responses**: `204 NO CONTENT`, `404 NOT FOUND`.

---

### Bonus – User Profile API

**Base path**: `/api/user-profiles`  
**Response wrapper**: `ApiResponse<T>` with fields `success`, `message`, `data`.

- **GET** `/api/user-profiles`  
  - Get all user profiles wrapped in `ApiResponse<List<UserProfile>>`.

- **GET** `/api/user-profiles/{userId}`  
  - Get profile by id.  
  - **Responses**: `200 OK` with profile, `404 NOT FOUND` with `success=false`.

- **POST** `/api/user-profiles`  
  - Create new profile.  
  - **Response**: `201 CREATED` with `success=true` and created profile.

- **PUT** `/api/user-profiles/{userId}`  
  - Update an existing profile.  
  - **Responses**: `200 OK` with updated data, `404 NOT FOUND`.

- **DELETE** `/api/user-profiles/{userId}`  
  - Delete profile.  
  - **Responses**: `200 OK` success message, `404 NOT FOUND`.

- **GET** `/api/user-profiles/search/username?username={username}`  
  - Search by username (contains, case-insensitive).

- **GET** `/api/user-profiles/search/country?country={country}`  
  - Search by country (exact, case-insensitive).

- **GET** `/api/user-profiles/search/age-range?min={min}&max={max}`  
  - Search by age range \([min, max]\).

- **PATCH** `/api/user-profiles/{userId}/activate`  
  - Activate a profile (`active=true`).

- **PATCH** `/api/user-profiles/{userId}/deactivate`  
  - Deactivate a profile (`active=false`).

---

### Testing Notes

- Use Postman to send requests to the endpoints above.  
- All data is stored in memory only (lists in controllers), so it resets each time the application restarts.  
- Standard HTTP status codes are used: `200 OK`, `201 CREATED`, `204 NO CONTENT`, `404 NOT FOUND`.
