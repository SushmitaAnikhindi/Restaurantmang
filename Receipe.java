package entities;

import java.util.Map;

public class Receipe {
    private Map<Ingredient,Double>composition;
    private String name;
    private double amount;

    public Receipe(String name, Map<Ingredient, Double>composition, double amount){
        this.amount = amount;
        this.composition = composition;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Ingredient, Double> getComposition() {
        return composition;
    }

    public void setComposition(Map<Ingredient, Double> composition) {
        this.composition = composition;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
