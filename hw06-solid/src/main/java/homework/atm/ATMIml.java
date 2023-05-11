package homework.atm;

import java.util.*;
import java.util.TreeMap;

import static java.util.Comparator.comparingInt;

public class ATMIml implements ATM{
    TreeMap<Money, Integer> moneyMap = new TreeMap<>(comparingInt(o -> o.getDenomination()));
    int balance;
    private final int[] denominationList = new int[]{100, 200, 500, 1000, 2000, 5000};

    public ATMIml() {
        for(int i = 0; i < denominationList.length; i++){
            moneyMap.put(new Money(denominationList[i]), 0);
        }
    }
    public void addMoney(int denomination, int unit) {
        var m = new Money(denomination);
        var b = moneyMap.get(m);
        b += unit;
        this.balance += unit * denomination;
        moneyMap.put(m, b);
    }
    public int getBalance() {
        return balance;
    }
    public void getBalanceForDenomination() {
        moneyMap.forEach((k,v) -> System.out.println("Balance denomination "+k.getDenomination()+" - "+v + " unit " + " - " + k.getDenomination() * v + " rub"));

    }
    public void requestMoney(int balance) {
        TreeMap<Money, Integer> moneyMap_new = new TreeMap<>(comparingInt(o -> o.getDenomination()));
        moneyMap_new.putAll(moneyMap);
        var request_balance = balance;
        var balance_new = this.balance;
        if (getBalance() <= balance){
            System.out.println("Not enough money, balance - " + getBalance() + ", request - " + balance);
        } else {
            for (Map.Entry<Money, Integer> item : moneyMap_new.descendingMap().entrySet()) {
                if (item.getValue() > 0
                        && item.getKey().getDenomination() <= balance
                        && ((item.getKey().getDenomination() * item.getValue()) % balance == 0
                            || (item.getKey().getDenomination() * item.getValue()) / balance > 0
                            || balance / item.getKey().getDenomination() > 0)
                ){
                    var balance_out = 0;
                    if ((item.getKey().getDenomination() * item.getValue()) % balance == 0){
                        balance_out = balance;
                    } else if ((item.getKey().getDenomination() * item.getValue()) / balance > 0){
                        balance_out = (item.getKey().getDenomination() * item.getValue()) / balance * item.getKey().getDenomination();;
                    } else if (balance / item.getKey().getDenomination() > 0){
                        var balance_qty = balance / item.getKey().getDenomination();
                        if (balance_qty <= item.getValue()) {
                            balance_out =  balance_qty * item.getKey().getDenomination();
                        } else {
                            balance_out =  item.getValue() * item.getKey().getDenomination();
                        }
                    }
                    var m = item.getKey();
                    var b = moneyMap_new.get(m);
                    b -= balance_out / item.getKey().getDenomination();
                    balance_new -= balance_out;
                    moneyMap_new.put(m, b);
                    balance -=  balance_out;
                    if (balance == 0){
                        break;
                    }
                }
            }
            if (balance != 0){
                System.out.println("Request - " + request_balance + ". Not enough money, please enter another amount");
            } else {
                System.out.println("Request - " + request_balance + ". Successfull");
                moneyMap = moneyMap_new;
                this.balance = balance_new;
            }
        }
    }
}