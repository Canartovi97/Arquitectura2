import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
}
