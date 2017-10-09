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

}
