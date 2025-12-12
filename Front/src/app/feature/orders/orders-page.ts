import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { OrdersService, OrderResponseDTO, OrderStatusDTO } from "../../shared/services/orders/order-service";

@Component({
  selector: 'app-orders-page',
  standalone: true,
  imports: [CommonModule],
  template: `
<div class="w-full flex justify-center mt-[10em] px-6">
  <div class="flex flex-col gap-4 w-full max-w-4xl">

    <div *ngIf="loading" class="text-gray-500 text-center">Carregando pedidos...</div>
    <div *ngIf="!loading && orders.length === 0" class="text-gray-600 text-center p-10">
      Nenhum pedido encontrado.
    </div>

    <div *ngFor="let order of orders"
         class="border rounded-lg p-4 flex justify-between items-center shadow-sm bg-white">

      <!-- LEFT - Detalhes do pedido -->
      <div class="flex flex-col gap-1">
        <p>
          <span class="font-semibold">enviado para:</span> {{ order.address.fullAddress }}
        </p>
        <p>
          <span class="font-semibold">status:</span> 
          <span [ngClass]="statusColor(order.status)"> {{ order.status }}</span>
        </p>
        <p>
          <span class="font-semibold">codigo de rastreio:</span> {{ order.traceCode }}
        </p>
      </div>

      <!-- RIGHT - Total e quantidade de itens -->
      <div class="flex flex-col items-end gap-1">
        <span class="text-green-600 font-bold text-lg">
          $ {{ getTotal(order).toFixed(2) }}
        </span>
        <span class="text-gray-600">{{ order.items.length }} itens</span>
      </div>

    </div>
  </div>
</div>
`
})
export class OrdersPageComponent implements OnInit {

  orders: OrderResponseDTO[] = [];
  loading = true;

  constructor(private ordersService: OrdersService) {}

  ngOnInit() {
    this.loadOrders();
  }

  loadOrders() {
    this.loading = true;
    this.ordersService.listOrders().subscribe({
      next: res => {
        this.orders = res;
        this.loading = false;
      },
      error: err => {
        console.error("Erro ao carregar pedidos", err);
        this.loading = false;
      }
    });
  }

  getTotal(order: OrderResponseDTO): number {
    return order.items.reduce((acc, i) => acc + (i.unitPrice * i.quantity), 0);
  }

  statusColor(status: OrderStatusDTO): string {
    switch (status) {
      case 'PROCESSING': return 'text-orange-500 font-semibold';
      case 'CANCELED': return 'text-red-500 font-semibold';
      case 'DELIVERED': return 'text-green-500 font-semibold';
      case 'SHIPPED': return 'text-blue-500 font-semibold';
      case 'CREATED': return 'text-gray-700 font-semibold';
      default: return 'text-gray-700';
    }
  }
}
