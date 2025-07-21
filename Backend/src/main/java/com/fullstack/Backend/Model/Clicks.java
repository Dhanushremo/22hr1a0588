package com.fullstack.Backend.Model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name="clicks")
public class Clicks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "ref")
    private String ref;
    @Column(name = "location")
    private String location;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(name = "urlid")
    private Url url;

    public Clicks(String ref, String location, LocalDateTime timestamp, Url url) {
        this.ref = ref;
        this.location = location;
        this.timestamp = timestamp;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public Clicks() {
    }

    public Clicks(Long id, String ref, String location, LocalDateTime timestamp, Url url) {
        this.id = id;
        this.ref = ref;
        this.location = location;
        this.timestamp = timestamp;
        this.url = url;
    }
}
