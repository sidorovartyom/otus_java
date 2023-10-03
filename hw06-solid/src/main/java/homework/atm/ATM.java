package homework.atm;

public interface ATM {
    void addMoney(int denomination, int unit);
    int getBalance();
    void getBalanceForDenomination();
    void requestMoney(int balance);
}
