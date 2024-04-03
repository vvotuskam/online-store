# Halyk Online Store
Done by ```Asulan Maksut```

This project is a simple CRUD (Create, Read, Update, Delete) API for managing products in an online store. The application provides basic operations for creating, reading, updating, and deleting products from the catalog.

### Instructions for Running the Application

Follow these steps to run the application using Docker Compose:

1. **Build the Project:**
   Ensure you have Maven installed on your system. Navigate to the root directory of the project and run the following command to build the project:

   ```bash
   mvn clean package
   ```

2. **Run Docker Compose:**
   After successfully building the project, you can start the application using Docker Compose. Run the following command:

   ```bash
   docker-compose up -d
   ```

   This command will start the application in detached mode, allowing you to continue using your terminal.

3. **Access Swagger Documentation:**
   Once the application is up and running, you can access the Swagger documentation to explore and interact with the API. Open your web browser and navigate to:

   ```
   http://localhost:8080/swagger-ui/index.html
   ```

   This will open the Swagger UI, where you can view all available endpoints and execute requests.

### Authorization

The API endpoints may require authorization. A default user is provided with the following credentials:

- Username: `user`
- Password: `password`

Use these credentials when prompted for authentication.

### Additional Notes

- Ensure Docker and Docker Compose are installed on your system before running the application.
- Make sure no other services are running on port 8080 as it is used by the Spring Boot application.
- If you encounter any issues during the setup or execution, refer to the project documentation or seek assistance from project maintainers.