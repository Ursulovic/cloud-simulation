package rs.raf.demo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long date;

    @ManyToOne
    private Machine machine;
    @Column(nullable = false)
    private String operation;

    @Column(nullable = false)
    private String message;
}
