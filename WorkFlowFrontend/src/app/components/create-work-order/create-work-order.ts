// src/app/components/create-work-order/create-work-order.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { WorkOrderService } from '../../services/work-order'; // Corrected Path
import { WorkOrderRequest } from '../../models/work-order-request.model';
import { WorkOrderResponse } from '../../models/work-order-response.model'; // <-- ADDED IMPORT

@Component({
  selector: 'app-create-work-order',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-work-order.html',
  styleUrls: ['./create-work-order.css'] // Corrected from .scss to .css to match your file
})
export class CreateWorkOrderComponent implements OnInit {
  workOrderForm!: FormGroup;
  loading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private workOrderService: WorkOrderService
  ) {}

  ngOnInit(): void {
    this.workOrderForm = this.fb.group({
      customerName: ['', [Validators.required, Validators.minLength(3)]],
      customerEmail: ['', [Validators.email]],
      customerPhone: ['', [Validators.required, Validators.pattern(/^01[0125][0-9]{8}$/)]],
      customerAddress: [''],
      workOrderDescription: ['', [Validators.required, Validators.minLength(10)]],
    });
  }

  get f() { return this.workOrderForm.controls; }

  onSubmit(): void {
    this.successMessage = null;
    this.errorMessage = null;
    this.workOrderForm.markAllAsTouched();

    if (this.workOrderForm.invalid) {
      return;
    }

    this.loading = true;
    const requestData: WorkOrderRequest = this.workOrderForm.value;

    this.workOrderService.createWorkOrder(requestData).subscribe({
      // FIXED: Added explicit types for response and err
      next: (response: WorkOrderResponse) => {
        this.successMessage = `Work Order #${response.workOrderId} created successfully for ${response.customerName}!`;
        this.workOrderForm.reset();
      },
      error: (err: any) => { // Using 'any' for error is acceptable
        if (err.error && typeof err.error === 'object') {
           this.errorMessage = Object.values(err.error).join(', ');
        } else {
           this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
      },
      complete: () => {
        this.loading = false;
      }
    });
  }
}