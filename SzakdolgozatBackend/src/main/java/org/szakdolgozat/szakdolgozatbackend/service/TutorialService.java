package org.szakdolgozat.szakdolgozatbackend.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.FileNotFoundException;

@Service
public class TutorialService {

    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    @Autowired
    public TutorialService(GridFsTemplate gridFsTemplate, GridFsOperations gridFsOperations) {
        this.gridFsTemplate = gridFsTemplate;
        this.gridFsOperations = gridFsOperations;
    }

    public List<String> getAllFiles() {
        List<GridFSFile> files = new ArrayList<>();
        gridFsTemplate.find(new Query()).forEach(files::add);

        return files.stream()
                .map(GridFSFile::getFilename)
                .collect(Collectors.toList());
    }

    public GridFSFile getFileByName(String fileName) {
        return gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileName)));
    }

    public InputStream downloadFile(String fileName) throws IOException {
        GridFSFile file = getFileByName(fileName);
        if (file != null) {
            GridFsResource resource = gridFsOperations.getResource(file);
            return resource.getInputStream();
        }
        throw new FileNotFoundException("File not found");
    }
}
