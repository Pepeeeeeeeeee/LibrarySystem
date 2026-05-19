package be.kdg.librarysystem.domain;

import be.kdg.librarysystem.domain.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application_users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(unique = true)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;


    /**
     * LEGACY constructor — DO NOT USE for new code.
     */
    @Deprecated
    public User(
            int id,
            int memberId,
            String username,
            String password,
            Roles role
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}