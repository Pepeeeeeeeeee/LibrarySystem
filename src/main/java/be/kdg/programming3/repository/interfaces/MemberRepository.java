package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.Member;
import java.util.List;

public interface MemberRepository {
    List<Member> findAll();
    void save(Member member);
}
