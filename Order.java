package entities;

import java.util.UUID;

public class Order {
    private Receipe receipe;
    private double amount;
    private String orderId;

    public Order(Receipe receipe, double amount){
        this.amount= amount;
        this.orderId= UUID.randomUUID().toString();
        this.receipe=receipe;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Receipe getReceipe() {
        return receipe;
    }

    public void setReceipe(Receipe receipe) {
        this.receipe = receipe;
    }

    public String getOrderId() {
        return orderId;
    }
}
