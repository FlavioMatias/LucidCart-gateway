package com.LucidCart.controller

import com.LucidCart.gateway.user.*
import com.LucidCart.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

// ======== RESPONSE DTOs ======== //
data class SignupResponse(val id: Long?, val email: String?)
data class UploadPhotoResponse(val photo_url: String)

@Tag(name = "Auth", description = "User authentication endpoints")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val userService: UserService) {

    @Operation(summary = "Register new user", description = "Creates a new user with email and password.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid data")
        ]
    )
    @PostMapping("/signup")
    fun signup(@RequestBody req: SignUpDTO): SignupResponse {
        val createdUser = userService.signup(req.email, req.password)
        return SignupResponse(createdUser.id, createdUser.email)
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login successful"),
            ApiResponse(responseCode = "401", description = "Invalid credentials")
        ]
    )
    @PostMapping("/signin")
    fun signin(@RequestBody req: SignInDTO): SignInResponse {
        val token = userService.login(req.email, req.password)
        return SignInResponse(token)
    }
}

@Tag(name = "Profile", description = "User profile endpoints")
@RestController
@RequestMapping("/api/v1/profile")
class ProfileController(private val userService: UserService) {

    @Operation(summary = "Upload profile photo", description = "Uploads an image to be used as the user's profile photo.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Photo uploaded successfully"),
            ApiResponse(responseCode = "400", description = "Invalid or missing file"),
            ApiResponse(responseCode = "401", description = "Invalid or missing token")
        ]
    )
    @PostMapping("/photo", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadProfilePhoto(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @Parameter(description = "Profile image file", required = true)
        @RequestPart("photo") file: MultipartFile
    ): UploadPhotoResponse {

        val photoUrl = userService.uploadProfilePhoto(
            userId = 0,
            token = token.removePrefix("Bearer "),
            fileBytes = file.bytes,
            filename = file.originalFilename ?: "profile.png"
        )

        return UploadPhotoResponse(photoUrl)
    }
}
