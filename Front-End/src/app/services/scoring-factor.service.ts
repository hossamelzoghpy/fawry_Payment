import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable } from 'rxjs';

export interface ScoringFactorConfigDto {
  code: string;
  weight: number;
}

@Injectable({
  providedIn: 'root'
})
export class ScoringFactorService {
  private apiUrl = 'http://localhost:8080/api/factor/config';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  private getHeaders(): HttpHeaders {
    let token = '';
    if (isPlatformBrowser(this.platformId)) {
      token = localStorage.getItem('jwt') || '';
    }
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getAll(): Observable<ScoringFactorConfigDto[]> {
    return this.http.get<ScoringFactorConfigDto[]>(`${this.apiUrl}/getAll`, { headers: this.getHeaders() });
  }

  create(dto: ScoringFactorConfigDto): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/create`, dto, { headers: this.getHeaders() });
  }

  update(dto: ScoringFactorConfigDto): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/update`, dto, { headers: this.getHeaders() });
  }

  delete(code: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete`, {
      headers: this.getHeaders(),
      params: { code }
    });
  }
}
