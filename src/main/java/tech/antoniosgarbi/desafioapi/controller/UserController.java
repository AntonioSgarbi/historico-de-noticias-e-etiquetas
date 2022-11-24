package tech.antoniosgarbi.desafioapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.dto.NewsDTO;
import tech.antoniosgarbi.desafioapi.service.CustomerService;

import java.security.Principal;
import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final CustomerService customerService;


    public UserController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<NewsDTO>> query(Principal principal, @RequestParam String query, @RequestParam(required = false) String date) {
        return ResponseEntity.ok(this.customerService.query(query, date, principal));
    }

    @GetMapping("/history")
    public ResponseEntity<Page<AccessTagRegisterDTO>> history(Principal principal, Pageable pageable) {
        return ResponseEntity.ok(this.customerService.getTagsHistory(principal, pageable));
    }

    @PostMapping
    public ResponseEntity<String> addTag(Principal principal, @RequestParam String tag) {
        return ResponseEntity.ok(this.customerService.addTag(principal, tag));
    }

    @DeleteMapping
    public ResponseEntity<String> removeTag(Principal principal, @RequestParam String tag) {
        return ResponseEntity.ok(this.customerService.removeTag(principal, tag));
    }

}
