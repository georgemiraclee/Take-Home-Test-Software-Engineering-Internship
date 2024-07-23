import java.util.Map;
import java.util.StringJoiner;

public class Customer {
    private String name;
    private Integer balance;
    private Map<String, Integer> owedFrom;
    private Map<String, Integer> owedTo;

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
        this.balance=0;
    }

    public Customer(String name, Integer balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Map<String, Integer> getOwedFrom() {
        return owedFrom;
    }

    public void setOwedFrom(Map<String, Integer> owedFrom) {
        this.owedFrom = owedFrom;
    }

    public Map<String, Integer> getOwedTo() {
        return owedTo;
    }

    public void setOwedTo(Map<String, Integer> owedTo) {
        this.owedTo = owedTo;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ",  "{", "}")
                .add("name='" + name + "'")
                .add("balance=" + balance)
                .toString();
    }
}
