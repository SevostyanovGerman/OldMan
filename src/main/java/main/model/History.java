package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="history")
public class History {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_received")
    private Date dateRecieved;

    @Column(name = "date_transferred")
    private Date dateTransferred;

    @Column(name = "status")
    private String status;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        History history = (History) o;

        if (id != null ? !id.equals(history.id) : history.id != null) return false;
        if (dateRecieved != null ? !dateRecieved.equals(history.dateRecieved) : history.dateRecieved != null)
            return false;
        if (dateTransferred != null ? !dateTransferred.equals(history.dateTransferred) : history.dateTransferred != null)
            return false;
        if (status != null ? !status.equals(history.status) : history.status != null) return false;
        if (from != null ? !from.equals(history.from) : history.from != null) return false;
        return to != null ? to.equals(history.to) : history.to == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateRecieved != null ? dateRecieved.hashCode() : 0);
        result = 31 * result + (dateTransferred != null ? dateTransferred.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}
