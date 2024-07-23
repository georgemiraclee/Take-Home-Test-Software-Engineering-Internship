import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class AtmApplication {
    private static Map<String, Customer> customers = new HashMap<>();
    private static String name;

    public static void main(String[] args) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String command = "";
        help();
        while (!command.equals("stop")){
            command = reader.readLine();
            String[] commands = null;
            String param = "";
            if(command != null) {
                commands = command.split(" ");
            }

            if(commands != null && commands.length > 0){
                param = commands[0];
            }

            if(param.equals("login") && commands.length == 2){
                if(name != null){
                    System.out.println("You should logout first");
                }else {
                    name = commands[1];
                    login();
                }
            }else if(param.equals("deposit") && commands.length == 2){
                deposit(commands[1]);
            }else if(param.equals("withdraw") && commands.length == 2){
                withdraw(commands[1]);
            }else if(param.equals("transfer") && commands.length == 3){
                transfer(commands[1], commands[2]);
            }else if(param.equals("logout")){
                logout();
            }else if(param.equals("help")){
                help();
            }else {
                System.out.println("Please read help");
            }
        }
        reader.close();
        input.close();
    }

    private static void logout() {
        if(name == null || name.equals("")){
            System.out.println("you are not login yet");
        }else {
            System.out.println("Goodbye, "+ name +"!");
            name = null;
        }
    }

    public static void help(){
        System.out.println("help command:\n");
        System.out.println("* `login [name]` - Logs in as this customer and creates the customer if not exist");
        System.out.println("* `deposit [amount]` - Deposits this amount to the logged in customer");
        System.out.println("* `withdraw [amount]` - Withdraws this amount from the logged in customer");
        System.out.println("* `transfer [target] [amount]` - Transfers this amount from the logged in customer to the target customer");
        System.out.println("* `logout` - Logs out of the current customer");
        System.out.println("* `help` - show all command\n");
    }

    public static void login(){
        Customer customer = null;
        if(customers.get(name) != null){
            customer = customers.get(name);
        }else {
            customer = new Customer(name);
            customers.put(name, customer);
        }
        System.out.println("Hello, "+ name+"!");
        System.out.println("Your balance is $"+ customer.getBalance());

        Map<String,Integer> owedFrom = customer.getOwedFrom();
        if(owedFrom != null){
            for(Map.Entry<String, Integer> entry: owedFrom.entrySet()){
                System.out.println("Owed $"+ entry.getValue() +" from "+ entry.getKey());
            }
        }

        Map<String,Integer> owedTo = customer.getOwedTo();
        if(owedTo != null){
            for(Map.Entry<String, Integer> entry: owedTo.entrySet()){
                System.out.println("Owed $"+ entry.getValue() +" to "+ entry.getKey());
            }
        }
    }

    public static void deposit(String value){
        int intValue =0;
        try{
            intValue = Integer.parseInt(value);
        }catch (Exception ex){
            System.out.println("the value is not number");
        }
        Customer customer = null;
        if((name != null || !name.equals("")) && customers.get(name) != null) {
            customer = customers.get(name);
        }else {
            System.out.println("Please login first, with command");
            System.out.println("login [name]");
        }
        if(customer != null) {
            int depositValue = intValue;
            Map<String,Integer> owedTo = customer.getOwedTo();
            if(owedTo != null){
                for(Map.Entry<String, Integer> entry: owedTo.entrySet()){
                    int transferValue = entry.getValue();
                    int owedPending = 0;
                    if(depositValue < entry.getValue()){
                        transferValue = depositValue;
                        owedPending = entry.getValue() - depositValue;
                        depositValue = 0;
                    }else {
                        depositValue = depositValue - entry.getValue();
                    }

                    Customer customerTransfer = customers.get(entry.getKey());
                    customerTransfer.setBalance(customerTransfer.getBalance() + transferValue);
                    Map<String, Integer> owedFrom = customerTransfer.getOwedFrom();
                    if(owedFrom == null){
                        owedFrom = new HashMap<>();
                    }
                    if(owedFrom.get(name) == null) {
                        owedFrom.put(name, transferValue);
                    }else {
                        owedFrom.put(name, owedPending);
                    }

                    customerTransfer.setOwedFrom(owedFrom);
                    customers.put(entry.getKey(), customerTransfer);

                    System.out.println("Transfer $"+ transferValue +" to "+ entry.getKey());
                    if(owedPending == 0){
                        owedTo.remove(entry.getKey());
                    }else {
                        owedTo.put(entry.getKey(),owedPending);
                        System.out.println("Your balance is $" + customer.getBalance());
                        System.out.println("Owed $"+ owedPending +" to "+ entry.getKey());
                    }
                }
                // update owed to
                customer.setOwedTo(owedTo);
                if(depositValue > 0) {
                    customer.setBalance(customer.getBalance() + depositValue);
                    System.out.println("Your balance is $" + customer.getBalance());
                }

            }else {
                customer.setBalance(customer.getBalance() + intValue);
                System.out.println("Your balance is $" + customer.getBalance());
            }
            customers.put(name, customer);
        }
    }

    public static void withdraw(String value){
        int intValue =0;
        try{
            intValue = Integer.parseInt(value);
        }catch (Exception ex){
            System.out.println("the value is not number");
        }

        if((name != null || !name.equals("")) && customers.get(name) != null) {
            Customer customer = customers.get(name);
            int withdrawValue = intValue;
            if(customer.getBalance() < intValue){
                withdrawValue = customer.getBalance();
            }
            customer.setBalance(customer.getBalance() - withdrawValue);
            customers.put(name, customer);
            System.out.println("Withdraw $"+ withdrawValue+"!");
            System.out.println("Your balance is $" + customer.getBalance());
        }else {
            System.out.println("Please login first, with command");
            System.out.println("login [name]");
        }
    }


    @SuppressWarnings("null")
    public static void transfer(String target, String value){
        int intValue =0;
        try{
            intValue = Integer.parseInt(value);
        }catch (Exception ex){
            System.out.println("the value is not number");
        }
        Customer customer = null;
        if((name != null || !name.equals("")) && customers.get(name) != null) {
            customer = customers.get(name);
        }else {
            System.out.println("Please login first, with command");
            System.out.println("login [name]");
        }

        Customer targetCustomer = null;
        if((target != null || !target.equals(""))&& intValue > 0){
            int owedValue = 0;
            int transferValue = intValue;

            if(customer.getBalance() < intValue) {
                transferValue = customer.getBalance();
                owedValue = intValue - customer.getBalance();
                Map<String, Integer> owedTo = customer.getOwedTo();
                if(owedTo == null){
                    owedTo = new HashMap<>();
                }
                if(owedTo.get(target) == null) {
                    owedTo.put(target, owedValue);
                }else {
                    owedTo.put(target, owedTo.get(target)+ owedValue);
                }
                customer.setOwedTo(owedTo);
            }

            Map<String, Integer> owedFrom = customer.getOwedFrom();
            if(owedFrom != null && owedFrom.get(target) != null){
                owedFrom.put(target, owedFrom.get(target)-transferValue);
                customer.setOwedFrom(owedFrom);

                System.out.println("your balance is $"+ customer.getBalance());
                System.out.println("Owed $"+ owedFrom.get(target) +" from "+target);
            }else if (owedValue > 0) {
                customer.setBalance(0);
            }else {
                customer.setBalance(customer.getBalance() - transferValue);
                System.out.println("Transferred $"+transferValue+" to "+target);
                System.out.println("your balance is $"+ customer.getBalance());
            }
            customers.put(name, customer);

            targetCustomer = customers.get(target);
            Map<String, Integer> owedTargetTo= targetCustomer.getOwedTo();
            if(owedValue > 0){
                Map<String, Integer> owedTargetFrom = targetCustomer.getOwedFrom();
                if(owedTargetFrom == null){
                    owedTargetFrom = new HashMap<>();
                }

                if(owedTargetFrom.get(name) == null){
                    owedTargetFrom.put(name, owedValue);
                }else{
                    owedTargetFrom.put(name, owedTargetFrom.get(name) + owedValue);
                }
                targetCustomer.setOwedFrom(owedTargetFrom);
                targetCustomer.setBalance(targetCustomer.getBalance()+transferValue);

                System.out.println("Transferred $"+transferValue+" to "+target);
                System.out.println("your balance is $"+ customer.getBalance());
                System.out.println("Owed $"+ owedValue +" to "+ target);
            }else if(owedTargetTo != null && owedTargetTo.get(name) != null){
                //transfer 40,
                if(transferValue - owedTargetTo.get(name) >= 0){
                    owedTargetTo.remove(name);
                    int balance =  transferValue - owedTargetTo.get(name);
                    targetCustomer.setBalance(balance);
                }else {
                    owedTargetTo.put(name, owedTargetTo.get(name) - transferValue);
                }
                targetCustomer.setOwedTo(owedTargetTo);
            }else {
                targetCustomer.setBalance(targetCustomer.getBalance()+transferValue);
            }

            customers.put(target, targetCustomer);
        }else {
            System.out.println("Please input target name");
            System.out.println("transfer [target] [amount]");
        }
    }
}
