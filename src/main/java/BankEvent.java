import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

public record BankEvent(int clientId, Instant date, BigDecimal eventMoney, BigDecimal balance, Currency currency,
                        BankEventType type) {
}
