export interface WorkOrderResponse {
  workOrderId: string;
  workOrderDescription: string;
  customerID: string;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  customerAddress: string;
  createdAt: string;
  statues: string;
  TechnicianID: string | null;
}