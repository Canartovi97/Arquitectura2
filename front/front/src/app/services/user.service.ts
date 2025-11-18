import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8080/api/v1';

export interface UserProfile {
  id?: number;
  username: string;
  name: string;
  lastname: string;
  email: string;
  age: number | null;
  phone: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) { }

  getUserProfile(id: number): Observable<UserProfile> {
    // Ajusta la URL si tu backend usa otra ruta
    return this.http.get<UserProfile>(`${API_URL}/users/${id}`);
  }

  updateUserProfile(id: number, body: Partial<UserProfile>): Observable<UserProfile> {
    // PUT o PATCH según tu backend
    return this.http.put<UserProfile>(`${API_URL}/users/${id}`, body);
  }
}
