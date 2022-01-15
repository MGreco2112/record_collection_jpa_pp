package com.recordcollection.recorddatabase.connections;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.recordcollection.recorddatabase.models.Collector;

import javax.persistence.*;

@Entity
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originator_id", referencedColumnName = "id")
    @JsonIncludeProperties("id")
    private Collector originator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    @JsonIncludeProperties("id")
    private Collector recipient;

    @Enumerated(EnumType.STRING)
    private EConnection type;

    public Connection() {
    }

    public Connection(Collector originator, Collector recipient, EConnection type) {
        this.originator = originator;
        this.recipient = recipient;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collector getOriginator() {
        return originator;
    }

    public void setOriginator(Collector originator) {
        this.originator = originator;
    }

    public Collector getRecipient() {
        return recipient;
    }

    public void setRecipient(Collector recipient) {
        this.recipient = recipient;
    }

    public EConnection getType() {
        return type;
    }

    public void setType(EConnection type) {
        this.type = type;
    }
}
