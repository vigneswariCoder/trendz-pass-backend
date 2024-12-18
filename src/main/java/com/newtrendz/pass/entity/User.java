package com.newtrendz.pass.entity;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @Field("email")
    @Email
    @Indexed(unique = true)
    private String email;

    private String password;

    @Field("phone_number")
    @Indexed(unique = true)
    private long phoneNumber;


    @Field(name = "status")
    private int status;

    @Field(name = "deleted_status")
    private int deletedStatus;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;

}
