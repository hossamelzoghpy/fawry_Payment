import { Injectable, signal } from '@angular/core';

export interface Toast {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  duration?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  toasts = signal<Toast[]>([]);

  private generateId(): string {
    return Math.random().toString(36).substring(2, 9);
  }

  private addToast(type: Toast['type'], message: string, duration = 5000): void {
    const id = this.generateId();
    const toast: Toast = { id, type, message, duration };
    
    this.toasts.update(current => [...current, toast]);

    if (duration > 0) {
      setTimeout(() => this.removeToast(id), duration);
    }
  }

  success(message: string, duration?: number): void {
    this.addToast('success', message, duration);
  }

  error(message: string, duration?: number): void {
    this.addToast('error', message, duration);
  }

  warning(message: string, duration?: number): void {
    this.addToast('warning', message, duration);
  }

  info(message: string, duration?: number): void {
    this.addToast('info', message, duration);
  }

  removeToast(id: string): void {
    this.toasts.update(current => current.filter(t => t.id !== id));
  }

  clearAll(): void {
    this.toasts.set([]);
  }
}