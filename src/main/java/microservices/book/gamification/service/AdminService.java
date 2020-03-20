package microservices.book.gamification.service;

import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    void deleteDatabaseContents();

}
