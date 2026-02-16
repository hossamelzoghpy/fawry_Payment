import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TransactionService, TransactionDTO, PaginatedResponse } from '../../services/transaction.service';
import { GatewayService, GatewayConfigDTO } from '../../services/gateway.service';
import { AuthService } from '../../services/auth.service';
import { ErrorHandlerService } from '../../common/service/errorhandler.service';
import { DatePipe, DecimalPipe, SlicePipe } from '@angular/common';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [FormsModule, DatePipe, DecimalPipe, SlicePipe],
  templateUrl: './transactions.component.html'
})
export class TransactionsComponent implements OnInit {
  private transactionService = inject(TransactionService);
  private gatewayService = inject(GatewayService);
  private authService = inject(AuthService);
  private errorHandler = inject(ErrorHandlerService);

  transactions = signal<TransactionDTO[]>([]);
  gateways = signal<GatewayConfigDTO[]>([]);
  isLoading = signal(false);
  isLoadingGateways = signal(false);
  hasSearched = signal(false);

  // Pagination signals
  currentPage = signal(0);
  totalPages = signal(0);
  pageSize = signal(10);

  billerId = signal('');
  gatewayId = signal('');
  date = signal('');
  maxDate = new Date().toISOString().split('T')[0];

  // Computed values for pagination info
  startItem = computed(() => (this.currentPage() * this.pageSize()) + 1);
  endItem = computed(() => (this.currentPage() * this.pageSize()) + this.transactions().length);
  hasNextPage = computed(() => this.currentPage() < this.totalPages() - 1);
  hasPreviousPage = computed(() => this.currentPage() > 0);

  ngOnInit(): void {
    this.billerId.set(this.authService.getUserBillerId());
    this.loadGateways();
  }

  loadGateways(): void {
    this.isLoadingGateways.set(true);
    this.gatewayService.getAll().subscribe({
      next: (data) => {
        this.gateways.set(data);
        this.isLoadingGateways.set(false);
      },
      error: (err) => {
        this.errorHandler.handleError(err, 'Failed to load gateways');
        this.isLoadingGateways.set(false);
      }
    });
  }

  onSearch(): void {
    if (!this.billerId()) {
      this.errorHandler.handleWarning('Biller ID is required');
      return;
    }
    this.currentPage.set(0);
    this.loadData(0);
  }

  loadData(pageNumber: number = 0): void {
    this.isLoading.set(true);
    this.hasSearched.set(true);

    this.transactionService.getTransactions(
      this.billerId(),
      this.gatewayId(),
      this.date(),
      pageNumber
    ).subscribe({
      next: (response: PaginatedResponse<TransactionDTO>) => {
        this.transactions.set(response.data);
        this.currentPage.set(response.currentPage);
        this.totalPages.set(response.totalPages);
        this.pageSize.set(response.pageSize);
        this.isLoading.set(false);
        
        if (response.data.length === 0 && pageNumber === 0) {
          this.errorHandler.handleInfo('No transactions found for the selected criteria');
        }
      },
      error: (err) => {
        this.errorHandler.handleError(err, 'Failed to load transactions');
        this.isLoading.set(false);
      }
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages() && page !== this.currentPage()) {
      this.loadData(page);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  nextPage(): void {
    if (this.hasNextPage()) {
      this.goToPage(this.currentPage() + 1);
    }
  }

  previousPage(): void {
    if (this.hasPreviousPage()) {
      this.goToPage(this.currentPage() - 1);
    }
  }

  getPageNumbers(): number[] {
    const total = this.totalPages();
    const current = this.currentPage();
    const delta = 2;
    const range: number[] = [];
    const rangeWithDots: number[] = [];

    for (let i = 0; i < total; i++) {
      if (
        i === 0 ||
        i === total - 1 ||
        (i >= current - delta && i <= current + delta)
      ) {
        range.push(i);
      }
    }

    let prev: number | undefined;
    for (const i of range) {
      if (prev !== undefined && i - prev > 1) {
        rangeWithDots.push(-1); // -1 represents ellipsis
      }
      rangeWithDots.push(i);
      prev = i;
    }

    return rangeWithDots;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'SUCCESS': return 'bg-green-100 text-green-700';
      case 'FAILED': return 'bg-red-100 text-red-600';
      case 'PENDING': return 'bg-yellow-100 text-yellow-700';
      case 'RECOMMENDED': return 'bg-blue-100 text-blue-700';
      default: return 'bg-gray-100 text-gray-600';
    }
  }

  getUrgencyClass(urgency: string): string {
    return urgency === 'INSTANT'
      ? 'bg-blue-100 text-blue-700'
      : 'bg-gray-100 text-gray-600';
  }

  getGatewayName(id: string): string {
    return this.gateways().find(g => g.id === id)?.name || id;
  }

  clearFilters(): void {
    this.gatewayId.set('');
    this.date.set('');
    this.transactions.set([]);
    this.hasSearched.set(false);
    this.currentPage.set(0);
    this.totalPages.set(0);
  }
}