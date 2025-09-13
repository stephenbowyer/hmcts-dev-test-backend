package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class APIController {

    @GetMapping("/api")
    public ResponseEntity<String> apiList() {
        try {
            String content = new String(Files.readAllBytes(
                Paths.get("src/main/endpoints.json")));
            return ok(content);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to read endpoints.json");
        }
    }
}
