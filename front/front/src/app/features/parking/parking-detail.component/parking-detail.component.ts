import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import {
  ParkingService,
  ParkingSpace,
} from '../../../services/parking.service';
import {
  BookingService,
  Booking,
} from '../../../services/booking.service';
import {
  PaymentService,
} from '../../../services/payment.service';
import { AuthService } from '../../../services/auth.service';
import { SafeUrlPipe } from '../../../shared/pipes/safe-url-pipe';


@Component({
  selector: 'app-parking-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, SafeUrlPipe],
  templateUrl: './parking-detail.component.html',
  styleUrls: ['./parking-detail.component.scss'],
})
export class ParkingDetailComponent implements OnInit {
  parking?: ParkingSpace;
  booking?: Booking;

  loadingParking = false;
  loadingBooking = false;
  loadingPayment = false;

  error = '';
  success = '';
  paymentStatus = '';

  bookingForm: FormGroup;
  estimatedAmount = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private parkingService: ParkingService,
    private bookingService: BookingService,
    private paymentService: PaymentService,
    private auth: AuthService,
    private fb: FormBuilder
  ) {
    this.bookingForm = this.fb.group({
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
    });

    this.bookingForm.valueChanges.subscribe(() => {
      this.updateEstimatedAmount();
    });
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? +idParam : NaN;
    if (!id) {
      this.router.navigate(['/parking-spaces']);
      return;
    }

    this.loadParking(id);
  }

  private loadParking(id: number): void {
    this.loadingParking = true;
    this.error = '';

    this.parkingService.getParkingSpace(id).subscribe({
      next: (space) => {
        this.parking = space;
        this.loadingParking = false;
        this.updateEstimatedAmount();
        this.updateGoogleMapsUrl();
      },
      error: () => {
        this.loadingParking = false;
        this.error = 'No fue posible cargar la información del parqueadero.';
      },
    });
  }

  private updateEstimatedAmount(): void {
    if (!this.parking) {
      this.estimatedAmount = 0;
      return;
    }

    const { startTime, endTime } = this.bookingForm.value;

    if (!startTime || !endTime) {
      this.estimatedAmount = this.parking.pricePerHour ?? 0;
      return;
    }

    const start = new Date(startTime);
    const end = new Date(endTime);
    const diffMs = end.getTime() - start.getTime();

    if (diffMs <= 0) {
      this.estimatedAmount = this.parking.pricePerHour ?? 0;
      return;
    }

    const hours = diffMs / (1000 * 60 * 60);
    this.estimatedAmount = Math.max(
      this.parking.pricePerHour * Math.ceil(hours),
      this.parking.pricePerHour
    );
  }

  private getCurrentUserId(): number {
    // Para demo: si no tienes el id en el token, deja 1 y listo.
    // Si luego agregas "userId" o "sub" al JWT, lo puedes leer desde AuthService.
    return 1;
  }

  onCreateBooking(): void {
    if (!this.parking) return;
    if (this.bookingForm.invalid) {
      this.bookingForm.markAllAsTouched();
      return;
    }

    this.loadingBooking = true;
    this.error = '';
    this.success = '';
    this.paymentStatus = '';

    const { startTime, endTime } = this.bookingForm.value;

    const body = {
      parkingSpaceId: this.parking.id!,
      userId: this.getCurrentUserId(),
      startTime,
      endTime,
    };

    this.bookingService.createBooking(body).subscribe({
      next: (booking) => {
        this.booking = booking;
        this.loadingBooking = false;
        this.success = 'Reserva creada correctamente.';
      },
      error: () => {
        this.loadingBooking = false;
        this.error = 'No fue posible crear la reserva.';
      },
    });
  }

  onCancelBooking(): void {
    if (!this.booking) return;

    this.loadingBooking = true;
    this.error = '';
    this.success = '';
    this.paymentStatus = '';

    this.bookingService.cancelBooking(this.booking.id).subscribe({
      next: () => {
        this.loadingBooking = false;
        this.success = 'Reserva cancelada correctamente.';
        if (this.booking) {
          this.booking.status = 'CANCELLED';
        }
      },
      error: () => {
        this.loadingBooking = false;
        this.error = 'No fue posible cancelar la reserva.';
      },
    });
  }

  onPayMercadoPago(): void {
    if (!this.booking || !this.parking) return;

    this.loadingPayment = true;
    this.error = '';
    this.success = '';
    this.paymentStatus = '';

    const body = {
      bookingId: this.booking.id,
      userId: this.getCurrentUserId(),
      amount: this.estimatedAmount,
      currency: 'COP',
      description: `Reserva de estacionamiento en ${this.parking.title}`,
    };

    this.paymentService.payWithMercadoPago(body).subscribe({
      next: (res) => {
        this.loadingPayment = false;
        this.paymentStatus =
          'Pago enviado a Mercado Pago (demo). Revisa la respuesta en consola.';
        console.log('MercadoPago response', res);
      },
      error: () => {
        this.loadingPayment = false;
        this.error = 'No fue posible procesar el pago con Mercado Pago.';
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/parking-spaces']);
  }






  activeImageIndex = 0;




  get hasImages(): boolean {
    return !!this.parking?.imageUrls && this.parking.imageUrls.length > 0;
  }

  selectImage(index: number): void {
    this.activeImageIndex = index;
  }


  googleMapsUrl = '';

  private updateGoogleMapsUrl(): void {
    if (!this.parking?.location) {
      this.googleMapsUrl = '';
      return;
    }

    const { latitude, longitude } = this.parking.location;
    this.googleMapsUrl =
      `https://www.google.com/maps?q=${latitude},${longitude}&hl=es&z=16&output=embed`;
  }



}
