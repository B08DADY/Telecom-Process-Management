// src/app/services/work-order.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WorkOrderRequest } from '../models/work-order-request.model';
import { WorkOrderResponse } from '../models/work-order-response.model';

@Injectable({
  providedIn: 'root'
})
export class WorkOrderService {

  // Your Spring Boot API's base URL
  private apiUrl = 'http://localhost:8080/workorders';

  constructor(private http: HttpClient) { }

  /**
   * Creates a new work order by sending a POST request to the backend.
   * @param workOrderData The work order data from the form.
   * @returns An Observable of the server's response.
   */
  createWorkOrder(workOrderData: WorkOrderRequest): Observable<WorkOrderResponse> {
    return this.http.post<WorkOrderResponse>(this.apiUrl, workOrderData);
  }
}