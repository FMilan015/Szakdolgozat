package org.szakdolgozat.szakdolgozatbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.szakdolgozat.szakdolgozatbackend.service.TutorialService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/tutorials")
public class TutorialController {

    private final TutorialService tutorialService;

    @Autowired
    public TutorialController(TutorialService tutorialService) {
        this.tutorialService = tutorialService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllImages() {
        List<String> fileNames = tutorialService.getAllFiles();
        return ResponseEntity.ok(fileNames);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) throws IOException {
        InputStream imageStream = tutorialService.downloadFile(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(imageStream));
    }
}
