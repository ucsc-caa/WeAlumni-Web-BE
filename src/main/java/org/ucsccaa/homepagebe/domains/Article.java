package org.ucsccaa.homepagebe.domains;

import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String category;
    private String title;
    private String author;
    private String brief;
    private String content;
    private String cover;
    private String timestamp;
    @ElementCollection
    private List<String> resources;
}

