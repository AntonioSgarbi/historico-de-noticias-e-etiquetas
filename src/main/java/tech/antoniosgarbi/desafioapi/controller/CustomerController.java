package tech.antoniosgarbi.desafioapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping
    @Operation(summary = "Lists all news based on user search")
    public ResponseEntity<List<NewsDTO>> query(Principal principal, @RequestParam String query, @RequestParam(required = false) String date) {
        return ResponseEntity.ok(this.customerService.query(query, date, principal));
    }

    @GetMapping("/history")
    @Operation(summary = "Lists the history of tags searched by the user")
    public ResponseEntity<Page<AccessTagRegisterDTO>> history(Principal principal, Pageable pageable) {
        return ResponseEntity.ok(this.customerService.getTagsHistory(principal, pageable));
    }

    @GetMapping("/today")
    @Operation(summary = "Lists all the news of the day for the user's registered tags")
    public ResponseEntity<List<NewsDTO>> todayNews(Principal principal, Pageable pageable) {
        return ResponseEntity.ok(this.customerService.getTodayNews(principal));
    }

    @PostMapping
    @Operation(summary = "Adds the given tag to the user's tag list")
    public ResponseEntity<String> addTag(Principal principal, @RequestParam String tag) {
        return ResponseEntity.ok(this.customerService.addTag(principal, tag));
    }

    @DeleteMapping
    @Operation(summary = "Removes the passed tag from the user's tag list")
    public ResponseEntity<String> removeTag(Principal principal, @RequestParam String tag) {
        return ResponseEntity.ok(this.customerService.removeTag(principal, tag));
    }

}
