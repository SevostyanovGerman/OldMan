package main.model;


import javax.persistence.*;

@Entity
@Table(name="files")
public class File {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "url")
    private String url;

}

