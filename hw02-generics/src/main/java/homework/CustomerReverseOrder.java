package homework;

import java.util.Deque;
import java.util.LinkedList;

public class CustomerReverseOrder {
    private final Deque<Customer> customerDeque;

    public CustomerReverseOrder() {
        customerDeque = new LinkedList<>();
    }
    public void add(Customer customer) {
        customerDeque.push(customer);
    }

    public Customer take() {
        return customerDeque.pop();
    }
}
