package homework.atm;

public class Money {
    private int denomination;

    public Money(int denomination) {
        this.denomination = denomination;
    }
    public int getDenomination() {
        return denomination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        if (this.denomination == money.denomination) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return denomination;
    }
}
