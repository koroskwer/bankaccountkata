import java.math.BigDecimal;
import java.time.Clock;
import java.util.Currency;

public class DepositServiceImpl implements DepositService {

    private final BankEventRepository repository;
    private final Clock clock;

    public DepositServiceImpl(Clock clock, BankEventRepository bankEventRepository) {
        this.clock = clock;
        this.repository = bankEventRepository;
    }

    @Override
    public void deposit(int clientId, BigDecimal money, Currency currency) {
        BankEvent currentState = this.repository.getMostRecentBankEvent(clientId);

        if (!currentState.currency().equals(currency)) {
            throw new CurrencyMismatchException();
        }

        BigDecimal newBalance = currentState.balance().add(money);

        System.out.println("deposit date:"+this.clock.instant());
        BankEvent event = new BankEvent(clientId, this.clock.instant(), money, newBalance, currency, BankEventType.DEPOSIT);
        this.repository.addEvent(event, clientId);
    }
}
