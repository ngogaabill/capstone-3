# Capstone 3 – EasyShop (Backend + Frontend)

This project contains a Java Spring Boot backend and a static HTML/CSS/JavaScript frontend.

When you unzip the archive, you should get a folder named `capstone-3` with this structure:

```text
capstone-3/
  backend-api/      # Java Spring Boot backend (Maven)
  frontend-ui/         # Static HTML, CSS, JS
````

---

## Requirements

* **Java Development Kit (JDK) 17**
* **IntelliJ IDEA Community Edition** (latest)
* **MySQL Server** (e.g., MySQL 8.x)
* **MySQL Workbench** (to run the database script)
* Internet browser (Chrome, Firefox, Edge, Safari, etc.)

You do **not** need to install Maven separately; IntelliJ can use its bundled Maven.

---

## How to open the project in IntelliJ

1. Unzip the project so you have a folder called `capstone-3`.
2. Open IntelliJ IDEA (Community).
3. Choose **File → Open...**.
4. Select the `capstone-3` folder and click **Open**.
5. When IntelliJ asks you to “Trust” the project, click **Trust**.
6. IntelliJ will load the project with two modules:

    * `backend-api` – Java Spring Boot backend
    * `frontend-ui` – static HTML/CSS/JS

If IntelliJ asks you to configure an SDK, choose **JDK 17**.

---

## Database setup (MySQL)

Before you run the backend, you must create and initialize the database.

1. Make sure **MySQL Server** is running on your machine.

2. Open **MySQL Workbench**.

3. Connect to your local MySQL server (for example, `localhost` with your MySQL username).

4. In MySQL Workbench, go to **File → Open SQL Script...**.

5. Navigate to the project folder and open:

   ```text
   capstone-3/backend-api/database/create_database_easyshop.sql
   ```

6. Once the script is open in Workbench, click the **Execute** button (the lightning bolt icon) to run the script.

    * This will create the database and any required tables/data for the EasyShop application.

7. In IntelliJ, open:

   ```text
   capstone-3/backend-api/src/main/resources/application.properties
   ```

   and check the database connection settings (URL, username, and password).
   Make sure:

    * The **database name** matches what the SQL script created.
    * The **username and password** match a valid MySQL user on your system.

   If needed, you can either:

    * Update `application.properties` to match your MySQL username/password, **or**
    * Create a MySQL user in Workbench that matches the values in `application.properties`.

Once the script has run successfully and the credentials match, the backend will be able to connect to the database.

---

## How to run the backend

1. In IntelliJ, make sure the project is fully indexed and Maven dependencies have been downloaded (you may see a progress bar at the bottom).
2. In the **Run configuration** dropdown (top-right of IntelliJ), choose:

   **`Backend (Spring Boot)`**

   (If it doesn’t exist, you can run the main class manually by right-clicking the `EasyshopApplication` class in `backend-api` and choosing **Run**.)
3. Click the green **Run** triangle.
4. The Spring Boot application will start and listen on:

   ```text
   http://localhost:8080
   ```

Check the Run tool window for any startup errors (for example, database connection problems). If there are errors, double-check your MySQL setup and `application.properties` values.

---

## How to run the frontend

1. In IntelliJ’s **Project** view, navigate to:

   ```text
   frontend-ui/index.html
   ```

2. Right-click `index.html` → **Open in Browser** → choose your browser.

3. Alternatively, you can locate `frontend-ui/index.html` in Finder / File Explorer and double-click it to open it in a browser.
---

## Project Features & Implementations

This EasyShop application includes the following implemented features:

### Shopping Cart Functionality
* **Shopping Cart DAO** (`MySqlShoppingCartDao`) - Handles all database operations for shopping cart items
* **Shopping Cart Controller** (`ShoppingCartController`) - Manages REST API endpoints for cart operations:
    * `GET /cart` - Retrieve the current user's shopping cart
    * `POST /cart/products/{productId}` - Add a product to the cart
    * `PUT /cart/products/{productId}` - Update product quantity in the cart
    * `DELETE /cart` - Clear all items from the cart
* **Bug Fix** - Resolved product duplication issue that occurred during product updates

### User Profile Management
* Implemented user profile functionality for logged-in users
* Users can view and manage their account information

### Category Management
* Full category implementation for product organization
* Users can browse products by category

### Frontend Enhancements
* Added color styling and visual improvements to the user interface

---

## Technologies Used

### Backend
* Java 17
* Spring Boot
* Spring Security
* MySQL Database
* Maven

### Frontend
* HTML5
* CSS3
* JavaScript
* REST API Integration

---
