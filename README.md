# Todo List Service

Simple WebMVC and RESTful API service for users to create and manage a basic todo list. 

Written in Java 17 with Spring Boot, using Redis to persist data.

By Oscar Bergström ([obstrom](https://github.com/obstrom)) for Java Enterprise course project. This is only a demo and assignment project.

## Local setup

### Requires
* Java JDK 17
* Redis (or Docker)

### Setup
1. Clone or download the project.
2. Run Redis on localhost.
   1. **Via Docker:** From the root of this project run ``docker-compose up -d`` (Requires Docker)
   2. **Via installation:** Install and run Redis via your preferred package manager or from [redis.io](https://redis.io/).
3. Make sure ports `8080` and `6379` are free. 

*Port and other settings can be changed in `src/main/resources/application.properties`.*
   
To launch the service. Navigate with your terminal to the root of the project and run `./gradlew bootRun`.

## Using the service

The default Admin user is `admin` with password `root` unless edited in the `application.properties`-file.

### Web-view
Navigate to [localhost:8080](http://localhost:8080/) in your browser.

### API

Access the API using any REST client software like Postman by making requests to endpoints under ``localhost:8080/api/v1/``

Authorization type is __Basic Auth__.

### API overview

#### User management

| Method | Endpoint               | Role  | Request body (JSON)                  | Description                    |
|--------|------------------------|-------|--------------------------------------|--------------------------------|
| GET    | `/`                    | Admin |                                      | Returns all users              |
| GET    | `/{id}`                | Admin |                                      | Returns user by given ID       |
| GET    | `/username/{username}` | Admin |                                      | Returns user by given username |
| GET    | `/self`                | Any   |                                      | Returns this user              |
| POST   | `/`                    | Any   | `{ "username": "", "password": "" }` | Creates new non-admin user     |
| POST   | `/admin`               | Admin | `{ "username": "", "password": "" }` | Creates new __admin__ user     |
| DELETE | `/{id}`                | Admin |                                      | Deletes user by given ID       |

#### Todo management

All requests made for Todos are:
* Scoped to the users role. Admins cannot and are not intended to manage other users todos.
* Scoped to the user (i.e. account) making the request. You can only access and change todos of the user you are making requests as.

| Method | Endpoint          | Role  | Request body (RAW) | Description                      |
|--------|-------------------|-------|--------------------|----------------------------------|
| GET    | `/`               | User  |                    | Returns all your todos           |
| GET    | `/active`         | User  |                    | Returns all your active todos    |
| GET    | `/completed`      | User  |                    | Returns all your completed todos |
| GET    | `/id/{id}`        | User  |                    | Returns single todo by ID        |
| POST   | `/`               | User  | `message`          | Creates new active todo          |
| PATCH  | `/{id}/message`   | User  | `message`          | Updates todo message by ID       |
| PATCH  | `/{id}/completed` | User  |                    | Marks todo as completed by ID    |
| DELETE | `/{id}`           | User  |                    | Delete todo by ID                |