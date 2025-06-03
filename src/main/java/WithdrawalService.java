import java.math.BigDecimal;
import java.util.Currency;

public interface WithdrawalService {
    void withdraw(int clientId, BigDecimal money, Currency currency);
}
