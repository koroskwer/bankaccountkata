import java.math.BigDecimal;
import java.time.Clock;
import java.util.Currency;

public class WithdrawalServiceImpl implements WithdrawalService {

    private final BankEventRepository repository;
    private final Clock clock;

    public WithdrawalServiceImpl(Clock clock, BankEventRepository bankEventRepository){
        this.clock = clock;
        this.repository = bankEventRepository;
    }

    @Override
    public void withdraw(int clientId, BigDecimal money, Currency currency) {
        BankEvent currentState = this.repository.getMostRecentBankEvent(clientId);

        if(!currentState.currency().equals(currency)){
            throw new CurrencyMismatchException();
        }
        if(currentState.balance().compareTo(money) == -1){
            throw new NotEnoughMoneyException();
        }

        System.out.println("current:"+currentState);
        System.out.println("money:"+money);
        BigDecimal newBalance = currentState.balance().subtract(money);
        System.out.println(newBalance);

        BankEvent event = new BankEvent(clientId, this.clock.instant(), money, newBalance, currency, BankEventType.WITHDRAWAL);
        this.repository.addEvent(event, clientId);
    }
}
