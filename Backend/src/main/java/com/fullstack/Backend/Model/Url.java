package com.fullstack.Backend.Model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="url")
public class Url {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="originalurl")
    private String originalUrl;
    @Column(name="shortcode")
    private String shortCode;
    @Column(name="createdat")
    private LocalDateTime createdAt;
    @Column(name="expiryat")
    private LocalDateTime expiryAt;
    @OneToMany(mappedBy="url", cascade=CascadeType.ALL)
    private List<Clicks> clicks=new ArrayList<Clicks>();

    public Url(Long id, String originalUrl, String shortCode, LocalDateTime createdAt, LocalDateTime expiryAt, List<Clicks> clicks) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.expiryAt = expiryAt;
        this.clicks = clicks;
    }
    public Url( String originalUrl, String shortCode, LocalDateTime createdAt, LocalDateTime expiryAt, List<Clicks> clicks) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.expiryAt = expiryAt;
        this.clicks = clicks;
    }

    public Url() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(LocalDateTime expiryAt) {
        this.expiryAt = expiryAt;
    }

    public List<Clicks> getClicks() {
        return clicks;
    }

    public void setClicks(List<Clicks> clicks) {
        this.clicks = clicks;
    }
}
