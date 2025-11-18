import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8080/api/v1';

export interface MercadoPagoPaymentRequest {
  bookingId: string;
  userId: number;
  amount: number;
  currency: string;
  description: string;
}

@Injectable({ providedIn: 'root' })
export class PaymentService {
  constructor(private http: HttpClient) { }

  payWithMercadoPago(body: MercadoPagoPaymentRequest): Observable<any> {
    return this.http.post(`${API_URL}/payments/mercado-pago`, body);
  }
}
