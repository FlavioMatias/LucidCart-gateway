import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// =====================================
// BASE URL GLOBAL
// =====================================
export const BASE_URL = 'http://localhost:8080/api/v1';
const ORDERS_URL = `${BASE_URL}/orders`;
const ITEMS_URL = `${ORDERS_URL}/items`;

// =====================================
// MODELS (todos os DTO equivalentes aos Kotlin)
// =====================================

// -------- ENUM --------
export type OrderStatusDTO =
  | 'CREATED'
  | 'PROCESSING'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELED';

// -------- ITEM --------
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

// -------- ADDRESS --------
export interface AddressDTO {
  id: number | null;
  userId: number;
  fullAddress: string;
  latitude: number;
  longitude: number;
}

// -------- ORDER --------
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

// -------- RESPONSES --------
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

  // ---------- ITEMS ----------
  addItem(req: ItemOrderRequestDTO): Observable<AddItemResponseDTO> {
    return this.http.post<AddItemResponseDTO>(ITEMS_URL, req);
  }

  updateItem(id: number, req: ItemOrderRequestDTO): Observable<UpdateItemResponseDTO> {
    return this.http.put<UpdateItemResponseDTO>(`${ITEMS_URL}/${id}`, req);
  }

  deleteItem(id: number): Observable<void> {
    return this.http.delete<void>(`${ITEMS_URL}/${id}`);
  }

  // ---------- ORDERS ----------
  findOrder(id: number): Observable<FindOrderResponseDTO> {
    return this.http.get<FindOrderResponseDTO>(`${ORDERS_URL}/${id}`);
  }

  listOrders(params?: {
    status?: OrderStatusDTO;
    userId?: number;
    orderId?: number;
  }): Observable<ListOrdersResponseDTO> {
    return this.http.get<ListOrdersResponseDTO>(ORDERS_URL, { params: params as any });
  }

  deleteOrder(id: number): Observable<void> {
    return this.http.delete<void>(`${ORDERS_URL}/${id}`);
  }

  // ---------- CART ----------
  findCart(): Observable<FindCartResponseDTO> {
    return this.http.get<FindCartResponseDTO>(`${ORDERS_URL}/cart`);
  }

  sendOrder(order: OrderDTO): Observable<SendOrderResponseDTO> {
    return this.http.post<SendOrderResponseDTO>(`${ORDERS_URL}?id=${order.id}`, {});
  }
}

