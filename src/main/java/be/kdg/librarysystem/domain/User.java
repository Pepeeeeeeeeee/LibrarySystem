package be.kdg.librarysystem.domain;

import be.kdg.librarysystem.domain.enums.Flags;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Flags flags;

    private String token;

    /**
     * LEGACY constructor — DO NOT USE for new code.
     */
    @Deprecated
    public User(
            int id,
            int memberId,
            String username,
            String password,
            Flags flags,
            String token
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.flags = flags;
        this.token = token;
    }
}
