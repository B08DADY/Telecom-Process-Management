export interface WorkOrderRequest { // <-- ADD "export" HERE
  customerName: string;
  customerEmail: string | null;
  customerAddress: string | null;
  customerPhone: string;
  workOrderDescription: string;
 
}