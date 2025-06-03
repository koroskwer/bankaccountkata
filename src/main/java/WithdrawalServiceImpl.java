import java.math.BigDecimal;
import java.time.Clock;
import java.util.Currency;

public class WithdrawalServiceImpl implements WithdrawalService {

    private final BankEventRepository repository;
    private final Clock clock;
    private final BankEventFactory bankEventFactory;

    public WithdrawalServiceImpl(Clock clock, BankEventRepository bankEventRepository, BankEventFactory bankEventFactory) {
        this.clock = clock;
        this.repository = bankEventRepository;
        this.bankEventFactory = bankEventFactory;
    }

    @Override
    public void withdraw(int clientId, BigDecimal money, Currency currency) {
        BankEvent currentState = this.repository.getMostRecentBankEvent(clientId);

        if (!currentState.currency().equals(currency)) {
            throw new CurrencyMismatchException();
        }
        if (currentState.balance().compareTo(money) == -1) {
            throw new NotEnoughMoneyException();
        }

        BigDecimal newBalance = currentState.balance().subtract(money);

        BankEvent event = this.bankEventFactory.createBankEvent(clientId, this.clock.instant(), money, newBalance, currency, BankEventType.WITHDRAWAL);
        this.repository.addEvent(event, clientId);
    }
}
