import { TransactionType } from "./TransactionType";

export type Category = {
  id: number;
  name: string;
  type: TransactionType; 
};