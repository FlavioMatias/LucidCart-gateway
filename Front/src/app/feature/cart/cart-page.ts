import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { OrdersService, ItemOrderResponseDTO, OrderResponseDTO } from "../../shared/services/orders/order-service";
import { ProductDTO, ProductService } from "../../shared/services/products/products-service";

@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
<div class="w-full flex justify-center mt-[10em] px-6">

  <div class="flex gap-10 w-full max-w-6xl">

    <!-- LEFT -->
    <div class="flex-1 bg-white shadow-md rounded-xl p-6">

      <h2 class="text-2xl font-semibold mb-6">Your cart</h2>

      <div *ngIf="loading" class="text-gray-500">Loading...</div>

      <div *ngIf="!loading && safeItems.length === 0" class="text-gray-600 text-center p-10">
        Your cart is empty.
      </div>

      <div *ngFor="let item of safeItems"
           class="flex items-center gap-5 border border-gray-200 rounded-xl p-4 mb-4">

        <img 
          [src]="getProduct(item.productId)?.image"
          alt="product" 
          class="w-28 h-28 rounded-md object-cover"
        >

        <div class="flex-1">
          <p class="text-lg font-medium">
            {{ getProduct(item.productId)?.name }}
          </p>

          <p class="text-green-600 font-semibold mt-1">
            $ {{ item.unitPrice.toFixed(2) }}
          </p>
        </div>

        <select 
          class="border rounded-lg px-2 py-1"
          [(ngModel)]="item.quantity"
          (ngModelChange)="updateQuantity(item, $event)">
          <option *ngFor="let n of quantities" [value]="n">
            Qty: {{n}}
          </option>
        </select>

        <button 
          class="text-red-500 hover:text-red-700 text-xl"
          (click)="removeItem(item)">
          🗑️
        </button>

      </div>

      <div class="flex justify-end mt-6" *ngIf="safeItems.length > 0">
        <button 
          class="px-6 py-2 border border-pink-500 text-pink-600 rounded-xl hover:bg-pink-50"
          (click)="cancelOrder()">
          Cancel
        </button>
      </div>

    </div>

    <!-- RIGHT -->
    <div class="w-[300px] bg-white shadow-md rounded-xl p-6 h-fit">

      <h3 class="text-xl font-semibold mb-6">Purchase summary</h3>

      <div class="flex justify-between text-gray-700 mb-4">
        <span>Total:</span>
        <span>$ {{ getTotal().toFixed(2) }}</span>
      </div>

      <button 
        class="w-full py-2 mt-6 border border-gray-700 rounded-lg hover:bg-gray-100 transition"
        [disabled]="safeItems.length === 0 || sending"
        (click)="buyNow()">
        {{ sending ? 'Sending...' : 'Buy now' }}
      </button>

    </div>
  </div>
</div>
`
})
export class CartPageComponent implements OnInit {

  cart: OrderResponseDTO | null = null;
  loading = true;
  sending = false;

  productsMap: Record<number, ProductDTO> = {};

  quantities = [1,2,3,4,5,6,7,8,9];

  constructor(
    private ordersService: OrdersService,
    private productService: ProductService,
    private router: Router
  ) {}

  ngOnInit() {
    this.preloadProducts();
    this.loadCart();
  }

  // ---------- CARREGA TODOS PRODUTOS UMA VEZ ----------
  preloadProducts() {
    this.productService.listProducts().subscribe({
      next: res => {
        for (const p of res) this.productsMap[p.id] = p;
      },
      error: err => console.error("Error loading products", err)
    });
  }

  getProduct(id: number): ProductDTO | undefined {
    return this.productsMap[id];
  }

  get safeItems(): ItemOrderResponseDTO[] {
    return this.cart?.items ?? [];
  }

  loadCart() {
    this.loading = true;
    this.ordersService.findCart().subscribe({
      next: res => {
        this.cart = res;
        this.loading = false;
      },
      error: err => {
        console.error("Error loading cart", err);
        this.loading = false;
      }
    });
  }

  updateQuantity(item: ItemOrderResponseDTO, quantity: number) {
    this.ordersService.updateItem(item.id!, {
      productId: item.productId,
      quantity,
      unitPrice: item.unitPrice
    }).subscribe(() => this.loadCart());
  }

  removeItem(item: ItemOrderResponseDTO) {
    this.ordersService.deleteItem(item.id!).subscribe(() => {
      if (this.cart) {
        this.cart.items = this.cart.items.filter(i => i.id !== item.id);
      }
    });
  }

  cancelOrder() {
    if (!this.cart?.id) return;
    this.ordersService.deleteOrder(this.cart.id).subscribe({
      next: () => this.router.navigate(['/orders']),
      error: err => {
        console.error("Error canceling order", err);
        alert("Error canceling current order!");
      }
    });
  }

  getTotal(): number {
    return this.safeItems.reduce(
      (acc, i) => acc + (i.unitPrice * i.quantity), 
      0
    );
  }

  buyNow() {
    if (!this.cart) return;

    this.sending = true;
    this.ordersService.sendOrder(this.cart.id).subscribe({
      next: () => {
        this.sending = false;
        this.router.navigate(['/orders']);
      },
      error: () => {
        this.sending = false;
        alert('Error sending order!');
      }
    });
  }
}
