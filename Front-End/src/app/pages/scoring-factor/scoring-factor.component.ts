import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ScoringFactorService, ScoringFactorConfigDto } from '../../services/scoring-factor.service';
import { ErrorHandlerService } from '../../common/service/errorhandler.service';

@Component({
  selector: 'app-scoring-factor',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './scoring-factor.component.html'
})
export class ScoringFactorComponent implements OnInit {
    private errorHandler = inject(ErrorHandlerService);

  factors: ScoringFactorConfigDto[] = [];
  isLoading = signal(false);
  showModal = signal(false);
  isEditMode = signal(false);

  dto: ScoringFactorConfigDto = this.emptyDto();

  constructor(private scoringFactorService: ScoringFactorService) {}

  ngOnInit(): void {
    this.loadData();
  }

  emptyDto(): ScoringFactorConfigDto {
    return { code: '', weight: 0 };
  }

  loadData(): void {
    this.isLoading.set(true);
    this.scoringFactorService.getAll().subscribe({
      next: (data) => { this.factors = data; this.isLoading.set(false); },
      error: (err) => { this.isLoading.set(false); this.errorHandler.handleError(err); }
    });
  }

  openModal(item?: ScoringFactorConfigDto): void {
    this.isEditMode.set(!!item);
    this.dto = item ? { ...item } : this.emptyDto();
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  onSubmit(): void {
    this.isLoading.set(true);
    const request = this.isEditMode()
      ? this.scoringFactorService.update(this.dto)
      : this.scoringFactorService.create(this.dto);

    request.subscribe({
      next: () => { this.closeModal(); this.loadData(); },
      error: (err) => { this.isLoading.set(false); this.errorHandler.handleError(err); }
    });
  }

  deleteItem(code: string): void {
    if (confirm('Are you sure you want to delete this factor?')) {
      this.scoringFactorService.delete(code).subscribe({
        next: () => this.loadData(),
        error: (err) => this.errorHandler.handleError(err)
      });
    }
  }
}