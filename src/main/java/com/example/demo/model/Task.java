package com.example.demo.model;

import com.example.demo.model.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "finishedDate")
    private Date finishedDate;

    @Column
    private boolean downloaded;

    @Column
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

}
