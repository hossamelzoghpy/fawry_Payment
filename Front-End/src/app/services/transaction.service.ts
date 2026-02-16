import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable } from 'rxjs';

export interface TransactionDTO {
  id: string;
  billerId: string;
  gatewayId: string;
  amount: number;
  commission: number;
  urgency: string;
  status: string;
  createdAt: string;
}

export interface PaginatedResponse<T> {
  data: T[];
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = 'http://localhost:8080/api/billers/logs';

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

  getTransactions(
    billerId: string, 
    gatewayId?: string, 
    date?: string,
    pageNumber: number = 0
  ): Observable<PaginatedResponse<TransactionDTO>> {
    let params = new HttpParams().set('pageNumber', pageNumber.toString());
    
    if (gatewayId) params = params.set('gatewayId', gatewayId);
    if (date) params = params.set('date', date);

    return this.http.get<PaginatedResponse<TransactionDTO>>(
      `${this.apiUrl}/${billerId}/transactions`,
      { headers: this.getHeaders(), params }
    );
  }
}