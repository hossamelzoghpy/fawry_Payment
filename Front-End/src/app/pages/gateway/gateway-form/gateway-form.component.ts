import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { GatewayService, GatewayConfigDTO } from '../../../services/gateway.service';
import { ErrorHandlerService } from '../../../common/service/errorhandler.service';

@Component({
  selector: 'app-gateway-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gateway-form.component.html',
})
export class GatewayFormComponent implements OnInit {
    private errorHandler = inject(ErrorHandlerService);
  isEditMode = signal(false) ;
  isLoading = signal(false);
  gatewayId: string | null = null;
  dto: GatewayConfigDTO = {
    name: '',
    fixedFee: 0,
    percentageFee: 0,
    dailyLimit: 0,
    minTransaction: 0,
    maxTransaction: 0,
    processingTime: 0,
    enabled: true,
  };
  constructor(
    private gatewayService: GatewayService,
    private route: ActivatedRoute,
    public router: Router,
  ) {}

  ngOnInit(): void {
    this.gatewayId = this.route.snapshot.paramMap.get('id');
    if (this.gatewayId) {
      this.isEditMode.set(true);
      this.gatewayService.getById(this.gatewayId).subscribe({
        next: (data) => (this.dto = data),
      });
    }
  }

  onSubmit(): void {
    this.isLoading.set(true);
    const request = this.isEditMode()
      ? this.gatewayService.update(this.dto)
      : this.gatewayService.create(this.dto);

    request.subscribe({
      next: () => this.router.navigate(['/gateways']),
      error: (err) => {
        this.isLoading.set(false);
        this.errorHandler.handleError(err);
      },
    });
  }
}
