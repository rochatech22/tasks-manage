import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../models/user.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  registerData: RegisterRequest = {
    name: '',
    email: '',
    password: '',
    confirmPassword: ''
  };
  
  loading = false;
  error = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit() {
    if (this.isFormValid()) {
      this.loading = true;
      this.error = '';
      
      this.authService.register(this.registerData).subscribe({
        next: (response) => {
          this.loading = false;
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.loading = false;
          this.error = error.error?.message || 'Erro ao registrar usuário';
        }
      });
    }
  }

  isFormValid(): boolean {
    return this.registerData.name.length >= 2 &&
           this.registerData.email.includes('@') &&
           this.registerData.password.length >= 6 &&
           this.registerData.password === this.registerData.confirmPassword;
  }

  passwordsMatch(): boolean {
    return this.registerData.password === this.registerData.confirmPassword;
  }
}

