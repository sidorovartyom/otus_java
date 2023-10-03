package homework.atm;

import java.util.*;
import java.util.TreeMap;

import static java.util.Comparator.comparingInt;

public class ATMIml implements ATM{
    int balance;
    private MoneyVault moneyVault;
    private final int[] denominationList = new int[]{100, 200, 500, 1000, 2000, 5000};

    public ATMIml() {
        Denomination[] denomination = Denomination.values();
        moneyVault = new MoneyVault(denomination);
    }

    public void addMoney(int denomination, int unit) {
        moneyVault.addMoney(denomination, unit);
    }
    public int getBalance() {
        return moneyVault.getBalance();
    }
    public void getBalanceForDenomination() {
        moneyVault.getBalanceForDenomination();
    }
    public void requestMoney(int balance) {
        moneyVault.requestMoney(balance);
    }
}