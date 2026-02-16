import { HttpClient } from '@angular/common/http';
import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/api/auth/login`,
      { username, password },
      { observe: 'response' }
    );
  }

  saveToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('jwt', token);
    }
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('jwt');
    }
    return null;
  }

  logout(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('jwt');
    }
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getUsername(): string {
    const token = this.getToken();
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.username || '';
    } catch {
      return '';
    }
  }

  getUserRoles(): string[] {
    const token = this.getToken();
    if (!token) return [];
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.Roles || [];
    } catch {
      return [];
    }
  }

  getUserBillerId(): string {
    const token = this.getToken();
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.billerId || '';
    } catch {
      return '';
    }
  }


  hasRole(role: string): boolean {
    const roles = this.getUserRoles();
    return roles.includes(role);
  }


  isAdmin(): boolean {
    return this.hasRole('ROLE_ADMIN');
  }


  isUser(): boolean {
    return this.hasRole('ROLE_USER');
  }


  getPrimaryRole(): string {
    const roles = this.getUserRoles();
    if (roles.length === 0) return 'User';
  
    return roles[0].replace('ROLE_', '').toLowerCase();
  }
}