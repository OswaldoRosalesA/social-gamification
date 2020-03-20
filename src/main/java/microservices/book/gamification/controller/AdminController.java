package microservices.book.gamification.controller;

import lombok.RequiredArgsConstructor;
import microservices.book.gamification.service.AdminService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
@RequiredArgsConstructor
@RequestMapping(path = "/gamification/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping(path = "/delete-db")
    public ResponseEntity deleteDatabase(){
        adminService.deleteDatabaseContents();
        return ResponseEntity.ok().build();
    }

}
