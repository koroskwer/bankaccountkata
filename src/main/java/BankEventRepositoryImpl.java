import java.util.*;

public class BankEventRepositoryImpl implements BankEventRepository {

    private final Map<Integer, PriorityQueue<BankEvent>> storage;

    public BankEventRepositoryImpl() {
        this.storage = new HashMap<>();
    }

    @Override
    public List<BankEvent> getEventsForClientId(int clientId) {
        List<BankEvent> result = new ArrayList<>(this.storage.get(clientId));
        result.sort(this.getComparator());
        return result;
    }

    public BankEvent getMostRecentBankEvent(int clientId) {
        this.storage.computeIfAbsent(clientId, k -> new PriorityQueue<>(this.getComparator()));
        return this.storage.get(clientId).peek();
    }

    @Override
    public void addEvent(BankEvent bankEvent, int clientId) {
        PriorityQueue<BankEvent> eventsForClient = this.storage.computeIfAbsent(clientId, k -> new PriorityQueue<>(this.getComparator()));
        eventsForClient.add(bankEvent);
        this.storage.put(clientId, eventsForClient);
    }

    private Comparator<BankEvent> getComparator() {
        return (bankEvent, bankEvent2) -> {
            if (bankEvent.date().isBefore(bankEvent2.date())) {
                return 1;
            } else if (bankEvent.date().isAfter(bankEvent2.date())) {
                return -1;
            }

            if (bankEvent.eventId() > bankEvent2.eventId()) {
                return -1;
            } else if(bankEvent.eventId() < bankEvent2.eventId()){
                return 1;
            }
            return 0;
        };
    }
}
