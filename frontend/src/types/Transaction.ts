import { TransactionType } from "./TransactionType";
import { Category } from "./Category";

export type Transaction = {
  id: number;
  amount: number;
  date: string;
  description: string;
  type: TransactionType;
  category: Category;
};