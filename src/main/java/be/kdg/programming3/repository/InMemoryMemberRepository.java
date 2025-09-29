package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Member;
import be.kdg.programming3.repository.interfaces.DataRepository;
import be.kdg.programming3.repository.interfaces.MemberRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class InMemoryMemberRepository implements MemberRepository {

    private final DataRepository dataRepository;

    public InMemoryMemberRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<Member> findAll() {
        return dataRepository.findAllMembers();
    }

    @Override
    public void save(Member member) {
        dataRepository.findAllMembers().add(member);
    }
}