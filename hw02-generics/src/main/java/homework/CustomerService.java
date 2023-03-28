package homework;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private final TreeMap<Customer, String> customerMap;

    public CustomerService() {
        customerMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        return Map.entry(new Customer(customerMap.firstEntry().getKey()), customerMap.firstEntry().getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = customerMap.higherEntry(customer);
        return higherEntry != null ? Map.entry(new Customer(higherEntry.getKey()), higherEntry.getValue()) : null;
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }
}
