import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

const API_URL = 'http://localhost:8080/api/v1';

export interface RegisterRequest {
  username: string;
  password: string;
  name: string;
  lastname: string;
  email: string;
  age: number;
  phone: string;
  roles: string[];
}

export interface LoginRequest {
  username: string;
  password: string;
}

// 👇 así viene la respuesta del backend
export interface LoginResponse {
  id: number;
  jwt: string;
  message: string;
  role: string;
}

interface DecodedToken {
  sub?: string;
  [key: string]: any;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'parking_token';
  private userIdKey = 'parking_user_id';
  private roleKey = 'parking_role';

  constructor(private http: HttpClient) { }

  // ---- REGISTER ----
  register(body: RegisterRequest): Observable<any> {
    console.log(body);
    return this.http.post(`${API_URL}/users/create`, body);
  }

  // ---- LOGIN ----
  login(body: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${API_URL}/user/auth/login`, body)
      .pipe(
        tap((res) => {
          if (res?.jwt) {
            localStorage.setItem(this.tokenKey, res.jwt);
          }

          if (res?.id !== undefined && res?.id !== null) {
            localStorage.setItem(this.userIdKey, String(res.id));
          }

          if (res?.role) {
            localStorage.setItem(this.roleKey, res.role);
          }
        })
      );
  }

  // ---- LOGOUT ----
  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userIdKey);
    localStorage.removeItem(this.roleKey);
  }

  // ---- TOKEN / SESSION ----
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // ---- Helpers extra (por si quieres sacar cosas del JWT) ----
  private decodeToken(): DecodedToken | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = token.split('.')[1];
      const normalized = payload.replace(/-/g, '+').replace(/_/g, '/');
      const decoded = atob(normalized);
      return JSON.parse(decoded);
    } catch (e) {
      console.error('Error decoding token', e);
      return null;
    }
  }

  getUsername(): string | null {
    const decoded = this.decodeToken();
    return decoded?.sub ?? null;
  }

  // ---- ID de usuario (desde localStorage) ----
  getCurrentUserId(): number | null {
    const raw = localStorage.getItem(this.userIdKey);
    if (!raw) return null;

    const num = Number(raw);
    return Number.isNaN(num) ? null : num;
  }

  // Alias por si en algún sitio ya usaste getUserId()
  getUserId(): number | null {
    return this.getCurrentUserId();
  }

  // ---- Rol y helpers por rol ----
  getUserRole(): string | null {
    const role = localStorage.getItem(this.roleKey);
    return role ?? null;
  }

  isOwner(): boolean {
    return this.getUserRole() === 'ROLE_OWNER';
  }

  // Ajusta el nombre del rol del usuario normal si en el backend es otro
  isClient(): boolean {
    return this.getUserRole() === 'ROLE_USER';
  }
}
