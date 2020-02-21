package microservices.book.gamification.client;

import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {

    private final RestTemplate restTemplate;
    private final String multiplicationHost;

    public MultiplicationResultAttemptClientImpl(final RestTemplate restTemplate,
                                                 @Value("${multiplicationHost}") final String multiplicationHost) {
        this.restTemplate = restTemplate;
        this.multiplicationHost = multiplicationHost;
    }

    @Override
    public MultiplicationResultAttempt retrieveMultiplicationResultAttemptbyId(Long multiplicationId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(multiplicationHost);
        stringBuilder.append("/results/");
        stringBuilder.append(multiplicationId);
        return restTemplate.getForObject(stringBuilder.toString(), MultiplicationResultAttempt.class);
    }
}
