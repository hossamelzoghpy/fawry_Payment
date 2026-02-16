import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
})
export class SidebarComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);

  username = signal('');
  userRole = signal('');
  isAdmin = signal(false);
  isUser = signal(false);

  ngOnInit(): void {
    this.username.set(this.authService.getUsername());
    this.userRole.set(this.authService.getPrimaryRole());
    this.isAdmin.set(this.authService.isAdmin());
    this.isUser.set(this.authService.isUser());
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}