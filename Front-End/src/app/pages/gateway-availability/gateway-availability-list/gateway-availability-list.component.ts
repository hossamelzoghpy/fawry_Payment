import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  GatewayAvailabilityService,
  GatewayAvailabilityDTO,
} from '../../../services/gateway-availability.service';
import { GatewayService, GatewayConfigDTO } from '../../../services/gateway.service';
import { ErrorHandlerService } from '../../../common/service/errorhandler.service';

@Component({
  selector: 'app-gateway-availability-list',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './gateway-availability-list.component.html',
})
export class GatewayAvailabilityListComponent implements OnInit {
  private errorHandler = inject(ErrorHandlerService);
  private availabilityService = inject(GatewayAvailabilityService);
  private gatewayService = inject(GatewayService);

  availabilities = signal<GatewayAvailabilityDTO[]>([]);
  gateways = signal<GatewayConfigDTO[]>([]);
  selectedGatewayId = signal<string>('');
  isLoading = signal(false);
  showModal = signal(false);
  isEditMode = signal(false);
  hasSearched = signal(false);

  days = [
    { value: 'MONDAY', label: 'Monday' },
    { value: 'TUESDAY', label: 'Tuesday' },
    { value: 'WEDNESDAY', label: 'Wednesday' },
    { value: 'THURSDAY', label: 'Thursday' },
    { value: 'FRIDAY', label: 'Friday' },
    { value: 'SATURDAY', label: 'Saturday' },
    { value: 'SUNDAY', label: 'Sunday' },
  ];

  dto: GatewayAvailabilityDTO = this.emptyDto();

  ngOnInit(): void {
    this.loadGateways();
  }

  loadGateways(): void {
    this.isLoading.set(true);
    this.gatewayService.getAll().subscribe({
      next: (data) => {
        this.gateways.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.errorHandler.handleError(err, 'Failed to load gateways');
        this.isLoading.set(false);
      },
    });
  }

  onSearch(): void {
    if (!this.selectedGatewayId()) {
      this.errorHandler.handleWarning('Please select a gateway first');
      return;
    }
    
    this.hasSearched.set(true);
    this.loadAvailabilities();
  }

  loadAvailabilities(): void {
    const gatewayId = this.selectedGatewayId();
    if (!gatewayId) {
      this.availabilities.set([]);
      return;
    }

    this.isLoading.set(true);
    this.availabilityService.getAllPerGateway(gatewayId).subscribe({
      next: (data) => {
        this.availabilities.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.errorHandler.handleError(err, 'Failed to load availabilities');
        this.isLoading.set(false);
      },
    });
  }

  getGatewayName(id: string): string {
    return this.gateways().find((g) => g.id === id)?.name || id;
  }

  emptyDto(): GatewayAvailabilityDTO {
    return { 
      gatewayId: '', 
      dayOfWeek: 'MONDAY', 
      startTime: '09:00', 
      endTime: '17:00' 
    };
  }

  getDayLabel(day: string): string {
    return this.days.find((d) => d.value === day)?.label || day;
  }

  openModal(item?: GatewayAvailabilityDTO): void {
    if (!this.selectedGatewayId() && !item) {
      this.errorHandler.handleWarning('Please select a gateway first');
      return;
    }

    this.isEditMode.set(!!item);
    this.dto = item ? { ...item } : {
      ...this.emptyDto(),
      gatewayId: this.selectedGatewayId()
    };
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
    this.dto = this.emptyDto();
  }

  onSubmit(): void {
    this.isLoading.set(true);

    const today = new Date();

    const start = new Date(today);
    const [startHour, startMinute] = this.dto.startTime.split(':').map(Number);
    start.setHours(startHour, startMinute, 0, 0);

    const end = new Date(today);
    const [endHour, endMinute] = this.dto.endTime.split(':').map(Number);
    end.setHours(endHour, endMinute, 0, 0);

    const formatTime = (date: Date): string => {
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${hours}:${minutes}:${seconds}`;
    };

    const payload = {
      ...this.dto,
      startTime: formatTime(start),
      endTime: formatTime(end),
    };

    const request = this.isEditMode()
      ? this.availabilityService.update(payload)
      : this.availabilityService.create(payload);

    request.subscribe({
      next: () => {
        const message = this.isEditMode()
          ? 'Availability updated successfully'
          : 'Availability created successfully';
        
        this.errorHandler.handleSuccess(message);
        this.closeModal();
        this.loadAvailabilities();
      },
      error: (error) => {
        this.isLoading.set(false);
        this.errorHandler.handleError(error);
      },
    });
  }

  deleteItem(id: number): void {
    if (confirm('Are you sure you want to delete this record?')) {
      this.availabilityService.delete(id.toString()).subscribe({
        next: () => {
          this.errorHandler.handleSuccess('Availability deleted successfully');
          this.loadAvailabilities();
        },
        error: (err) => {
          this.errorHandler.handleError(err, 'Failed to delete availability');
        }
      });
    }
  }
}