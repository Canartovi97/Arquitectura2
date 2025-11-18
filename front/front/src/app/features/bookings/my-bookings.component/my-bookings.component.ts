import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Booking, BookingService } from '../../../services/booking.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-my-bookings',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './my-bookings.component.html',
  styleUrls: ['./my-bookings.component.scss'],
})
export class MyBookingsComponent implements OnInit {
  bookings: Booking[] = [];
  loading = false;
  error = '';

  constructor(
    private bookingService: BookingService,
    private auth: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // 👉 ahora usamos el helper del AuthService
    const userId = this.auth.getUserId(); // alias de getCurrentUserId()
    console.log('userId en MyBookings:', userId);

    if (userId == null) {
      this.error = 'No se pudo identificar el usuario. Inicia sesión de nuevo.';
      return;
    }

    this.loadBookings(userId);
  }

  private loadBookings(userId: number): void {
    this.loading = true;
    this.error = '';

    this.bookingService.getBookingsByUser(userId).subscribe({
      next: (data) => {
        this.bookings = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando reservas', err);
        this.error = 'No fue posible cargar tus reservas. Inténtalo de nuevo.';
        this.loading = false;
      },
    });
  }

  goToParking(booking: Booking): void {
    this.router.navigate(['/parking-spaces', booking.parkingSpaceId]);
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'CANCELLED':
        return 'Cancelada';
      case 'CONFIRMED':
        return 'Confirmada';
      case 'PENDING':
        return 'Pendiente';
      default:
        return status;
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'CANCELLED':
        return 'badge-cancelled';
      case 'CONFIRMED':
        return 'badge-confirmed';
      case 'PENDING':
        return 'badge-pending';
      default:
        return 'badge-default';
    }
  }
}
