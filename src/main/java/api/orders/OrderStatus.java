package api.orders;

import api.enums.Status;

import java.util.Date;

public class OrderStatus {

    private Status status;
    private Date statusDate;

    public OrderStatus(Status status, Date statusDate) {
        this.status = status;
        this.statusDate = statusDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
}
