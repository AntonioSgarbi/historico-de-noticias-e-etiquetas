package tech.antoniosgarbi.desafioapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.dto.TagDTO;
import tech.antoniosgarbi.desafioapi.dto.UserDTO;
import tech.antoniosgarbi.desafioapi.model.User;


public interface AdminService {

    UserDTO create(UserDTO userDTO);

    User findById(Long id);

    Page<AccessTagRegisterDTO> findTagsHistoryByUser(Long id, Pageable pageable);

    Page<TagDTO> findAllTagsOrderByAccessCount(Pageable pageable);

    String sendNewsToUsers();

    void delete(Long userId);

    Page<UserDTO> listUsers(Pageable pageable);

}
