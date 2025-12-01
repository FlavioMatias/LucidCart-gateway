import { Component, Input } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ProductDTO } from "../../../shared/services/products/products-service";
import { OrdersService } from "../../../shared/services/orders/order-service";

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [CommonModule],
  template: `
  <div 
    class="relative flex flex-col overflow-hidden bg-white rounded-xl shadow-md 
           cursor-pointer w-[22rem] h-[20vw] hover:shadow-lg transition-all duration-200"
  >

    <!-- imagem -->
    <div class="relative h-[70%] w-full overflow-hidden border-b border-gray-200">
      <img 
        [src]="product.image" 
        [alt]="product.name" 
        class="w-full h-full object-cover"
      />

      <div class="absolute top-0 right-0 bg-white rounded-bl-xl px-6 py-3 font-semibold shadow-md">
        <span class="text-[0.7rem]">R$</span> {{ product.price }}
      </div>
    </div>

    <div class="flex flex-col px-4 pt-3 pb-16">
      <span class="font-medium text-lg text-gray-900">
        {{ product.name }}
      </span>

      <div class="flex items-center gap-2 text-base text-gray-700 mt-1">
        <span class="text-yellow-500">★</span>
        <span>5.0</span>
      </div>
    </div>

    <button
      (click)="addToCart()"
      class="
        absolute bottom-0 left-0 w-full py-3 bg-green-600 text-white font-semibold 
        hover:bg-green-700 hover:shadow-md transition-all duration-200
        cursor-pointer rounded-b-xl z-10
      "
    >
      Add to Cart
    </button>

  </div>

  <!-- TOAST -->
  <div 
    *ngIf="toastMessage"
    class="fixed bottom-6 right-6 px-5 py-3 rounded-lg text-white shadow-lg transition-all duration-300"
    [ngClass]="toastType === 'success' ? 'bg-green-600' : 'bg-red-600'"
  >
    {{ toastMessage }}
  </div>
  `
})
export class ProductCardComponent {
  @Input() product!: ProductDTO;

  toastMessage: string | null = null;
  toastType: 'success' | 'error' = 'success';

  constructor(private orders: OrdersService) {}

  private showToast(message: string, type: 'success' | 'error' = 'success') {
    this.toastMessage = message;
    this.toastType = type;

    setTimeout(() => {
      this.toastMessage = null;
    }, 2500);
  }

  addToCart() {
    const body = {
      productId: this.product.id,
      quantity: 1,
      unitPrice: this.product.price
    };

    this.orders.addItem(body).subscribe({
      next: () => this.showToast("Item adicionado ao carrinho!", "success"),
      error: err => {
        console.error("Erro ao adicionar item:", err);
        this.showToast("Erro ao adicionar item!", "error");
      }
    });
  }
}
