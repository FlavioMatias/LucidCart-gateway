import { Component, OnInit } from '@angular/core';
import { SrcBarComponent } from '../shared/components/srcbar.component';
import { AuthService } from '../shared/services/user/user-service';
import { AddressService, FindAddressResponseDTO } from '../shared/services/address/address-service';
import { OrdersService } from '../shared/services/orders/order-service';

@Component({
  selector: 'app-header',
  imports: [SrcBarComponent],
  template: `
  <header 
    class="fixed left-10 right-10 backdrop-blur-md bg-white/60 text-gray-950 py-3 px-6 z-50 rounded-b-[12px] 
           flex items-center justify-between border border-white/20"
    style="box-shadow: 0 1px 7px rgba(0,0,0,0.1);">

      <!-- Logo + SrcBar -->

      <div class="flex items-center gap-2 space-x-4">
        <a href="/" class="cursor-pointer">
          <img src="assets/logo.svg" alt="Lucid Cart Logo" class="h-12 w-12"/>
        </a>

        @if (authService.isAuthenticated()) {
          <div class="ml-[5px] cursor-pointer">
            <p class="text-sm text-gray-600">Deliver to</p>
            <h3 class="text-gray-900">{{ formatedAddress() }}</h3>
          </div>
        }

        <app-srcbar
          placeholder="You looking for something?"
          width="70rem"
          height="30px"
          (valueChange)="onSearch($event)"
        ></app-srcbar>
      </div>

      <!-- Links -->
      <nav>
        <ul class="flex items-center space-x-8">

          <!-- Orders -->
          <li>
            <a [href]="authService.isAuthenticated() ? '/orders/' : '#'"
               class="relative group flex items-center text-gray-900 transition-colors duration-300">
              <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="#000000" viewBox="0 0 256 256">
               <path d="M225.6,62.64l-88-48.17a19.91,19.91,0,0,0-19.2,0l-88,48.17A20,20,0,0,0,20,80.19v95.62a20,20,0,0,0,10.4,17.55l88,48.17a19.89,19.89,0,0,0,19.2,0l88-48.17A20,20,0,0,0,236,175.81V80.19A20,20,0,0,0,225.6,62.64ZM128,36.57,200,76,178.57,87.73l-72-39.42Zm0,78.83L56,76,81.56,62l72,39.41ZM44,96.79l72,39.4v76.67L44,173.44Zm96,116.07V136.19l24-13.13V152a12,12,0,0,0,24,0V109.92l24-13.13v76.65Z"></path>
              </svg>
              <span class="ml-[5px] font-medium">Orders</span>
            </a>
          </li>

          <!-- Cart -->
          <li class="relative cursor-pointer">
            <a [href]="authService.isAuthenticated() ? '/cart/' : '#'" class="relative block">
              
              <!-- Ícone -->
              <img src="assets/cart.svg" class="h-10 w-10" alt="Cart">

              <!-- Badge -->
              @if (cartCount > 0) {
                <span class="
                  absolute -top-1 -right-1 
                  bg-red-600 text-white text-[10px] font-bold 
                  rounded-full h-5 w-5 flex items-center justify-center shadow
                ">
                  {{ cartCount }}
                </span>
              }
            </a>
          </li>

          <!-- Login / Logout -->
          <li class="flex items-center">
            @if (authService.isAuthenticated()) {
              <button 
                (click)="logout()" 
                class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition">
                Logout
              </button>
            } @else {
              <a href="/login" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                Login
              </a>
            }
          </li>

        </ul>
      </nav>
  </header>
  `,
})
export class Header implements OnInit {
  address: string = '';
  cartCount: number = 0;

  constructor(
    public authService: AuthService,
    private addressService: AddressService,
    private ordersService: OrdersService
  ) {}

  ngOnInit() {
    if (this.authService.isAuthenticated()) {
      this.loadAddress();
      this.loadCartCount();
    }
  }

  loadAddress() {
    this.addressService.findAddress().subscribe({
      next: (res: FindAddressResponseDTO) => {
        this.address = res.address.fullAddress;
      },
      error: () => {
        this.address = '';
      }
    });
  }

  loadCartCount() {
    this.cartCount = 0;
    return
    this.ordersService.findCart().subscribe({
      next: (res) => {
        this.cartCount = res.order?.items?.length ?? 0;
      },
      error: () => {
        this.cartCount = 0;
      }
    });
  }

  formatedAddress(): string {
    if (!this.address) return '';
    const parts = this.address.split(',').map(p => p.trim());
    const first = parts[0];
    const penultimate = parts[parts.length - 2] || '';
    return `${first}... ${penultimate}`;
  }

  logout() {
    this.authService.logout();
  }

  onSearch(value: string) {
    console.log('text search:', value);
  }
}
