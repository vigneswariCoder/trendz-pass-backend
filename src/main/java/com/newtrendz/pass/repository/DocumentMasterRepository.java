package com.newtrendz.pass.repository;

import com.newtrendz.pass.entity.DocumentMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentMasterRepository extends MongoRepository<DocumentMaster,String> {
    @Query(value = "{ 'uploadedBy': ?0, 'documentFor': ?1,'status': ?2 }")
    Optional<DocumentMaster> findByUploadedByAndDocumentForAndStatus(String uploadedBy,String documentFor,int status);
}
