import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BASE_URL } from '../../../app.config';

// =====================================
// MODELS
// =====================================

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

  // Avaliação dinâmica da URL
  private get ADDRESS_URL() { return `${BASE_URL}/address`; }

  createAddress(req: AddressRequestDTO): Observable<CreateAddressResponseDTO> {
    return this.http.post<CreateAddressResponseDTO>(this.ADDRESS_URL, req);
  }

  updateAddress(req: AddressRequestDTO): Observable<UpdateAddressResponseDTO> {
    return this.http.put<UpdateAddressResponseDTO>(this.ADDRESS_URL, req);
  }

  deleteAddress(): Observable<void> {
    return this.http.delete<void>(this.ADDRESS_URL);
  }

  findAddress(): Observable<FindAddressResponseDTO> {
    return this.http.get<FindAddressResponseDTO>(this.ADDRESS_URL);
  }
}
