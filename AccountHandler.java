package service;

import entities.Expense;
import entities.Sales;

import java.util.List;

public class AccountHandler {
    public void printSales(List<Sales> salesList){
        for(Sales sales: salesList){
            System.out.println(sales.toString());
        }
    }
    public void printExpenses(List<Expense>expenseList){
        for (Expense expense : expenseList){
            System.out.println(expense.toString());
        }
    }
    public void printProfit(List<Sales> salesList,List<Expense> expenseList){
        double netprofit = 0;
        for(Sales sales: salesList){
            netprofit += sales.getAmount();
        }
        for (Expense expense:expenseList){
            netprofit -= expense.getAmount();
        }
        System.out.println("NetProfit so far is" + netprofit);
    }
}
