import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class BankAccountFacadeTest {

    private static final int CLIENT_ID = 1;
    private static final Currency SAMPLE_CURRENCY = Currency.getInstance(Locale.US);
    private static final Currency CURRENCY_FOR_MISMATCH = Currency.getInstance(Locale.JAPAN);

    @Test
    public void checkIfDepositIsRegistered() {
        // given
        BankAccountFacade testedFacade = new BankAccountFacade();
        testedFacade.openAccount(CLIENT_ID, SAMPLE_CURRENCY);

        // when
        testedFacade.deposit(CLIENT_ID, new BigDecimal(1000), SAMPLE_CURRENCY);

        // then
        Assertions.assertEquals(2, testedFacade.getOperationsHistory(CLIENT_ID).size());
        Assertions.assertEquals(new BigDecimal(1000), testedFacade.getCurrentBalance(CLIENT_ID));
    }

    @Test
    public void checkIfNotEnoughMoneyExceptionIsThrownWhenWithdrawExceedsAccountLimit() {
        // given
        BankAccountFacade testedFacade = new BankAccountFacade();
        testedFacade.openAccount(CLIENT_ID, SAMPLE_CURRENCY);

        // when
        Assertions.assertThrows(NotEnoughMoneyException.class, () -> testedFacade.withdraw(CLIENT_ID, new BigDecimal(100), SAMPLE_CURRENCY));
    }

    @Test
    public void checkIfCurrencyMismatchExceptionIsThrownWhenDifferentCurrencyIsProvided() {
        // given
        BankAccountFacade testedFacade = new BankAccountFacade();
        testedFacade.openAccount(CLIENT_ID, SAMPLE_CURRENCY);

        // when
        Assertions.assertThrows(CurrencyMismatchException.class, () -> testedFacade.withdraw(CLIENT_ID, new BigDecimal(100), CURRENCY_FOR_MISMATCH));
    }

    @Test
    public void checkIfWithdrawalIsRegistered() throws InterruptedException {
        // given
        BankAccountFacade testedFacade = new BankAccountFacade();
        testedFacade.openAccount(CLIENT_ID, SAMPLE_CURRENCY);
        testedFacade.deposit(CLIENT_ID, new BigDecimal(1000), SAMPLE_CURRENCY);

        // when
        testedFacade.withdraw(CLIENT_ID, new BigDecimal(500), SAMPLE_CURRENCY);

        // then
        Assertions.assertEquals(3, testedFacade.getOperationsHistory(CLIENT_ID).size());
        Assertions.assertEquals(new BigDecimal(500), testedFacade.getCurrentBalance(CLIENT_ID));
    }
}
