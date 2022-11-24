package tech.antoniosgarbi.desafioapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.model.AccessTagRegister;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;


public interface AccessTagRegisterService {

    Page<AccessTagRegisterDTO> findTagsHistoryByUser(UserCustomer user, Pageable pageable);

    void save(AccessTagRegister accessTagRegister);

}
