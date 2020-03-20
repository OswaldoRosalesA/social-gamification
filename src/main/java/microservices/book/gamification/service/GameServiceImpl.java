package microservices.book.gamification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final ScoreCardRepository scoreCardRepository;
    private final BadgeCardRepository badgeCardRepository;
    private final MultiplicationResultAttemptClient attemptClient;

    @Override
    public GameStats newAttemptForUser(final Long userId, final Long attemptId, final boolean correct) {

        // For the first version we'll give points only if it's correct
        if (!correct) {
            return GameStats.emptyStats(userId);
        }

        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        scoreCardRepository.save(scoreCard);

        log.info("User with id {} scored {} points for attempt id {}",
                userId, scoreCard.getScore(), attemptId);

        List<BadgeCard> badgeCards = processForBadges(userId, attemptId);

        List<Badge> badges = retrieveBadges(badgeCards);

        return new GameStats(userId, scoreCard.getScore(), badges);
    }

    /**
     * Checks the total score and the different score cards obtained
     * to give new badges in case their conditions are met.
     */
    private List<BadgeCard> processForBadges(final Long userId, final Long attemptId) {

        List<BadgeCard> badgeCards = new ArrayList<>();

        int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
        log.info("New score for user {} is {}", userId, totalScore);

        List<ScoreCard> scoreCardList = scoreCardRepository
                .findByUserIdOrderByScoreTimestampDesc(userId);

        List<BadgeCard> badgeCardList = badgeCardRepository
                .findByUserIdOrderByBadgeTimestampDesc(userId);

        // Badges depending on score
        checkAndGiveBadgeBasedOnScore(badgeCardList,
                Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId)
                .ifPresent(badgeCards::add);

        checkAndGiveBadgeBasedOnScore(badgeCardList,
                Badge.SILVER_MULTIPLICATOR, totalScore, 500, userId)
                .ifPresent(badgeCards::add);

        checkAndGiveBadgeBasedOnScore(badgeCardList,
                Badge.GOLD_MULTIPLICATOR, totalScore, 999, userId)
                .ifPresent(badgeCards::add);

        // First won badge
        if (scoreCardList.size() == 1 && !containsBadge(badgeCardList, Badge.FIRST_WON)) {
            BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
            badgeCards.add(firstWonBadge);
        }

        // Lucky number badge
        MultiplicationResultAttempt attemp = attemptClient
                .retrieveMultiplicationResultAttemptbyId(attemptId);
        if (!containsBadge(badgeCards, Badge.LUCKY_NUMBER) && isLuckyNumber(attemp)){
            BadgeCard luckyNumberBadge = giveBadgeToUser(Badge.LUCKY_NUMBER, userId);
            badgeCards.add(luckyNumberBadge);
        }

        return badgeCards;
    }

    @Override
    public GameStats retrieveStatsForUser(final Long userId) {
        int score = scoreCardRepository.getTotalScoreForUser(userId);
        List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
        List<Badge> badges = retrieveBadges(badgeCards);

        return new GameStats(userId, score, badges);
    }

    @Override
    public ScoreCard getScoreForAttempt(long attemptId) {
        return scoreCardRepository.findScoreCardByAttemptId(attemptId);
    }

    /**
     * Convenience method to check the current score against
     * the different thresholds to gain badges.
     * It also assigns badge to user if the conditions are met.
     */
    private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(
            final List<BadgeCard> badgeCards, final Badge badge,
            final int score, final int scoreThreshold, final Long userId) {

        if (score >= scoreThreshold && !containsBadge(badgeCards, badge)) {
            return Optional.of(giveBadgeToUser(badge, userId));
        }

        return Optional.empty();
    }

    /**
     * Checks if the passed list of badges includes the one being checked
     */
    private boolean containsBadge(final List<BadgeCard> badgeCards, final Badge badge) {
        return badgeCards.stream()
                .anyMatch(badgeCard -> badgeCard.getBadge().equals(badge));
    }

    /**
     * Assigns a new badge to the given user
     */
    private BadgeCard giveBadgeToUser(final Badge badge, final Long userId) {
        BadgeCard badgeCard = new BadgeCard(userId, badge);
        badgeCardRepository.save(badgeCard);
        log.info("User with id {} won a new badge: {}", userId, badge);
        return badgeCard;
    }

    private List<Badge> retrieveBadges(List<BadgeCard> badgeCards) {
        return badgeCards.stream()
                .map(BadgeCard::getBadge)
                .collect(Collectors.toList());
    }

    private boolean isLuckyNumber(MultiplicationResultAttempt attempt){
        return attempt.getMultiplicationFactorA() == 42
                || attempt.getMultiplicationFactorB() == 42;
    }

}
