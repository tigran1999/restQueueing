package com.example.demo.model;

import com.example.demo.model.enums.TaskType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Builder
public class Task {

    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id = UUID.randomUUID();

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "finishedDate")
    private Date finishedDate;

    @Column
    private boolean downloaded;

}
