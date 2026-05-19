package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.repository.interfaces.CheckoutRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookCheckoutJpaRepository extends JpaRepository<BookCheckout, Integer>, CheckoutRepository {
    @Override
    @Query("""
        SELECT bc
        FROM BookCheckout bc
        LEFT JOIN FETCH bc.handledByLibrarian
        LEFT JOIN FETCH bc.member
        WHERE bc.book.id = :bookId
    """)
    Optional<BookCheckout> findByBookId(@Param("bookId") int bookId);


    @Override
    List<BookCheckout> findByHandledByLibrarianId(int librarianId);

    @Query("""
    SELECT bc
    FROM BookCheckout bc
    JOIN FETCH bc.book
    WHERE bc.member.id = :memberId
    """)
    List<BookCheckout> findByMemberId(
            @Param("memberId") int memberId
    );
}
