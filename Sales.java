package entities;

public class Sales {
    private Order order;
    private double amount;

    public Sales(double amount,Order order){
        this.amount=amount;
        this.order=order;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString(){
        return "Receipe=" +this.getOrder().getReceipe().getName() + ",Amount=" +this.getAmount();
    }
}
