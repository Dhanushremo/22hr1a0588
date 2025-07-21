package com.fullstack.Backend.Controller;

import com.fullstack.Backend.Model.Url;
import com.fullstack.Backend.Service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("api/url")
@CrossOrigin(origins = "http://localhost:3000")
public class UrlController {
    @Autowired
    private UrlService urlService;
    @PostMapping("/shorturls")
    public ResponseEntity<?> createShortUrl(@RequestBody Map<String, Object> payload) {
        try {
            String longUrl = payload.get("url") != null ? payload.get("url").toString().trim() : null;
            String validityStr = payload.getOrDefault("validity", "30").toString().trim();
            String customCode = payload.get("shortcode") != null ? payload.get("shortcode").toString().trim() : null;

            if (longUrl == null || longUrl.isEmpty() || !longUrl.startsWith("http")) {
                urlService.log("backend", "warn", "controller", "Invalid URL format submitted");
                return ResponseEntity.badRequest().body(Map.of("error", "URL must start with http or https"));
            }
            int validity;
            try {
                validity = Integer.parseInt(validityStr);
            } catch (NumberFormatException e) {
                validity = 30;
                urlService.log("backend", "warn", "controller", "Invalid validity provided, defaulting to 30");
            }
            Url created = urlService.create(longUrl, validity, customCode);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("shortLink", "http://localhost:8080/" + created.getShortCode());
            response.put("expiry", created.getExpiryAt().toString());
            urlService.log("backend", "info", "controller", "Short URL generated: " + created.getShortCode());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (Exception e) {
            urlService.log("backend", "error", "controller", "URL creation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> fetchStatistics(@PathVariable String code) {
        try {
            Map<String, Object> stats = urlService.getStatistics(code);
            urlService.log("backend", "info", "controller", "Statistics fetched for: " + code);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            urlService.log("backend", "error", "controller", "Stats retrieval failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/{code}/redirect")
    public void redirectToOriginal(@PathVariable String code,
                                   @RequestHeader(value = "Referer", required = false) String referrer,
                                   HttpServletResponse response) throws IOException {
        try {
            String location = "IN"; // Optional — enhance with geo IP logic
            String originalUrl = urlService.handleRedirect(code, referrer, location);

            urlService.log("backend", "info", "controller", "Redirect issued for shortcode: " + code);
            response.sendRedirect(originalUrl);

        } catch (Exception e) {
            urlService.log("backend", "error", "controller", "Redirect failed for: " + code + " → " + e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
