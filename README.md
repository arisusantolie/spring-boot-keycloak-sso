# Spring Boot Keycloak SSO with TOTP (2FA)

A Spring Boot application demonstrating Single Sign-On (SSO) integration with Keycloak, featuring Time-based One-Time Password (TOTP) for Two-Factor Authentication.

## Features

- **Keycloak SSO Integration**: OAuth 2.0/OpenID Connect authentication
- **Two-Factor Authentication (2FA)**: TOTP implementation with QR code generation
- **Multiple Application Profiles**: Support for running multiple instances
- **Security**: Spring Security with role-based access control
- **Modern UI**: Bootstrap 5 integration with Thymeleaf templates

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.5.3
- **Spring Security**: OAuth 2.0 Client & Resource Server
- **Thymeleaf**: Template engine with Spring Security integration
- **H2 Database**: In-memory database for development
- **Bootstrap**: for responsive UI
- **Maven**: Build tool

## Prerequisites

Before running this application, ensure you have:

1. **Java 21** or higher installed
2. **Maven 3.6+** installed
3. **Keycloak Server** running (version 22.0+)
4. **Git** for cloning the repository

## Keycloak Configuration

### 1. Set up Keycloak Server

1. Download and start Keycloak server
2. Access the admin console (usually at `http://localhost:8080/admin`)
3. Create a new realm or use the master realm

### 2. Configure Keycloak Client

Create a new client in your Keycloak realm with the following settings:

- **Client ID**: `spring-boot-sso-client` (or your preferred name)
- **Client Type**: `OpenID Connect`
- **Valid Redirect URIs**: 
  - `http://localhost:9090/login/oauth2/code/keycloak` (for app1)
  - `http://localhost:9091/login/oauth2/code/keycloak` (for app2)
- **Web Origins**: 
  - `http://localhost:9090`
  - `http://localhost:9091`

## Application Configuration

### Update Configuration Files

You need to update the application properties files with your Keycloak configuration:

#### `src/main/resources/application-app1.properties`
#### `src/main/resources/application-app2.properties`

### Configuration Parameters to Replace

Replace the following placeholders with your actual Keycloak setup:

- `YOUR_CLIENT_ID`: The client ID you created in Keycloak
- `YOUR_CLIENT_SECRET`: The client secret from Keycloak (if using confidential client)
- `YOUR_KEYCLOAK_HOST`: Your Keycloak server host (e.g., `localhost`)
- `YOUR_REALM_NAME`: The name of your Keycloak realm

## Running the Application

### Option 1: Using Maven Wrapper (Recommended)

#### Run Application 1 (Port 9090):
```bash
bash ./mvnw spring-boot:run -Dspring-boot.run.profiles=app1
```
#### Run Application 2 (Port 9091):
```bash
bash ./mvnw spring-boot:run -Dspring-boot.run.profiles=app2
```

### Option 2: Using IDE

1. Run the `Application.java` class
2. Set the active profile to either `app1` or `app2`
3. Or set VM options: `-Dspring.profiles.active=app1`

## Accessing the Applications

- **Application 1**: http://localhost:9090
- **Application 2**: http://localhost:9091
- **H2 Database Console**: http://localhost:9090/h2-console (or 9091 for app2)

Additional : 
For TOTP 2FA keycloak tutorial, can visit in my personal blog at :https://arisusantolie.com/blog/enable-two-factor-authentication-2fa-in-keycloak-using-totp-and-rest-api-qr-code/

