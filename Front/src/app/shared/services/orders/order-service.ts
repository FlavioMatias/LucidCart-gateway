import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BASE_URL } from '../../../app.config';
import { AddressResponseDTO } from '../address/address-service';
// =====================================
// MODELS 
// =====================================

export type OrderStatusDTO =
  | 'CREATED'
  | 'PROCESSING'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELED';

export interface ItemOrderResponseDTO {
  id: number;
  productId: number;
  orderId: number;
  quantity: number;
  unitPrice: number;
  subtotal: number;
  createdAt: string | null;
  updatedAt: string | null;
}

export interface ItemOrderRequestDTO {
  productId: number;
  quantity: number;
  unitPrice: number;
}

export interface OrderResponseDTO {
  id: number;
  status: OrderStatusDTO;
  traceCode: string;
  address: AddressResponseDTO;
  createdAt: string | null;
  updatedAt: string | null;
  items: ItemOrderResponseDTO[];
}

// =====================================
// SERVICE
// =====================================

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  constructor(private http: HttpClient) {}

  private get ORDERS_URL() {
    return `${BASE_URL}/orders`;
  }

  private get ITEMS_URL() {
    return `${this.ORDERS_URL}/items`;
  }

  // ----------------- ITEMS -----------------

  addItem(req: ItemOrderRequestDTO): Observable<ItemOrderResponseDTO> {
    return this.http.post<ItemOrderResponseDTO>(this.ITEMS_URL, req);
  }

  updateItem(id: number, req: ItemOrderRequestDTO): Observable<ItemOrderResponseDTO> {
    return this.http.put<ItemOrderResponseDTO>(`${this.ITEMS_URL}/${id}`, req);
  }

  deleteItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.ITEMS_URL}/${id}`);
  }

  // ----------------- ORDERS -----------------

  findOrder(id: number): Observable<OrderResponseDTO> {
    return this.http.get<OrderResponseDTO>(`${this.ORDERS_URL}/${id}`);
  }

  listOrders(params?: {
    status?: OrderStatusDTO;
    orderId?: number;
  }): Observable<OrderResponseDTO[]> {
    return this.http.get<OrderResponseDTO[]>(this.ORDERS_URL, {
      params: params as any
    });
  }

  deleteOrder(id: number): Observable<void> {
    return this.http.delete<void>(`${this.ORDERS_URL}/${id}`);
  }

  // ----------------- CART -----------------

  findCart(): Observable<OrderResponseDTO> {
    return this.http.get<OrderResponseDTO>(`${this.ORDERS_URL}/cart`);
  }

  sendOrder(orderId: number): Observable<OrderResponseDTO> {
    return this.http.post<OrderResponseDTO>(`${this.ORDERS_URL}/${orderId}/send`, {});
  }
}