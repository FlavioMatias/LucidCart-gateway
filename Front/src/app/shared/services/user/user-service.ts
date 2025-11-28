import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { jwtDecode, JwtPayload } from 'jwt-decode';
import { tap } from 'rxjs/operators';
import { HOST, BASE_URL } from '../../../app.config';
// =====================================
// BASE URL (same pattern as other files)
// =====================================

const AUTH_URL = `${BASE_URL}/auth`;
const PROFILE_URL = `${BASE_URL}/profile`;

// =====================================
// MODELS
// =====================================

// ----- SIGNUP -----
export interface SignUpDTO {
  Email: string;
  Password: string;
}

export interface SignupResponse {
  id: number | null;
  email: string | null;
}

// ----- SIGNIN -----
export interface SignInDTO {
  Email: string;
  Password: string;
}

export interface SignInResponse {
  token: string;
}

// ----- PROFILE PHOTO -----
export interface UploadPhotoResponse {
  photo_url: string;
}

// =====================================
// SERVICE
// =====================================

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  // ---------- SIGNUP ----------
  signup(req: SignUpDTO): Observable<SignupResponse> {
    return this.http.post<SignupResponse>(`${AUTH_URL}/signup`, req);
  }

  // ---------- SIGNIN ----------
  signin(req: SignInDTO): Observable<SignInResponse> {
    return this.http
      .post<SignInResponse>(`${AUTH_URL}/signin`, req)
      .pipe(
        tap(res => {
          if (res?.token) {
            sessionStorage.setItem('token', res.token);
          }
        })
      );
  }
  logout() {
    sessionStorage.removeItem("token");
  }
  // ---------- PROFILE PHOTO UPLOAD ----------
  uploadPhoto(file: File): Observable<UploadPhotoResponse> {
    const formData = new FormData();
    formData.append('photo', file);

    return this.http.post<UploadPhotoResponse>(
      `${PROFILE_URL}/photo`,
      formData
    );
  }

  // =====================================
  // TOKEN METHODS (added as requested)
  // =====================================

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  getTokenExpiration(): number | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const decoded = jwtDecode<JwtPayload>(token);
      return decoded.exp ? decoded.exp * 1000 : null;
    } catch {
      console.log("Failed to decode JWT");
      return null;
    }
  }

  isTokenExpired(): boolean {
    const exp = this.getTokenExpiration();
    if (!exp) return true;
    return exp < Date.now();
  }
}
