package com.LucidCart.controller

import com.LucidCart.service.UserService
import com.LucidCart.gateway.user.*
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

// ======== RESPONSE DTOs ======== //
data class SignupResponse(val id: Long?, val email: String?)
data class UploadPhotoResponse(val photo_url: String)

@Tag(name = "Auth", description = "Endpoints de autenticação de usuários")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val userService: UserService) {

    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário com email e senha.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos")
        ]
    )
    @PostMapping("/signup")
    fun signup(@RequestBody req: SignUpDTO): EntityModel<SignupResponse> {
        val createdUser = userService.signup(req.email, req.password)
        val response = SignupResponse(createdUser.id, createdUser.email)
        val model = EntityModel.of(response)
        model.add(
            linkTo(AuthController::class.java).slash("signup").withSelfRel(),
            linkTo(AuthController::class.java).slash("signin").withRel("signin")
        )
        return model
    }

    @Operation(summary = "Login de usuário", description = "Autentica um usuário e retorna token JWT.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            ApiResponse(responseCode = "401", description = "Credenciais inválidas")
        ]
    )
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

@Tag(name = "Profile", description = "Endpoints de perfil de usuário")
@RestController
@RequestMapping("/api/v1/profile")
class ProfileController(private val userService: UserService) {

    @Operation(summary = "Upload de foto de perfil", description = "Envia uma imagem para ser usada como foto de perfil do usuário.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Foto enviada com sucesso"),
            ApiResponse(responseCode = "400", description = "Arquivo inválido ou ausente"),
            ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
        ]
    )
    @PostMapping("/photo", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadProfilePhoto(
        @Parameter(description = "Token JWT do usuário", required = true)
        @RequestHeader("Authorization") token: String,
        @Parameter(description = "Arquivo de imagem do perfil", required = true)
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
