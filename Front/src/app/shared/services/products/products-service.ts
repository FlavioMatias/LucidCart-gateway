import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { BASE_URL, HOST_NOT_PORT } from '../../../app.config';

// ================================
// DTOs
// ================================

export interface ProductDTO {
  id: number;
  name: string;
  description: string;
  price: number;
  image: string;
  category: string;
  stock: number;
}

// ================================
// SERVICE
// ================================
@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private ws!: WebSocket;
  private resultsSubject = new Subject<ProductDTO[]>();
  results$ = this.resultsSubject.asObservable();
  private host_ws = HOST_NOT_PORT;
  constructor(private http: HttpClient) {}

  private get PRODUCTS_URL() {
    return `${BASE_URL}/products`;
  }

  // ---- GET ONE ----
  getProduct(id: number): Observable<ProductDTO> {
    return this.http.get<ProductDTO>(`${this.PRODUCTS_URL}/${id}`);
  }

  // ---- LIST ----
  listProducts(): Observable<ProductDTO[]> {
    return this.http.get<ProductDTO[]>(this.PRODUCTS_URL);
  }

  // -----------------------------
  // 🔥 WEBSOCKET REAL TIME SEARCH
  // -----------------------------
  connectWS() {
    console.log('Conectando ao WebSocket...');
    const token = sessionStorage.getItem('token');
    console.log('Token recuperado:', token);
    if (!token) {
      console.error('Token ausente, não é possível conectar ao WebSocket.');
      return;
    }

    const wsUrl = `ws://${this.host_ws}:8086/api/v1/ws/products?token=${token}`;
    this.ws = new WebSocket(wsUrl);

    const buffer: ProductDTO[] = [];

    this.ws.onopen = () => {
      console.log('[WS] Conectado ao gateway');
    };

    this.ws.onmessage = (event) => {
      console.log('[WS] Mensagem recebida raw:', event.data); // raw data

      if (event.data === '__END__') {
        // envia o array completo quando acabar
        this.resultsSubject.next([...buffer]);
        console.log('[WS] Fim da transmissão, buffer enviado:', buffer);
        buffer.length = 0; // limpa o buffer
        return;
      }

      try {
        const product: ProductDTO = JSON.parse(event.data);
        console.log('[WS] Produto parseado:', product); // produto parseado

        buffer.push(product);

        // opcional: envia live a cada produto recebido
        this.resultsSubject.next([...buffer]);
      } catch (e) {
        console.error('[WS] erro JSON:', e);
      }
    };

    this.ws.onclose = () => {
      console.warn('[WS] Desconectou, tentando reconectar em 2s...');
      setTimeout(() => this.connectWS(), 2000);
    };

    this.ws.onerror = (err) => {
      console.error('[WS] Erro de conexão:', err);
    };
  }

  // Envia texto da busca
  searchLive(query: string) {
    if (!this.ws || this.ws.readyState !== WebSocket.OPEN) return;
    this.ws.send(query);
  }

  closeWS() {
    if (this.ws) {
      this.ws.close();
    }
  }
}
