package entities;

public class Expense {
    private double amount;
    private PurchaseOrder purchaseOrder;
    private ExpenseType expenseType;

    public Expense(PurchaseOrder purchaseOrder, ExpenseType expenseType, double amount){
        this.amount=amount;
        this.expenseType=expenseType;
        this.purchaseOrder=purchaseOrder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
    @Override

    public String toString(){
        return "Amount=" + this.getAmount() + ",ExpenseType=" + this.getExpenseType();

    }
}
