package com.alok.localstackdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("rawtypes")
@RestController("/")
public class WebController {
    @Autowired
    private ModelRepository modelRepository;

    @GetMapping
    public ResponseEntity get() {
        return ResponseEntity.ok(modelRepository.findAll());
    }

    @PostMapping
    public ResponseEntity save(@RequestBody Model model) {
        return ResponseEntity.ok(modelRepository.save(model));
    }
}

