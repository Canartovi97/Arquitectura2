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
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string; // ajusta al nombre que devuelva tu backend
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'parking_token';

  constructor(private http: HttpClient) { }

  register(body: RegisterRequest): Observable<any> {
    return this.http.post(`${API_URL}/users/create`, body);
  }

  login(body: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${API_URL}/user/auth/login`, body).pipe(
      tap((res) => {
        if (res?.token) {
          localStorage.setItem(this.tokenKey, res.token);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
