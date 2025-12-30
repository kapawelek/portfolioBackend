package org.springframework.samples.petclinic.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.service.DatabaseResetService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ResetDbRestController {

    private final DatabaseResetService resetService;

    public ResetDbRestController(DatabaseResetService resetService) {
        this.resetService = resetService;
    }

    @PostMapping("/resetdb")
    public ResponseEntity<Void> resetDatabase() {
        resetService.reset();
        return ResponseEntity.ok().build();
    }
}
