package tech.antoniosgarbi.desafioapi.controller;

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
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(this.adminService.create(userDTO));
    }

    @GetMapping("/user/tags")
    public ResponseEntity<Page<AccessTagRegisterDTO>> findTagsHistory(@RequestParam Long id, Pageable pageable) {
        return ResponseEntity.ok(this.adminService.findTagsHistoryByUser(id, pageable));
    }

    @GetMapping("/tag/popular")
    public ResponseEntity<Page<TagDTO>> findTags(Pageable pageable) {
        return ResponseEntity.ok(this.adminService.findAllTagsOrderByAccessCount(pageable));
    }

}
