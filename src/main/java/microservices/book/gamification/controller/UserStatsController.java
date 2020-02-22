package microservices.book.gamification.controller;

import lombok.RequiredArgsConstructor;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/stats")
public class UserStatsController {

    private final GameService gameService;

    @GetMapping
    public GameStats getStatsForUser(@RequestParam("userId") final Long userId) {
        return gameService.retrieveStatsForUser(userId);
    }

}
