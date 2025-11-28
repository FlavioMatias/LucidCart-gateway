import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// =====================================
// BASE URL GLOBAL
// =====================================
export const BASE_URL = 'http://localhost:8080/api/v1';
const ADDRESS_URL = `${BASE_URL}/address`;

// =====================================
// MODELS
// =====================================

// ----- Address -----
export interface AddressDTO {
  id: number | null;
  userId: number;
  fullAddress: string;
  latitude: number;
  longitude: number;
}

export interface AddressRequestDTO {
  fullAddress: string;
  latitude: number;
  longitude: number;
}

// ----- Responses -----
export interface CreateAddressResponseDTO {
  address: AddressDTO;
}

export interface UpdateAddressResponseDTO {
  address: AddressDTO;
}

export interface FindAddressResponseDTO {
  address: AddressDTO;
}

// =====================================
// SERVICE
// =====================================

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  constructor(private http: HttpClient) {}

  createAddress(req: AddressRequestDTO): Observable<CreateAddressResponseDTO> {
    return this.http.post<CreateAddressResponseDTO>(ADDRESS_URL, req);
  }

  updateAddress(req: AddressRequestDTO): Observable<UpdateAddressResponseDTO> {
    return this.http.put<UpdateAddressResponseDTO>(ADDRESS_URL, req);
  }

  deleteAddress(): Observable<void> {
    return this.http.delete<void>(ADDRESS_URL);
  }

  findAddress(): Observable<FindAddressResponseDTO> {
    return this.http.get<FindAddressResponseDTO>(ADDRESS_URL);
  }
}
