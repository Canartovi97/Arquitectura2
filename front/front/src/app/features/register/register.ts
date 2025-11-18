import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.html',
  styleUrls: ['./register.scss'],
})
export class Register {
  form: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';

protected roles = ['OWNER', 'CUSTOMER'];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(4)]],
      name: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      age: [null, [Validators.required, Validators.min(18)]],
      // phone: [null, Validators.required]
      phone: ['', Validators.required],
      roles: [['CUSTOMER'], Validators.required]


    });
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.register(this.form.value).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Usuario creado correctamente. Ahora puedes iniciar sesión.';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage =
          err.error?.message || 'Error al crear el usuario. Revisa los datos.';
      },
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }



}
