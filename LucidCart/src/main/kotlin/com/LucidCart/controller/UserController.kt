package com.LucidCart.controller

import com.LucidCart.service.UserService
import com.LucidCart.gateway.user.SignUpDTO
import com.LucidCart.gateway.user.SignInDTO
import com.LucidCart.gateway.user.SignInResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*

// ======== RESPONSE DTOs ======== //
data class SignupResponse(val id: Long?, val email: String?)
data class UploadPhotoResponse(val photo_url: String)

// ======================= AUTH ============================= //
@RestController
@RequestMapping("api/v1/auth")
class AuthController(private val userService: UserService) {

    @PostMapping("/signup")
    fun signup(@RequestBody req: SignUpDTO): EntityModel<SignupResponse> {

        val createdUser = userService.signup(req.email, req.password)
        val response = SignupResponse(createdUser.id, createdUser.email)

        val model = EntityModel.of(response)
        // Links HATEOAS
        model.add(
            linkTo(AuthController::class.java).slash("signup").withSelfRel(),
            linkTo(AuthController::class.java).slash("signin").withRel("signin")
        )
        return model
    }

    @PostMapping("/signin")
    fun signin(@RequestBody req: SignInDTO): EntityModel<SignInResponse> {

        val token = userService.login(req.email, req.password)
        val response = SignInResponse(token)

        val model = EntityModel.of(response)
        model.add(
            linkTo(AuthController::class.java).slash("signin").withSelfRel(),
            linkTo(AuthController::class.java).slash("signup").withRel("signup"),
            linkTo(ProfileController::class.java).slash("photo").withRel("upload_profile_photo")
        )

        return model
    }
}

// ======================= PROFILE ============================= //
@RestController
@RequestMapping("api/v1/profile")
class ProfileController(private val userService: UserService) {

    @PostMapping("/photo", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadProfilePhoto(
        @RequestHeader("Authorization") token: String,
        @RequestPart("photo") file: MultipartFile
    ): EntityModel<UploadPhotoResponse> {

        val photoUrl = userService.uploadProfilePhoto(
            userId = 0, // ajuste depois com JWT
            token = token.removePrefix("Bearer "),
            fileBytes = file.bytes,
            filename = file.originalFilename ?: "profile.png"
        )

        val response = UploadPhotoResponse(photoUrl)
        val model = EntityModel.of(response)

        model.add(
            linkTo(ProfileController::class.java).slash("photo").withSelfRel(),
            linkTo(AuthController::class.java).slash("signin").withRel("signin"),
            linkTo(AuthController::class.java).slash("signup").withRel("signup")
        )

        return model
    }
}
