package com.example.demo.user

import jakarta.validation.constraints.Email

data class UserCreateDto(
    val name: String,
    @Email
    val email: String,
    val age: Int
) {
    constructor(user: UserCreateDto) : this(
        name = user.name,
        email = user.email,
        age = user.age
    )
}
