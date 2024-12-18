package com.newtrendz.pass.service;

import com.newtrendz.pass.entity.DocumentMaster;
import com.newtrendz.pass.repository.DocumentMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class DocumentMasterService {
    @Autowired
    private DocumentMasterRepository documentMasterRepository;

    public List<DocumentMaster> addDocuments(List<MultipartFile> files, String  userId, String documentFor) {

        List<DocumentMaster> documents = new ArrayList<>();

        if (Objects.nonNull(files) && !files.isEmpty()) {

            for (MultipartFile file : files) {
                System.out.println("file name : " + file.getOriginalFilename());
                String documentName = file.getName();

                    DocumentMaster fileUpload = this.documentMasterRepository.findByUploadedByAndDocumentForAndStatus(userId, documentFor,1).orElse(new DocumentMaster());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    String formattedDate = dateFormat.format(new Date());
                    String originalFileName = file.getOriginalFilename();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String fileName = formattedDate + '_' + UUID.randomUUID().toString() + '_' + originalFileName;
                    fileUpload.setDocumentFor(documentFor);
                    fileUpload.setDocumentName(fileName);
                    fileUpload.setDocumentExtension(fileExtension);
                    fileUpload.setDocumentPath("uploads" + "/" + userId + "/" + fileName);
                    fileUpload.setUploadedBy(userId);
                    fileUpload.setStatus(1);
                    fileUpload.setCreatedAt(LocalDateTime.now());
                    documents.add(documentMasterRepository.save(fileUpload));

                    try {
                        String folderSuffix = "profile".equalsIgnoreCase(documentFor) ? "_profile" : "";
                        String savedFileName = "uploads" + File.separator + userId + folderSuffix ;

//                        String savedFileName = "uploads" + File.separator + userId;
                        Path uploadPath = Paths.get(savedFileName);
                        if (!Files.exists(uploadPath, new LinkOption[0])) {
                            Files.createDirectories(uploadPath);
                        }

                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(file.getInputStream(), filePath, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.warn(e.getMessage());
                    }

            }

        } else {
            log.info("File is Empty");
        }

        return documents;
    }
}
