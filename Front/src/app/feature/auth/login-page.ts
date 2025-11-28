import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService, SignInDTO } from '../../shared/services/user/user-service';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
<div class="flex justify-center items-center min-h-screen bg-gray-50 px-4">
  <div class="bg-white shadow-lg rounded-lg w-full max-w-4xl p-8 flex flex-col md:flex-row gap-6">
    
    <!-- LEFT - Image + Marketing -->
    <div class="flex-1 flex flex-col items-center justify-center gap-4">
      <h1 class="text-3xl font-bold">Login</h1>
      <p class="text-center text-green-600 font-bold">Log in and BUY BUY BUY!!!</p>
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

      <button 
        (click)="login()" 
        class="bg-green-600 text-white font-semibold p-3 rounded-lg hover:bg-green-700 transition"
      >
        Login
      </button>

      <p *ngIf="errorMsg" class="text-red-500 font-semibold text-center">{{ errorMsg }}</p>

      <!-- SIGNUP BUTTON -->
      <div class="flex justify-center mt-2">
        <a 
          routerLink="/signup"
          class="text-green-700 font-semibold hover:underline"
        >
          Don’t have an account? Create one
        </a>
      </div>

    </div>

  </div>
</div>
  `
})
export class LoginPageComponent {
  email = '';
  password = '';
  errorMsg = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    if (!this.email || !this.password) {
      this.errorMsg = 'Email e senha são obrigatórios!';
      return;
    }

    const req: SignInDTO = {
      Email: this.email,
      Password: this.password
    };

    this.authService.signin(req).subscribe({
      next: () => this.router.navigate(['/home']),
      error: err => {
        console.error(err);
        this.errorMsg = 'Falha no login. Verifique suas credenciais.';
      }
    });
  }
}
