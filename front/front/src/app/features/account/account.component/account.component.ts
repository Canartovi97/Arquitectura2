import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { UserService, UserProfile } from '../../../services/user.service';

interface CreditCard {
  id: number;
  alias: string;
  brand: string;
  last4: string;
  expiry: string; // MM/YY
  isDefault?: boolean;
}

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
  userProfile: UserProfile | null = null;

  profileForm: FormGroup;
  isSavingProfile = false;
  profileSaved = false;
  loadingProfile = false;
  loadError = '';

  // Tarjetas demo
  cards: CreditCard[] = [
    {
      id: 1,
      alias: 'Tarjeta principal',
      brand: 'VISA',
      last4: '4242',
      expiry: '12/28',
      isDefault: true,
    },
  ];

  cardForm: FormGroup;
  isSavingCard = false;
  cardSaved = false;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private userService: UserService
  ) {
    // Inicializamos el form vacío; luego lo rellenamos con los datos del backend
    this.profileForm = this.fb.group({
      username: [{ value: '', disabled: true }],
      name: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      age: [null, [Validators.required, Validators.min(8), Validators.max(100)]],
      phone: ['', [Validators.required, Validators.minLength(7)]],
    });

    this.cardForm = this.fb.group({
      alias: ['Tarjeta adicional', [Validators.required]],
      holderName: ['', [Validators.required]],
      cardNumber: ['', [Validators.required, Validators.minLength(12)]],
      expiryMonth: ['', [Validators.required]],
      expiryYear: ['', [Validators.required]],
    });
  }

  // ------------------- CARGA DE PERFIL -------------------

  ngOnInit(): void {
    const userId = this.auth.getUserId();
    console.log('userId para account:', userId);

    if (userId == null) {
      this.loadError = 'No se pudo identificar al usuario. Inicia sesión nuevamente.';
      return;
    }

    this.loadingProfile = true;
    this.userService.getUserProfile(userId).subscribe({
      next: (user) => {
        this.userProfile = user;

        this.profileForm.patchValue({
          username: user.username,
          name: user.name,
          lastname: user.lastname,
          email: user.email,
          age: user.age,
          phone: user.phone,
        });

        this.loadingProfile = false;
      },
      error: (err) => {
        console.error('Error cargando perfil', err);
        this.loadingProfile = false;
        this.loadError =
          'No fue posible cargar tu información de cuenta. Inténtalo más tarde.';
      },
    });
  }

  // ------------------- PERFIL -------------------

  saveProfile(): void {
    this.profileSaved = false;

    if (this.profileForm.invalid || !this.userProfile?.id) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const formValue = this.profileForm.getRawValue();

    const updated: UserProfile = {
      ...this.userProfile,
      name: formValue.name,
      lastname: formValue.lastname,
      email: formValue.email,
      age: formValue.age,
      phone: formValue.phone,
      // username viene deshabilitado, pero lo conservamos del perfil actual
    };

    this.isSavingProfile = true;

    this.userService.updateUserProfile(this.userProfile.id!, updated).subscribe({
      next: (saved) => {
        console.log('Perfil actualizado en backend:', saved);
        this.userProfile = saved;
        this.isSavingProfile = false;
        this.profileSaved = true;
      },
      error: (err) => {
        console.error('Error guardando perfil', err);
        this.isSavingProfile = false;
        // Puedes mostrar un mensaje más elaborado si quieres
      },
    });
  }

  // ------------------- TARJETAS (DEMO) -------------------

  addCard(): void {
    this.cardSaved = false;

    if (this.cardForm.invalid) {
      this.cardForm.markAllAsTouched();
      return;
    }

    const value = this.cardForm.value;
    const last4 =
      (value.cardNumber && value.cardNumber.toString().slice(-4)) || '4242';

    const card: CreditCard = {
      id: Date.now(),
      alias: value.alias!,
      brand: 'VISA', // demo
      last4,
      expiry: `${value.expiryMonth}/${value.expiryYear}`,
      isDefault: this.cards.length === 0,
    };

    console.log('Tarjeta agregada (demo, no se guarda en backend):', card);

    this.cards.push(card);
    this.cardForm.reset({
      alias: 'Tarjeta adicional',
      holderName: '',
      cardNumber: '',
      expiryMonth: '',
      expiryYear: '',
    });

    this.isSavingCard = true;
    setTimeout(() => {
      this.isSavingCard = false;
      this.cardSaved = true;
    }, 600);
  }

  setDefaultCard(card: CreditCard): void {
    this.cards = this.cards.map((c) => ({
      ...c,
      isDefault: c.id === card.id,
    }));
  }

  removeCard(card: CreditCard): void {
    if (card.isDefault && this.cards.length > 1) {
      const remaining = this.cards.filter((c) => c.id !== card.id);
      remaining[0].isDefault = true;
      this.cards = remaining;
    } else {
      this.cards = this.cards.filter((c) => c.id !== card.id);
    }
  }

  // Getters para el template
  get f() {
    return this.profileForm.controls;
  }

  get cf() {
    return this.cardForm.controls;
  }


  get initials(): string {
    const name = this.userProfile?.name?.trim() ?? '';
    const lastname = this.userProfile?.lastname?.trim() ?? '';

    const first = name.charAt(0).toUpperCase();
    const second = lastname.charAt(0).toUpperCase();

    // Si no hubiera nada, devolvemos una U por defecto
    return (first + second) || 'U';
  }
}
