import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { Inject, PLATFORM_ID } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ErrorHandlerService } from '../../common/service/errorhandler.service';
import { DecimalPipe } from '@angular/common';

interface SimpleGatewayResponse {
  id: string;
  name: string;
  estimatedCommission: number;
  processingTime: string;
}

interface RecommendationResponse {
  recommendedGateway: SimpleGatewayResponse;
  alternatives: SimpleGatewayResponse[];
}

@Component({
  selector: 'app-recommend',
  standalone: true,
  imports: [FormsModule, DecimalPipe],
  templateUrl: './recommend.component.html',
})
export class RecommendComponent implements OnInit {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private errorHandler = inject(ErrorHandlerService);

  billerId = signal('');
  amount = signal<number | null>(null);
  urgency = signal('INSTANT');
  isLoading = signal(false);
  result = signal<RecommendationResponse | null>(null);

  urgencyOptions = [
    { value: 'INSTANT', label: 'Instant' },
    { value: 'CAN_WAIT', label: 'Can Wait' },
  ];

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    this.billerId.set(this.authService.getUserBillerId());
  }

  private getHeaders(): HttpHeaders {
    let token = '';
    if (isPlatformBrowser(this.platformId)) {
      token = localStorage.getItem('jwt') || '';
    }
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  onSubmit(): void {
    // Validation
    if (!this.amount() || this.amount()! <= 0) {
      this.errorHandler.handleWarning('Please enter a valid amount');
      return;
    }

    if (!this.urgency()) {
      this.errorHandler.handleWarning('Please select urgency level');
      return;
    }

    this.isLoading.set(true);
    this.result.set(null);

    const body = {
      billerId: this.billerId(),
      amount: this.amount(),
      urgency: this.urgency(),
    };

    this.http
      .post<RecommendationResponse>('http://localhost:8080/api/payments/recommend', body, {
        headers: this.getHeaders(),
      })
      .subscribe({
        next: (data) => {
          this.result.set(data);
          this.isLoading.set(false);
          this.errorHandler.handleSuccess('Recommendation generated successfully');
        },
        error: (err) => {
          this.errorHandler.handleError(err, 'Failed to get recommendation');
          this.isLoading.set(false);
        },
      });
  }

  resetForm(): void {
    this.amount.set(null);
    this.urgency.set('INSTANT');
    this.result.set(null);
  }
}