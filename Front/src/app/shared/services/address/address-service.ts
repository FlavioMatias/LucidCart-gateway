import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BASE_URL } from '../../../app.config';

export interface AddressResponseDTO {
  id: number | null;
  userId: number;
  fullAddress: string;
  latitude: number;
  longitude: number;
  createdAt: string | null;
  updatedAt: string | null;
}

export interface AddressRequestDTO {
  fullAddress: string;
  latitude: number;
  longitude: number;
}


// =====================================
// SERVICE
// =====================================
@Injectable({
  providedIn: 'root'
})
export class AddressService {

  constructor(private http: HttpClient) {}

  private get ADDRESS_URL() {
    return `${BASE_URL}/address`;
  }

  createAddress(req: AddressRequestDTO): Observable<AddressResponseDTO> {
    return this.http.post<AddressResponseDTO>(this.ADDRESS_URL, req);
  }

  updateAddress(req: AddressRequestDTO): Observable<AddressResponseDTO> {
    return this.http.put<AddressResponseDTO>(this.ADDRESS_URL, req);
  }

  deleteAddress(): Observable<void> {
    return this.http.delete<void>(this.ADDRESS_URL);
  }

  findAddress(): Observable<AddressResponseDTO> {
    return this.http.get<AddressResponseDTO>(this.ADDRESS_URL);
  }
}