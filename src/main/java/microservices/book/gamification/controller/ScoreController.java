package microservices.book.gamification.controller;

import lombok.RequiredArgsConstructor;
import microservices.book.gamification.domain.ScoreCard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import microservices.book.gamification.service.GameService;

/**
 * This class implements a REST API for the Gamification User Statistics service.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/scores")
public class ScoreController {

    private final GameService gameService;

    @GetMapping(path = "/{attemptId}")
    public ScoreCard getScoreForAttempt(@PathVariable("attemptId") final long attemptId){
        return gameService.getScoreForAttempt(attemptId);
    }
}
