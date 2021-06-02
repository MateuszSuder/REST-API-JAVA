package RESTAPIproject.declarations;

import java.time.LocalDateTime;
import java.util.Date;

public class OrderStatus {
    private final Status status;
    private final LocalDateTime date;

    public OrderStatus(Status s) {
        this.status = s;
        this.date = LocalDateTime.now();
    }

    public OrderStatus(Status s, LocalDateTime d) {
        this.status = s;
        this.date = d;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }
}
