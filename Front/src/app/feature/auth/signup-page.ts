import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService, SignUpDTO, SignInDTO } from '../../shared/services/user/user-service';
import { AddressService, AddressRequestDTO } from '../../shared/services/address/address-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
<div class="flex justify-center items-center min-h-screen bg-gray-50 px-4">
  <div class="bg-white shadow-lg rounded-lg w-full max-w-4xl p-8 flex flex-col md:flex-row gap-6">

    <!-- LEFT - Image + Marketing -->
    <div class="flex-1 flex flex-col items-center justify-center gap-4">
      <h1 class="text-3xl font-bold">SignUp</h1>
      <p class="text-center text-green-600 font-bold">Your data is <span class="underline">safe</span> with us.</p>
      <img src="assets/ec-marketing.png" alt="Marketing" class="max-w-xs mt-4"/>
    </div>

    <!-- RIGHT - Form -->
    <div class="flex-1 flex flex-col gap-4">
      <input 
        type="email" 
        [(ngModel)]="email" 
        placeholder="your email here" 
        class="border rounded-lg p-3 w-full focus:outline-none focus:ring-2 focus:ring-green-500"
      />
      <input 
        type="password" 
        [(ngModel)]="password" 
        placeholder="your password here" 
        class="border rounded-lg p-3 w-full focus:outline-none focus:ring-2 focus:ring-green-500"
      />
      <input 
        type="password" 
        [(ngModel)]="confirmPassword" 
        placeholder="confirm your password" 
        class="border rounded-lg p-3 w-full focus:outline-none focus:ring-2 focus:ring-green-500"
      />
      <button 
        (click)="signup()" 
        class="bg-green-600 text-white font-semibold p-3 rounded-lg hover:bg-green-700 transition"
      >
        SignUp
      </button>
      <p *ngIf="errorMsg" class="text-red-500 font-semibold text-center">{{ errorMsg }}</p>
    </div>

  </div>
</div>
  `
})
export class SignupPageComponent {
  email = '';
  password = '';
  confirmPassword = '';
  errorMsg = '';

  constructor(
    private authService: AuthService,
    private addressService: AddressService,
    private router: Router
  ) {}

  signup() {
    if (!this.email || !this.password || !this.confirmPassword) {
      this.errorMsg = 'Todos os campos são obrigatórios!';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMsg = 'As senhas não conferem!';
      return;
    }

    const signupReq: SignUpDTO = {
      Email: this.email,
      Password: this.password
    };

    // 1. Cadastrar usuário
    this.authService.signup(signupReq).subscribe({
      next: () => {
        // 2. Logar automaticamente
        const signinReq: SignInDTO = { Email: this.email, Password: this.password };
        this.authService.signin(signinReq).subscribe({
          next: () => {
            // 3. Criar endereço fixo
            const addressReq: AddressRequestDTO = {
              fullAddress: 'Av. Sen. Salgado Filho, 1559 - Tirol, Natal - RN, 59015-000',
              latitude: 77.7,
              longitude: 77.7
            };
            this.addressService.createAddress(addressReq).subscribe({
              next: () => {
                this.router.navigate(['/home']); // Redireciona após tudo pronto
              },
              error: err => {
                console.error('Erro ao criar endereço:', err);
                this.router.navigate(['/home']); // mesmo assim vai pra dashboard
              }
            });
          },
          error: err => {
            console.error('Erro ao logar automaticamente:', err);
            this.errorMsg = 'Erro no login automático após cadastro.';
          }
        });
      },
      error: err => {
        console.error('Erro no cadastro:', err);
        this.errorMsg = 'Falha no cadastro. Email pode já estar em uso.';
      }
    });
  }
}
