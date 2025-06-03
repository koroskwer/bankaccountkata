import java.math.BigDecimal;
import java.time.Clock;
import java.util.Currency;
import java.util.List;

public class BankAccountFacade {

    private final Clock clock;
    private final WithdrawalService withdrawalService;
    private final DepositService depositService;
    private final BankEventRepository bankEventRepository;
    private final BankEventFactory factory;

    public BankAccountFacade() {
        this.clock = Clock.systemDefaultZone();
        this.bankEventRepository = new BankEventRepositoryImpl();
        this.factory = new BankEventFactory();
        this.withdrawalService = new WithdrawalServiceImpl(this.clock, this.bankEventRepository, factory);
        this.depositService = new DepositServiceImpl(this.clock, this.bankEventRepository, factory);
    }

    public void deposit(int clientId, BigDecimal money, Currency currency) {
        this.depositService.deposit(clientId, money, currency);
    }

    public void withdraw(int clientId, BigDecimal money, Currency currency) {
        this.withdrawalService.withdraw(clientId, money, currency);
    }

    public List<BankEvent> getOperationsHistory(int clientId) {
        return this.bankEventRepository.getEventsForClientId(clientId);
    }

    public BigDecimal getCurrentBalance(int clientId) {
        return this.bankEventRepository.getMostRecentBankEvent(clientId).balance();
    }

    public void openAccount(int clientId, Currency currency) {
        this.bankEventRepository.addEvent(this.factory.createBankEvent(clientId, this.clock.instant(), new BigDecimal(0),
                new BigDecimal(0), currency, BankEventType.OPEN_ACCOUNT), clientId);
    }
}
