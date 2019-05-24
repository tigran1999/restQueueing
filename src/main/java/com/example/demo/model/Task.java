package com.example.demo.model;

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
    @GeneratedValue
    private int id;

    private Date createdDate;

    private Date finishedDate;

    private boolean downloaded;

    private String fileUrl;

}
