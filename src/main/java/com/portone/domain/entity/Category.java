package com.portone.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Category {
    @Column(name = "category_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(max = 100)
    @Column(unique = true)
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
