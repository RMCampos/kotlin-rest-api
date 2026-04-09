package com.example.demo.user

// Please create a UserResource class that will be used to represent the User entity in the API responses. The UserResource class should have the following properties: id, name, email, and age. The class should also have a constructor that takes a User object and initializes the properties accordingly.
// And create CRUD operations for the User entity in a UserController class. The UserController should have the following endpoints:
// - GET /users: returns a list of all users
// - GET /users/{id}: returns a user by id  
// - POST /users: creates a new user
// - PUT /users/{id}: updates an existing user by id
// - DELETE /users/{id}: deletes a user by id

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.http.ResponseEntity
import org.apache.catalina.connector.Response
import jakarta.validation.Valid

@RestController
@RequestMapping("/users")
class UserResource() {

    // logger
    private val logger = org.slf4j.LoggerFactory.getLogger(UserResource::class.java)

    // Mock in-memory database for users
    private val users = mutableListOf<User>()
    private var nextId: Long = 1

    // GET /users: returns a list of all users
    @GetMapping
    fun getAllUsers(): List<User> {
        logger.info("Fetching all users")
        return users
    }
    
    // GET /users/{id}: returns a user by id
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): User? {
        logger.info("Fetching user with id: $id")   
        return users.find { it.id == id }
    }

    // POST /users: creates a new user
    @PostMapping
    fun createUser(@Valid @RequestBody userDto: UserCreateDto): ResponseEntity<Any> {
        // validate existing email
        if (users.any { it.email == userDto.email }) {
            logger.warn("Email ${userDto.email} already exists")
            return ResponseEntity.badRequest().body("Email " + userDto.email + " already exists")
        }

        val newUser = User(
            id = nextId,
            name = userDto.name,
            email = userDto.email,
            age = userDto.age
        )
        logger.info("Creating user with id: ${newUser.id}")
        users.add(newUser)
        nextId += 1
        return ResponseEntity.ok(newUser)
    }

    // PUT /users/{id}: updates an existing user by id
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @Valid @RequestBody updatedUser: UserCreateDto): ResponseEntity<User> {
        val userIndex = users.indexOfFirst { it.id == id }
        return if (userIndex != -1) {
            val user = users[userIndex]
            val newUser = User(
                id = user.id,
                name = updatedUser.name,
                email = updatedUser.email,
                age = updatedUser.age
            )
            users[userIndex] = newUser
            logger.info("Updated user with id: $id")
            ResponseEntity.ok(newUser)
        } else {
            logger.warn("User with id: $id not found")
            ResponseEntity.notFound().build()
        }
    }

    // DELETE /users/{id}: deletes a user by id
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        val removed = users.removeIf { it.id == id }
        if (removed) {
            logger.info("Deleted user with id: $id")
            return ResponseEntity.noContent().build()
        } else {
            logger.warn("User with id: $id not found")
            return ResponseEntity.notFound().build()
        }
    }

}

