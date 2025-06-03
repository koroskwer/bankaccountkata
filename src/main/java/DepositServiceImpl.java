import java.math.BigDecimal;
import java.time.Clock;
import java.util.Currency;

public class DepositServiceImpl implements DepositService {

    private final BankEventRepository repository;
    private final Clock clock;
    private final BankEventFactory bankEventFactory;

    public DepositServiceImpl(Clock clock, BankEventRepository bankEventRepository, BankEventFactory bankEventFactory) {
        this.clock = clock;
        this.repository = bankEventRepository;
        this.bankEventFactory = bankEventFactory;
    }

    @Override
    public void deposit(int clientId, BigDecimal money, Currency currency) {
        BankEvent currentState = this.repository.getMostRecentBankEvent(clientId);

        if (!currentState.currency().equals(currency)) {
            throw new CurrencyMismatchException();
        }

        BigDecimal newBalance = currentState.balance().add(money);

        BankEvent event = this.bankEventFactory.createBankEvent(clientId, this.clock.instant(), money, newBalance, currency, BankEventType.DEPOSIT);
        this.repository.addEvent(event, clientId);
    }
}
