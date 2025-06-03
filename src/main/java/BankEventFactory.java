import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.concurrent.atomic.AtomicLong;

public class BankEventFactory {

    private final AtomicLong atomicLong;

    public BankEventFactory(){
        this.atomicLong = new AtomicLong();
    }

    public BankEvent createBankEvent(int clientId, Instant time, BigDecimal eventMoney, BigDecimal newBalance, Currency currency, BankEventType eventType){
        return new BankEvent(this.atomicLong.getAndIncrement(), clientId, time, eventMoney, newBalance, currency, eventType);
    }
}
