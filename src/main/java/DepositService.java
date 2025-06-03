import java.math.BigDecimal;
import java.util.Currency;

public interface DepositService {
    void deposit(int clientId, BigDecimal money, Currency currency);
}
