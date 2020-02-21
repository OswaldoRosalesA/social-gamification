package microservices.book.gamification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This class links a Badge to a User. Contains also a timestamp with the moment in which the user got it.
 */

@Entity
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class BadgeCard {

    @Id
    @GeneratedValue
    @Column(name = "BADGE_ID")
    private final Long badgeId;

    @Column(name = "USER_ID")
    private final Long userId;

    @Column(name = "BADGE_TS")
    private final long badgeTimestamp;

    @Column(name = "BADGE")
    private final Badge badge;

    // Empty constructor for JSON / JPA
    public BadgeCard() {
        this(null, null, 0, null);
    }

    public BadgeCard(final Long userId, final Badge badge) {
        this(null, userId, System.currentTimeMillis(), badge);
    }

}
