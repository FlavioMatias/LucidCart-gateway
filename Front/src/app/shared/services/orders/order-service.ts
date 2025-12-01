import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BASE_URL } from '../../../app.config';

// =====================================
// MODELS (todos os DTO equivalentes aos Kotlin)
// =====================================

export type OrderStatusDTO =
  | 'CREATED'
  | 'PROCESSING'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELED';

export interface ItemOrderDTO {
  id: number | null;
  productId: number;
  orderId: number;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface ItemOrderRequestDTO {
  productId: number;
  quantity: number;
  unitPrice: number;
}

export interface AddressDTO {
  id: number | null;
  userId: number;
  fullAddress: string;
  latitude: number;
  longitude: number;
}

export interface OrderDTO {
  id: number;
  status: OrderStatusDTO;
  traceCode: string;
  address: AddressDTO;
  items: ItemOrderDTO[];
}

export interface OrderListDTO {
  orders: OrderDTO[];
}

export interface AddItemResponseDTO {
  item: ItemOrderDTO;
}

export interface UpdateItemResponseDTO {
  item: ItemOrderDTO;
}

export interface SendOrderResponseDTO {
  order: OrderDTO;
}

export interface FindOrderResponseDTO {
  order: OrderDTO;
}

export interface ListOrdersResponseDTO {
  orders: OrderDTO[];
}

export interface FindCartResponseDTO {
  order: OrderDTO;
}

// =====================================
// SERVICE
// =====================================

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  constructor(private http: HttpClient) {}

  // Avaliação dinâmica das URLs
  private get ORDERS_URL() { return `${BASE_URL}/orders`; }
  private get ITEMS_URL() { return `${this.ORDERS_URL}/items`; }

  // ---------- ITEMS ----------
  addItem(req: ItemOrderRequestDTO): Observable<AddItemResponseDTO> {
    return this.http.post<AddItemResponseDTO>(this.ITEMS_URL, req);
  }

  updateItem(id: number, req: ItemOrderRequestDTO): Observable<UpdateItemResponseDTO> {
    return this.http.put<UpdateItemResponseDTO>(`${this.ITEMS_URL}/${id}`, req);
  }

  deleteItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.ITEMS_URL}/${id}`);
  }

  // ---------- ORDERS ----------
  findOrder(id: number): Observable<FindOrderResponseDTO> {
    return this.http.get<FindOrderResponseDTO>(`${this.ORDERS_URL}/${id}`);
  }

  listOrders(params?: {
    status?: OrderStatusDTO;
    userId?: number;
    orderId?: number;
  }): Observable<ListOrdersResponseDTO> {
    return this.http.get<ListOrdersResponseDTO>(this.ORDERS_URL, { params: params as any });
  }

  deleteOrder(id: number): Observable<void> {
    return this.http.delete<void>(`${this.ORDERS_URL}/${id}`);
  }

  // ---------- CART ----------
  findCart(): Observable<FindCartResponseDTO> {
    return this.http.get<FindCartResponseDTO>(`${this.ORDERS_URL}/cart`);
  }

  sendOrder(order: OrderDTO): Observable<SendOrderResponseDTO> {
    return this.http.post<SendOrderResponseDTO>(`${this.ORDERS_URL}?id=${order.id}`, {});
  }
}
