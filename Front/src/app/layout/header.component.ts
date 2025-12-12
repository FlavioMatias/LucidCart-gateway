import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { SrcBarComponent } from '../shared/components/srcbar.component';
import { AuthService } from '../shared/services/user/user-service';
import { AddressService, AddressResponseDTO} from '../shared/services/address/address-service';
import { OrdersService } from '../shared/services/orders/order-service';
import { ProductService, ProductDTO } from '../shared/services/products/products-service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  imports: [SrcBarComponent],
  template: `
  <header class="fixed left-10 right-10 backdrop-blur-md bg-white/60 text-gray-950 py-3 px-6 z-50 rounded-b-[12px] 
           flex items-center justify-between border border-white/20"
          style="box-shadow: 0 1px 7px rgba(0,0,0,0.1);">

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
          (focus)="onSearchFocus()"
          (blur)="onSearchBlur()"
        ></app-srcbar>
      </div>

      <nav>
        <ul class="flex items-center space-x-8">
          <li>
            <a [href]="authService.isAuthenticated() ? '/orders/' : '#'" class="relative group flex items-center text-gray-900 transition-colors duration-300">
              <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="#000" viewBox="0 0 256 256">
               <path d="M225.6,62.64l-88-48.17a19.91,19.91,0,..."></path>
              </svg>
              <span class="ml-[5px] font-medium">Orders</span>
            </a>
          </li>

          <li class="relative cursor-pointer">
            <a [href]="authService.isAuthenticated() ? '/cart/' : '#'" class="relative block">
              <img src="assets/cart.svg" class="h-10 w-10" alt="Cart">
              @if (cartCount > 0) {
                <span class="absolute -top-1 -right-1 bg-red-600 text-white text-[10px] font-bold rounded-full h-5 w-5 flex items-center justify-center shadow">
                  {{ cartCount }}
                </span>
              }
            </a>
          </li>

          <li class="flex items-center">
            @if (authService.isAuthenticated()) {
              <button (click)="logout()" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition">
                Logout
              </button>
            } @else {
              <a href="/login" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Login</a>
            }
          </li>
        </ul>
      </nav>
  </header>
  `,
})
export class Header implements OnInit, OnDestroy {
  address: string = '';
  cartCount: number = 0;

  @Output() searchActive = new EventEmitter<boolean>();
  @Output() searchQuery = new EventEmitter<string>();
  @Output() searchResults = new EventEmitter<ProductDTO[]>();

  private wsSub?: Subscription;
  private wsConnected = false;

  constructor(
    public authService: AuthService,
    private addressService: AddressService,
    private ordersService: OrdersService,
    private productService: ProductService
  ) {}

  ngOnInit() {
    if (this.authService.isAuthenticated()) {
      this.loadAddress();
      this.loadCartCount();
    }
  }

  ngOnDestroy() {
    this.disconnectWS();
  }

  loadAddress() {
    this.addressService.findAddress().subscribe({
      next: (res: AddressResponseDTO) => this.address = res.fullAddress,
      error: () => this.address = ''
    });
  }

  loadCartCount() {
    this.cartCount = 0;
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

  // 🔥 Busca global via WebSocket
  onSearch(value: string) {
    const trimmed = value.trim();
    this.searchQuery.emit(trimmed);

    if (!trimmed) {
      this.searchActive.emit(false);
      return;
    }

    this.searchActive.emit(true);

    if (this.wsConnected) {
      this.productService.searchLive(trimmed);
    }
  }

  // 🔥 Conectar WS no foco
  onSearchFocus() {
    if (!this.wsConnected) {
      console.log('[Header] Conectando WebSocket...');
      this.productService.connectWS();
      this.wsConnected = true;

      this.wsSub = this.productService.results$.subscribe(products => {
        console.log('[Header] Produtos recebidos via WS:', products);
        this.searchResults.emit(products);
      });
    }
  }

  // 🔥 Desconectar WS no blur
  onSearchBlur() {
    console.log('[Header] Desconectando WebSocket...');
    this.disconnectWS();
  }

  private disconnectWS() {
    if (this.wsConnected) {
      this.productService.closeWS();
      this.wsSub?.unsubscribe();
      this.wsConnected = false;
    }
  }
}
