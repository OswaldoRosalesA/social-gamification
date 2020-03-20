package microservices.book.gamification.service;

import lombok.RequiredArgsConstructor;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final BadgeCardRepository badgeCardRepository;
    private final ScoreCardRepository scoreCardRepository;

    @Override
    public void deleteDatabaseContents() {
        badgeCardRepository.deleteAll();
        scoreCardRepository.deleteAll();
    }
}
