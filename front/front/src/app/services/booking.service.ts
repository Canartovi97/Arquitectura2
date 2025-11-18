import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8080/api/v1';

export type BookingStatus = 'PENDING' | 'CONFIRMED' | 'CANCELLED';

export interface Booking {
  id: string;
  parkingSpaceId: number;
  userId: number;
  startTime: string;
  endTime: string;
  status: BookingStatus;
  // Si más adelante el backend devuelve monto, puedes agregar:
  // totalAmount?: number;
}

export interface CreateBookingRequest {
  parkingSpaceId: number;
  userId: number;
  startTime: string; // ISO string
  endTime: string;   // ISO string
}

@Injectable({ providedIn: 'root' })
export class BookingService {
  constructor(private http: HttpClient) { }

  createBooking(body: CreateBookingRequest): Observable<Booking> {
    return this.http.post<Booking>(`${API_URL}/bookings`, body);
  }

  cancelBooking(bookingId: string): Observable<void> {
    return this.http.patch<void>(
      `${API_URL}/bookings/${bookingId}/cancel`,
      {}
    );
  }

  /**
   * Obtiene las reservas de un usuario.
   * Usa el endpoint: GET /bookings?userId=...&parkingSpaceId=...
   * (parkingSpaceId es opcional)
   */
  getBookingsByUser(userId: number, parkingSpaceId?: number): Observable<Booking[]> {
    let params = new HttpParams().set('userId', userId);

    if (parkingSpaceId != null) {
      params = params.set('parkingSpaceId', parkingSpaceId);
    }

    return this.http.get<Booking[]>(`${API_URL}/bookings`, { params });
  }
}
