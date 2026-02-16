import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable } from 'rxjs';

export interface GatewayAvailabilityDTO {
  id?: number;
  gatewayId: string;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
}

@Injectable({
  providedIn: 'root'
})
export class GatewayAvailabilityService {
  private apiUrl = 'http://localhost:8080/api/gateways/availability';

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

  getAll(): Observable<GatewayAvailabilityDTO[]> {
    return this.http.get<GatewayAvailabilityDTO[]>(`${this.apiUrl}/getAll`, { headers: this.getHeaders() });
  }

  getAllPerGateway(gateWayId: string): Observable<GatewayAvailabilityDTO[]> {
      return this.http.get<GatewayAvailabilityDTO[]>(
        `${this.apiUrl}/getAll/${gateWayId}`,
        { headers: this.getHeaders() }
      );
    }
  getById(id: string): Observable<GatewayAvailabilityDTO> {
    return this.http.get<GatewayAvailabilityDTO>(`${this.apiUrl}/get/${id}`, { headers: this.getHeaders() });
  }

  create(dto: GatewayAvailabilityDTO): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/create`, dto, { headers: this.getHeaders() });
  }

  update(dto: GatewayAvailabilityDTO): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/update`, dto, { headers: this.getHeaders() });
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`, { headers: this.getHeaders() });
  }
}
