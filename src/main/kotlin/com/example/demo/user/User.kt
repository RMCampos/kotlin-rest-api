package com.example.demo.user

import jakarta.validation.constraints.Email

data class User(
    val id: Long,
    val name: String,
    @Email
    val email: String,
    val age: Int
) {
    constructor(user: User) : this(
        id = user.id,
        name = user.name,
        email = user.email,
        age = user.age
    )
}
