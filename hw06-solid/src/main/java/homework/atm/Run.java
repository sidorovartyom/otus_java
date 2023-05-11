package homework.atm;


class Run {

    public static ATMIml atm;

    public static void main(String[] args){
        atm = new ATMIml();
        atm.addMoney(100, 3);
        atm.addMoney(200, 2);
        atm.addMoney(2000, 1);
        atm.addMoney(500, 2);
        System.out.println("Balance - " + atm.getBalance());
        atm.getBalanceForDenomination();
        System.out.println("---------- ");
        atm.requestMoney(1500);
        System.out.println("Balance - " + atm.getBalance());
        atm.getBalanceForDenomination();
        System.out.println("---------- ");
        atm.requestMoney(1500);
        System.out.println("Balance - " + atm.getBalance());
        atm.getBalanceForDenomination();
        //run();
    }
}