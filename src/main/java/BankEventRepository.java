import java.util.List;

public interface BankEventRepository {

    List<BankEvent> getEventsForClientId(int clientId);

    void addEvent(BankEvent bankEvent, int clientId);

    BankEvent getMostRecentBankEvent(int clientId);
}
