import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable } from 'rxjs';

export interface GatewayConfigDTO {
  id?: string;
  name: string;
  fixedFee: number;
  percentageFee: number;
  dailyLimit: number;
  minTransaction: number;
  maxTransaction: number;
  processingTime: number;
  enabled: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class GatewayService {

  private apiUrl = 'http://localhost:8080/api/gateways/config';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  private getHeaders(): HttpHeaders {
    let token = '';

    if (isPlatformBrowser(this.platformId)) {
      token = localStorage.getItem('jwt') || '';
    }

    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getAll(): Observable<GatewayConfigDTO[]> {
    return this.http.get<GatewayConfigDTO[]>(
      `${this.apiUrl}/getAll`,
      { headers: this.getHeaders() }
    );
  }
  

  getById(id: string): Observable<GatewayConfigDTO> {
    return this.http.get<GatewayConfigDTO>(
      `${this.apiUrl}/get/${id}`,
      { headers: this.getHeaders() }
    );
  }

  create(dto: GatewayConfigDTO): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/create`,
      dto,
      { headers: this.getHeaders() }
    );
  }

  update(dto: GatewayConfigDTO): Observable<void> {
    return this.http.put<void>(
      `${this.apiUrl}/update`,
      dto,
      { headers: this.getHeaders() }
    );
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/delete/${id}`,
      { headers: this.getHeaders() }
    );
  }
}
