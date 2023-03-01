package rs.raf.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column(nullable = false)
    private long creationDate;


    @Column(nullable = false)
    private String status;

    @ManyToOne()
    private User creator;


    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean busy;

    @Column(nullable = false)
    private String name;

    @Version
    private long version;

}
