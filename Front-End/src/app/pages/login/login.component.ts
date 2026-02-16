import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  isLoading = signal(false);

  constructor(private authService: AuthService, private router: Router) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/gateways']);
    }
  }

  onLogin(): void {
    this.isLoading.set(true);
    this.errorMessage = '';

    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        console.log("response ha[pens");
        console.log(response);
        const token = response.headers.get('Authorization');
        if (token) {
          this.authService.saveToken(token);
          
          if (this.authService.isAdmin()) {
            this.router.navigate(['/gateways']);
          } else if (this.authService.isUser()) {
            this.router.navigate(['/transactions']);
          } else {
            this.router.navigate(['/login']);
          }
        } else {
          this.errorMessage = response.body?.message || 'Login failed. Please try again.';
          this.isLoading.set(false);
        }
          
      },
      error: (error) => {
        console.log(error);
        console.log("error ha[pens");
        this.isLoading.set(false);
        this.errorMessage = error.error.errorMessage || 'An error occurred during login. Please try again.';        
      }
    });
  }
}
