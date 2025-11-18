import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

const API_URL = 'http://localhost:8080/api/v1';

export interface ParkingLocation {
  address: string;
  city: string;
  neighborhood: string;
  latitude: number;
  longitude: number;

}

export interface ParkingSpace {
  id?: number;
  title: string;
  description: string;
  pricePerHour: number;
  status: string; // 'AVAILABLE', etc.
  ownerId: number;
  location: ParkingLocation;
  imageUrls?: string[];
}

export interface ParkingPage {
  content: ParkingSpace[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({ providedIn: 'root' })
export class ParkingService {
  constructor(private http: HttpClient) { }

  getParkingSpaces(page = 0, size = 10): Observable<ParkingPage> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<ParkingPage>(`${API_URL}/parking-spaces`, { params });
  }

  createParkingSpace(formData: FormData): Observable<any> {
    return this.http.post(`${API_URL}/parking-spaces`, formData);
  }


  getParkingSpace(id: number): Observable<ParkingSpace> {
    return this.http.get<ParkingSpace>(`${API_URL}/parking-spaces/${id}`);
  }


  getAvailableSpaces(): Observable<ParkingSpace[]> {
    return this.http.get<ParkingSpace[]>(`${API_URL}/parking-spaces/available`);
  }

}
