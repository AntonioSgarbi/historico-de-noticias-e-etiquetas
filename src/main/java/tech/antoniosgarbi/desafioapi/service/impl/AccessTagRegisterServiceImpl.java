package tech.antoniosgarbi.desafioapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.mapper.AccessTagRegisterMapper;
import tech.antoniosgarbi.desafioapi.model.AccessTagRegister;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.repository.AccessTagRepository;
import tech.antoniosgarbi.desafioapi.service.AccessTagRegisterService;


@Service
@RequiredArgsConstructor
public class AccessTagRegisterServiceImpl implements AccessTagRegisterService {

    private final AccessTagRepository accessTagRepository;


    @Override
    public Page<AccessTagRegisterDTO> findTagsHistoryByUser(UserCustomer user, Pageable pageable) {
        Page<AccessTagRegister> pageAccessTagRegister = this.accessTagRepository.findAllByUser(user, pageable);

        AccessTagRegisterMapper mapper = AccessTagRegisterMapper.INSTANCE;

        return pageAccessTagRegister.map(mapper::toDTO);
    }

    @Override
    public void save(AccessTagRegister accessTagRegister) {
        this.accessTagRepository.save(accessTagRegister);
    }

}
