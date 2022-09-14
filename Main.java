import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import entities.*;
import exceptions.IngredientNotFoundException;
import exceptions.InsufficientIngredientException;
import exceptions.InsufficentMoneyException;
import exceptions.ReceipeNotFoundException;
import io.IngredientIO;
import service.AccountHandler;
import service.IngredientHandler;
import service.ReceipeHandler;

public class Main {

    private static List<Sales> salesList;
    private static List<Expense> expenseList;
    private static double availableMoney;
    private static  List<Ingredient> ingredientList;
    private static List<Receipe> receipeList;

    private static AccountHandler accountHandler;

    private static IngredientHandler ingredientHandler;
    private static ReceipeHandler receipeHandler;
    private static IngredientIO ingredientIO;

    public static void main(String[] args) throws FileNotFoundException {

        ingredientIO = new IngredientIO();
        ingredientHandler = new IngredientHandler();

        CommandType currentCommand = CommandType.NO_COMMAND;
        Ingredient selectedIngredient = null;
        double ingredientQty = 0;
        Receipe selectedReceipe = null ;
        Map<Ingredient,Double> insufficientIngredients = null;

        ingredientList = ingredientIO.readIngredientList("resources/ing.txt");

        while (true) {
            try {
                if (currentCommand == CommandType.NO_COMMAND) {
                    int selectedNumber = displayPrompt();
                    currentCommand = CommandType.values()[selectedNumber];
                }
                else if (currentCommand == CommandType.INPUT_INGREDIENT_QTY){
                    ingredientQty = inputIngredientQty();
                    if(ingredientHandler.isPossibleToOrderIngredient(selectedIngredient, ingredientQty, availableMoney)){
                        System.out.println("Ordered successfully");
                        purchaseIngredient(selectedIngredient,ingredientQty);
                        currentCommand = CommandType.NO_COMMAND;
                    }
                    else{
                        throw new InsufficentMoneyException();
                    }
                }
                else if (currentCommand == CommandType.ORDER_SPECIFIC_INGREDIENT) {
                    selectedIngredient = selectIngredient();
                    currentCommand = CommandType.INPUT_INGREDIENT_QTY;
                }
                else if (currentCommand == CommandType.VIEW_AVAILABLE_INGREDIENTS) {
                    ingredientHandler.viewIngredients(ingredientList);
                    currentCommand = CommandType.NO_COMMAND;
                }
                else if (currentCommand == CommandType.VIEW_TOTAL_SALES) {
                    accountHandler.printSales(salesList);
                    currentCommand = CommandType.NO_COMMAND;
                }
                else if (currentCommand == CommandType.VIEW_TOTAL_EXPENSES) {
                    accountHandler.printExpenses(expenseList);
                    currentCommand = CommandType.NO_COMMAND;
                }

                else if (currentCommand == CommandType.VIEW_NET_PROFIT) {
                    accountHandler.printProfit(salesList, expenseList);
                    currentCommand = CommandType.NO_COMMAND;
                }
                else if (currentCommand == CommandType.PLACE_ORDER) {
                    selectedReceipe = selectReceipe();
                    receipeHandler.CheckIfPossibleToPrepareReceipe(selectedReceipe,ingredientList);
                }
                else if (currentCommand == CommandType.ORDER_MULTIPLE_INGREDIENTS) {
                    ingredientHandler.isPossibleToOrderIngredients(insufficientIngredients, availableMoney);
                    purchaseIngredients(insufficientIngredients);
                    currentCommand = CommandType.FINALIZE_ORDER;
                }
                else if (currentCommand == CommandType.FINALIZE_ORDER) {
                    finalizeOrder(selectedReceipe);
                }
                else if (currentCommand == CommandType.EXIT) {
                    System.exit(0);
                }
            }
            catch(InsufficientIngredientException ex){
                insufficientIngredients = ex.getInsufficientIngredients();
                currentCommand = CommandType.ORDER_MULTIPLE_INGREDIENTS;
            }
            catch(InsufficentMoneyException ex){
                System.out.println(ex.getMessage());
                currentCommand = CommandType.NO_COMMAND;
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
    public static int displayPrompt() {
        System.out.println("Please select one of the following command");
        System.out.println("1. View Available Ingredient");
        System.out.println("2. Order Specific Ingredient");
        System.out.println("3. View Total Sales");
        System.out.println("4. View Total Expenses");
        System.out.println("5. View Net Profit");
        System.out.println("6. Place Order");
        System.out.println("7. Exit From The Program");

        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
    public static Ingredient selectIngredient(){
        Scanner scanner = new Scanner(System.in);
        String ingredientName = scanner.nextLine();

        for (int i= 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getName().equals(ingredientName)) {
                return ingredientList.get(i);
            }
        }
        throw new IngredientNotFoundException("Ingredient" + ingredientName + " not found!");
    }
    public static Receipe selectReceipe() {
        Scanner scanner = new Scanner(System.in);
        String receipeName = scanner.nextLine();

        for (int i= 0; i< receipeList.size(); i++) {
            if (receipeList.get(i).getName().equals(receipeName)) {
                return receipeList.get(i);
            }
        }
        throw new ReceipeNotFoundException("Receipe" + receipeName + "not found");
    }
    public static double inputIngredientQty(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }
    public static void purchaseIngredient(Ingredient ingredientOrdered,double qtyOrdered){
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getName().equals(ingredientOrdered.getName())) {
                double oldQty = ingredientList.get(i).getQty();
                ingredientList.get(i).setQty(oldQty + qtyOrdered);
            }
        }
        double totalAmount = ingredientOrdered.getRate()*qtyOrdered;
        Map<Ingredient,Double> composition = new HashMap<>();
        composition.put(ingredientOrdered,qtyOrdered);
        PurchaseOrder po = new PurchaseOrder(totalAmount,composition);
        expenseList.add(new Expense(po, ExpenseType.PURCHASE, totalAmount));
        availableMoney -= totalAmount;
    }
    public static void purchaseIngredients(Map<Ingredient, Double>ingredientToOrder){
        double moneySpent = 0.0;
        for (int i = 0; i< ingredientList.size(); i++) {
            if (ingredientToOrder.containsKey(ingredientList.get(i))) {
                double oldQty = ingredientList.get(i).getQty();
                double qtyToOrder = ingredientToOrder.get(ingredientList.get(i));
                moneySpent += qtyToOrder * ingredientList.get(i).getRate();
                ingredientList.get(i).setQty(oldQty + qtyToOrder);
            }
        }
        PurchaseOrder po = new PurchaseOrder(moneySpent,ingredientToOrder);
        Expense expense = new Expense(po, ExpenseType.PURCHASE, moneySpent);
        expenseList.add(expense);
        availableMoney -= moneySpent;
    }
    public static void finalizeOrder(Receipe receipe){
        Map<Ingredient,Double>composition = receipe.getComposition();

        for (int i =0; i< ingredientList.size(); i++) {
            Ingredient currentIngredient = ingredientList.get(i);
            if (composition.containsKey(currentIngredient)) {
                double oldQty = currentIngredient.getQty();
                ingredientList.get(i).setQty(oldQty - composition.get(currentIngredient));
            }
        }
        Order order = new Order (receipe, receipe.getAmount());
        Sales sale = new Sales(receipe.getAmount(), order);

        salesList.add(sale);
        availableMoney += receipe.getAmount();
    }

}



