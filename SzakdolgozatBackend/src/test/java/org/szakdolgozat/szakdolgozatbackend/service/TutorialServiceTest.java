package org.szakdolgozat.szakdolgozatbackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

class TutorialServiceTest {

    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private GridFsOperations gridFsOperations;

    private TutorialService tutorialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tutorialService = new TutorialService(gridFsTemplate, gridFsOperations);
    }

    @Test
    void testGetAllFiles() {
        GridFSFile file1 = mock(GridFSFile.class);
        GridFSFile file2 = mock(GridFSFile.class);
        when(file1.getFilename()).thenReturn("file1.jpg");
        when(file2.getFilename()).thenReturn("file2.jpg");
        List<GridFSFile> files = Arrays.asList(file1, file2);

        GridFSFindIterable findIterable = mock(GridFSFindIterable.class);
        when(gridFsTemplate.find(any(Query.class))).thenReturn(findIterable);

        doAnswer(invocation -> {
            Consumer<GridFSFile> consumer = invocation.getArgument(0);
            files.forEach(consumer);
            return null;
        }).when(findIterable).forEach(any());

        List<String> fileNames = tutorialService.getAllFiles();

        assertEquals(2, fileNames.size());
        assertTrue(fileNames.contains("file1.jpg"));
        assertTrue(fileNames.contains("file2.jpg"));
        verify(gridFsTemplate, times(1)).find(any(Query.class));
    }

    @Test
    void testGetFileByName_FileExists() {
        GridFSFile file = mock(GridFSFile.class);
        when(gridFsTemplate.findOne(any(Query.class))).thenReturn(file);

        GridFSFile result = tutorialService.getFileByName("file1.jpg");

        assertNotNull(result);
        verify(gridFsTemplate, times(1)).findOne(any(Query.class));
    }

    @Test
    void testGetFileByName_FileDoesNotExist() {
        when(gridFsTemplate.findOne(any(Query.class))).thenReturn(null);

        GridFSFile result = tutorialService.getFileByName("nonexistent.jpg");

        assertNull(result);
        verify(gridFsTemplate, times(1)).findOne(any(Query.class));
    }

    @Test
    void testDownloadFile_FileExists() throws IOException {
        GridFSFile file = mock(GridFSFile.class);
        GridFsResource resource = mock(GridFsResource.class);
        InputStream inputStream = mock(InputStream.class);

        when(gridFsTemplate.findOne(any(Query.class))).thenReturn(file);
        when(gridFsOperations.getResource(eq(file))).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(inputStream);

        InputStream result = tutorialService.downloadFile("file1.jpg");

        assertNotNull(result);
        verify(gridFsTemplate, times(1)).findOne(any(Query.class));
        verify(gridFsOperations, times(1)).getResource(eq(file));
        verify(resource, times(1)).getInputStream();
    }

    @Test
    void testDownloadFile_FileNotFound() {
        when(gridFsTemplate.findOne(any(Query.class))).thenReturn(null);

        assertThrows(FileNotFoundException.class, () -> {
            tutorialService.downloadFile("nonexistent.jpg");
        });
        verify(gridFsTemplate, times(1)).findOne(any(Query.class));
        verifyNoMoreInteractions(gridFsOperations);
    }
}
