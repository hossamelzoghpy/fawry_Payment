import { Injectable, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastService } from './toast.service';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {
  private toastService = inject(ToastService);

  handleError(error: any, customMessage?: string): void {
    console.error('Error occurred:', error);

    let message = customMessage;
    if (error?.error?.errorMessage != undefined)
        message = error.error.errorMessage;

    if (message === undefined) {
        if (error instanceof HttpErrorResponse) {
        // Handle HTTP errors
        if (error.error?.message) {
            message = error.error.message;
        } else if (error.status === 0) {
            message = 'Unable to connect to server. Please check your internet connection.';
        } else if (error.status === 401) {
            message = 'Unauthorized. Please login again.';
        } else if (error.status === 403) {
            message = 'You do not have permission to perform this action.';
        } else if (error.status === 404) {
            message = 'Resource not found.';
        } else if (error.status === 500) {
            message = 'Server error. Please try again later.';
        } else if (error.statusText) {
            message = error.statusText;
        }
        } else if (error?.message) {
        message = error.message;
        }
    }
    this.toastService.error(message || 'An unexpected error occurred. Please try again.');
  }

  handleSuccess(message: string): void {
    this.toastService.success(message);
  }

  handleWarning(message: string): void {
    this.toastService.warning(message);
  }

  handleInfo(message: string): void {
    this.toastService.info(message);
  }
}