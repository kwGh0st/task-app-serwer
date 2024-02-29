# Task Management App live at: https://usersandtodos.netlify.app

Task Management App is a feature-rich fullstack application designed for efficient task management. The application is built using Spring Boot for the backend and React Vite for the frontend, providing users with a seamless and secure task management experience.

## Key Features

### 1. Registration and Login

- Users can easily create an account through the registration process.
- Secure login with protection against authentication attacks.

### 2. Email Confirmation

- After registration, users receive an email with a unique confirmation link.
- Email confirmation enhances the security of user accounts.

### 3. User Roles

- The application implements two roles: "User" and "Admin."
- "User" role provides basic functionalities for task management.
- "Admin" role has additional privileges for user and administrative panel management.

### 4. Task Management

- Users can perform various actions on tasks, including adding, editing, deleting, and marking tasks as completed.
- Tasks are stored in a MySQL database, ensuring persistent data storage.

### 5. Forgot Password

- Users can recover their passwords through a secure password-reset process.
- Password-reset link is sent to users via email.

### 6. Security Measures

- Advanced security techniques, including password hashing and JWT token encryption, are implemented to protect user data.

## Technologies

### Backend

- Spring Boot
- MySQL (database)
- Spring Security (authentication and authorization)
- JWT (Access Tokens)

### Frontend

- React Vite
- React Router (navigation)
- Axios (communication with the backend)
- Context API (state management)

## Additional Highlights

- **Responsiveness:** The application is designed to be responsive, ensuring optimal usage across various devices.
- **Aesthetic Design:** Attention is given to an aesthetically pleasing and clear user interface, enhancing the overall user experience.
