import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { ParkingService, ParkingSpace } from '../../../services/parking.service';

@Component({
  selector: 'app-parking-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './parking-dashboard.component.html',
  styleUrls: ['./parking-dashboard.component.scss'],
})
export class ParkingDashboardComponent implements OnInit {
  spaces: ParkingSpace[] = [];
  loading = false;
  error = '';
  page = 0;
  size = 10;
  totalPages = 0;

  constructor(
    private parkingService: ParkingService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadSpaces();
  }

  loadSpaces(): void {
    this.loading = true;
    this.error = '';

    this.parkingService.getParkingSpaces(this.page, this.size).subscribe({
      next: (res) => {
        // si tu backend usa otro nombre en vez de "content", ajústalo aquí
        this.spaces = (res.content || []).filter(
          (s) => s.status === 'AVAILABLE'
        );
        this.totalPages = res.totalPages ?? 1;
        this.loading = false;
      },
      error: () => {
        this.error = 'No fue posible cargar los espacios de parqueo.';
        this.loading = false;
      },
    });
  }

  nextPage(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.loadSpaces();
    }
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadSpaces();
    }
  }

  goToCreate(): void {
    this.router.navigate(['/parking-spaces/new']);
  }
}
