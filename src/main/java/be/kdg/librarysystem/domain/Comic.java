package be.kdg.librarysystem.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("COMIC")
@Data
@NoArgsConstructor
public class Comic extends Book {

    @Column(nullable = true)
    private String artist;
}
