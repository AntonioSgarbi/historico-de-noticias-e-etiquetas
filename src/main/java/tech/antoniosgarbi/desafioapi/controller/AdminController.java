package tech.antoniosgarbi.desafioapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.dto.TagDTO;
import tech.antoniosgarbi.desafioapi.dto.UserDTO;
import tech.antoniosgarbi.desafioapi.service.AdminService;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PostMapping("/user/new")
    @Operation(summary = "Create new User")
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(this.adminService.create(userDTO));
    }

    @GetMapping("/users")
    @Operation(summary = "List all users")
    public ResponseEntity<Page<UserDTO>> list(Pageable pageable) {
        return ResponseEntity.ok(this.adminService.listUsers(pageable));
    }

    @DeleteMapping("/user/{id}")
    @Operation(summary = "Delete User")
    public ResponseEntity<Void> create(@PathVariable Long id) {
        this.adminService.delete(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/user/tags")
    @Operation(summary = "Lists the history of tags accessed by the user")
    public ResponseEntity<Page<AccessTagRegisterDTO>> findTagsHistory(@RequestParam Long id, Pageable pageable) {
        return ResponseEntity.ok(this.adminService.findTagsHistoryByUser(id, pageable));
    }

    @PutMapping("/users/email")
    @Operation(summary = "Sends an email to each user with the news of the day of their registered tags")
    public ResponseEntity<String> sendNewsToUsers() {
        return ResponseEntity.ok(this.adminService.sendNewsToUsers());
    }

    @GetMapping("/tag/popular")
    @Operation(summary = "Lists tags sorted by hit count")
    public ResponseEntity<Page<TagDTO>> findTags(Pageable pageable) {
        return ResponseEntity.ok(this.adminService.findAllTagsOrderByAccessCount(pageable));
    }

}
