package org.szakdolgozat.szakdolgozatbackend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.szakdolgozat.szakdolgozatbackend.service.TutorialService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

class TutorialControllerTest {

    @Mock
    private TutorialService tutorialService;

    @InjectMocks
    private TutorialController tutorialController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllImages() {
        List<String> fileNames = Arrays.asList("file1.jpg", "file2.jpg");
        when(tutorialService.getAllFiles()).thenReturn(fileNames);

        ResponseEntity<List<String>> response = tutorialController.getAllImages();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileNames, response.getBody());
        verify(tutorialService, times(1)).getAllFiles();
    }

    @Test
    void testDownloadImage_FileExists() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(tutorialService.downloadFile("file1.jpg")).thenReturn(inputStream);

        ResponseEntity<Resource> response = tutorialController.downloadImage("file1.jpg");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
        assertEquals("attachment; filename=\"file1.jpg\"",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        verify(tutorialService, times(1)).downloadFile("file1.jpg");
    }

    @Test
    void testDownloadImage_FileNotFound() throws IOException {
        when(tutorialService.downloadFile("nonexistent.jpg")).thenThrow(new FileNotFoundException("File not found"));

        assertThrows(FileNotFoundException.class, () -> {
            tutorialController.downloadImage("nonexistent.jpg");
        });
        verify(tutorialService, times(1)).downloadFile("nonexistent.jpg");
    }
}

