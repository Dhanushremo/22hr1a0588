package com.fullstack.Backend.Service;

import com.fullstack.Backend.Model.Clicks;
import com.fullstack.Backend.Model.Url;
import com.fullstack.Backend.Repository.ClicksRepository;
import com.fullstack.Backend.Repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UrlService {
    @Autowired
    UrlRepository urlRepository;

    @Autowired
    ClicksRepository clicksRepository;


    public Url create(String originalUrl, Integer validity, String customCode) {
        String code = customCode != null ? customCode : UUID.randomUUID().toString().substring(0, 6);
        if (urlRepository.existsByShortCode(code)) {
            log("backend", "error", "repository", "Shortcode already exists: " + code);
            throw new IllegalArgumentException("Short code already exists");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = now.plusMinutes(validity != null ? validity : 30);
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(code);
        url.setCreatedAt(now);
        url.setExpiryAt(expiry);

        try {
            Url saved = urlRepository.save(url);
            log("backend", "info", "repository", "Short URL successfully saved: " + code);
            return saved;
        } catch (Exception e) {
            log("backend", "error", "repository", "Save failed: " + e.getMessage());
            throw e;
        }
    }

    public Map<String, Object> getStatistics(String code) {
        Url url = urlRepository.findByShortCode(code).orElseThrow(() -> new NoSuchElementException("Code not found"));
        List<Clicks> clicks = clicksRepository.findByUrl(url);
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("originalUrl", url.getOriginalUrl());
        stats.put("createdAt", url.getCreatedAt());
        stats.put("expiryAt", url.getExpiryAt());
        stats.put("totalClicks", clicks.size());
        List<Map<String, Object>> clickLogs = clicks.stream().map(click -> {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("timestamp", click.getTimestamp());
            entry.put("referrer", click.getRef());
            entry.put("location", click.getLocation());
            return entry;
        }).collect(Collectors.toList());
        stats.put("clickDetails", clickLogs);
        return stats;
    }
    public String handleRedirect(String code, String referrer, String location) {
        Url url = urlRepository.findByShortCode(code)
                .orElseThrow(() -> {
                    log("backend", "error", "repository", "Redirect attempt failed:shortcode not found" +code);
                    return new NoSuchElementException("Code not found");
                });
        if (LocalDateTime.now().isAfter(url.getExpiryAt())) {
            log("backend", "warn", "handler", "Redirect blocked:link expired for shortcode" +code);
            throw new IllegalStateException("Link expired");
        }
        Clicks click = new Clicks();
        click.setUrl(url);
        click.setRef(referrer);
        click.setLocation(location);
        click.setTimestamp(LocalDateTime.now());
        clicksRepository.save(click);
        log("backend", "info", "service", "Redirect successful → " + code + " sent to: " + url.getOriginalUrl());
        return url.getOriginalUrl();
    }
    public void log(String stack, String level, String pkg, String message) {
        try {
            String cmd = String.format("node ../middlewareLog/log.js %s %s %s \"%s\"", stack, level, pkg, message);
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) { }
    }
}
