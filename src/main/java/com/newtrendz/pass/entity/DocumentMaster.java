package com.newtrendz.pass.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "document_masster")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentMaster {
    @Id
    private String id;

    @Field(name = "document_for")
    private String documentFor;

    @Field(name = "document_name")
    private String documentName;

    @Field(name = "document_extension")
    private String documentExtension;

    @Field(name = "document_path")
    private String documentPath;

    @Field(name = "uploaded_by")
    private String uploadedBy;

    @Field(name = "status")
    private int status;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;
}
