import { Component, Input } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ProductSimple } from "../../../shared/models/product/product-simple.model";
import { OrdersService } from "../../../shared/services/orders/order-service";

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="relative flex flex-col overflow-hidden bg-white rounded-lg shadow-md cursor-pointer w-[22rem] h-[20vw]">

    <!-- imagem -->
    <div class="relative h-[70%] w-full overflow-hidden top-0 border-b border-gray-200">
      <img 
        [src]="product.imageUrl" 
        [alt]="product.name" 
        class="w-full h-full object-cover"
      />

      <!-- price tag -->
      <div class="absolute top-0 right-0 bg-white rounded-bl-lg px-6 py-3 font-semibold shadow-md">
        <span class="text-[0.7rem]">R$</span> {{ product.price }}
      </div>
    </div>

    <!-- info -->
    <div class="flex flex-col px-4 pt-3 pb-14"> <!-- padding inferior para o botão -->
      <span class="font-medium text-lg text-gray-900">
        {{ product.name }}
      </span>

      <div class="flex items-center gap-2 text-base text-gray-700 mt-1">
        <span class="text-yellow-500">★</span>
        <span>{{ product.rating }}</span>
      </div>
    </div>

    <!-- botão add to cart -->
    <button
      (click)="addToCart()"
      class="absolute bottom-0 left-0 w-full py-3 bg-green-600 text-white font-semibold 
             hover:bg-green-700 transition-all duration-200"
    >
      Add to Cart
    </button>

  </div>
  `
})
export class ProductCardComponent {
  @Input() product!: ProductSimple;

  constructor(private orders: OrdersService) {}

  addToCart() {
    const body = {
      productId: this.product.id,
      quantity: 1,
      unitPrice: this.product.price
    };

    this.orders.addItem(body).subscribe({
      next: () => console.log("Item adicionado ao carrinho!"),
      error: err => console.error("Erro ao adicionar item:", err)
    });
  }
}
