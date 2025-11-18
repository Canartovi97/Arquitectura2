import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ParkingService } from '../../../services/parking.service';

@Component({
  selector: 'app-parking-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './parking-form.component.html',
  styleUrls: ['./parking-form.component.scss'],
})
export class ParkingFormComponent {
  form: FormGroup;
  images: File[] = [];
  loading = false;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private parkingService: ParkingService,
    private router: Router
  ) {
    this.form = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      pricePerHour: [null, [Validators.required, Validators.min(0)]],
      status: ['AVAILABLE', Validators.required],
      ownerId: [1, Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required],
      neighborhood: ['', Validators.required],
      latitude: [null, Validators.required],
      longitude: [null, Validators.required],
    });
  }

  onFilesSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.images = Array.from(input.files);
    }
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const request = {
      title: this.form.value.title,
      description: this.form.value.description,
      pricePerHour: this.form.value.pricePerHour,
      status: this.form.value.status,
      ownerId: this.form.value.ownerId,
      location: {
        address: this.form.value.address,
        city: this.form.value.city,
        neighborhood: this.form.value.neighborhood,
        latitude: this.form.value.latitude,
        longitude: this.form.value.longitude,
      },
    };

    const formData = new FormData();

    // 👇 Parte JSON con tipo application/json (igual que en Postman)
    const jsonBlob = new Blob([JSON.stringify(request)], {
      type: 'application/json',
    });
    formData.append('request', jsonBlob);

    // 👇 Imágenes (puedes mantenerlo como lo tenías)
    this.images.forEach((file) => {
      formData.append('images', file, file.name);
    });

    this.loading = true;
    this.error = '';
    this.success = '';

    this.parkingService.createParkingSpace(formData).subscribe({
      next: () => {
        this.loading = false;
        this.success = 'Espacio creado correctamente.';
        setTimeout(() => this.router.navigate(['/dashboard']), 1500);
      },
      error: () => {
        this.loading = false;
        this.error =
          'Ocurrió un error al crear el espacio. Verifica los datos e inténtalo de nuevo.';
      },
    });
  }


  cancel(): void {
    this.router.navigate(['/dashboard']);
  }
}
