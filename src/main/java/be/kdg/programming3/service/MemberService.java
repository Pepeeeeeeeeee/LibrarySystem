package be.kdg.programming3.service;

import be.kdg.programming3.domain.Member;
import be.kdg.programming3.repository.interfaces.DataRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemberService {
    private final DataRepository repo;

    public MemberService(DataRepository repo) {
        this.repo = repo;
    }

    public List<Member> getAllMembers() {
        return repo.findAllMembers();
    }

    public void addMember(Member member) {
        repo.findAllMembers().add(member);
    }
}
