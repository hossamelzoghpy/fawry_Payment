import { Component, OnInit, signal, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GatewayService, GatewayConfigDTO } from '../../../services/gateway.service';
import { CommonModule } from '@angular/common';
import { ErrorHandlerService } from '../../../common/service/errorhandler.service';

@Component({
  selector: 'app-gateway-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gateway-list.component.html'
})
export class GatewayListComponent implements OnInit {
  private gatewayService = inject(GatewayService);
  private errorHandler = inject(ErrorHandlerService);

  gateways = signal<GatewayConfigDTO[]>([]);
  isLoading = signal(false);
  showModal = signal(false);
  isEditMode = signal(false);

  dto: GatewayConfigDTO = this.emptyDto();

  ngOnInit(): void {
    this.loadGateways();   
  }

  emptyDto(): GatewayConfigDTO {
    return {
      name: '',
      fixedFee: 0,
      percentageFee: 0,
      dailyLimit: 0,
      minTransaction: 0,
      maxTransaction: 0,
      processingTime: 0,
      enabled: true
    };
  }

  loadGateways(): void {
    this.isLoading.set(true);

    this.gatewayService.getAll().subscribe({
      next: (data) => {
        console.log('Gateways loaded:', data);
        this.gateways.set(data || []);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.errorHandler.handleError(err);
        this.isLoading.set(false);
      }
    });
  }

  openModal(gw?: GatewayConfigDTO): void {
    this.isEditMode.set(!!gw);
    this.dto = gw ? { ...gw } : this.emptyDto();
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
    this.dto = this.emptyDto();
  }

  onSubmit(): void {
    this.isLoading.set(true);

    const request = this.isEditMode()
      ? this.gatewayService.update(this.dto)
      : this.gatewayService.create(this.dto);

    request.subscribe({
      next: () => {
        const message = this.isEditMode() 
          ? 'Gateway updated successfully' 
          : 'Gateway created successfully';
        
        this.errorHandler.handleSuccess(message);
        this.closeModal();
        this.loadGateways();
      },
      error: (err) => {
        const message = this.isEditMode() 
          ? 'Failed to update gateway' 
          : 'Failed to create gateway';
        
        this.errorHandler.handleError(err, message);
        this.isLoading.set(false);
      }
    });
  }

  deleteGateway(id?: string): void {
    if (!id) return;

    if (confirm('Are you sure you want to delete this gateway?')) {
      this.gatewayService.delete(id).subscribe({
        next: () => {
          this.errorHandler.handleSuccess('Gateway deleted successfully');
          this.loadGateways();
        },
        error: (err) => {
          this.errorHandler.handleError(err, 'Failed to delete gateway');
        }
      });
    }
  }
}