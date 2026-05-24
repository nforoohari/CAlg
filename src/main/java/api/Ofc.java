package api;

import java.util.ArrayList;
import java.util.List;

public class Ofc {
    private Spo spo;
    private List<Djo> details;

    public Ofc() {
        this.details = new ArrayList<>();
    }

    public Spo getOrderStatus() {
        return this.spo;
    }

    public void setOrderStatus(Spo spo) {
        this.spo = spo;
    }

    public List<Djo> getDetails() {
        return this.details;
    }

    public void setDetails(List<Djo> details) {
        this.details = details;
    }
}
