package tech.antoniosgarbi.desafioapi.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafioapi.configuration.SecurityConfig;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.dto.TagDTO;
import tech.antoniosgarbi.desafioapi.dto.UserDTO;
import tech.antoniosgarbi.desafioapi.exception.UserException;
import tech.antoniosgarbi.desafioapi.mapper.UserModelMapper;
import tech.antoniosgarbi.desafioapi.model.User;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.repository.UserRepository;
import tech.antoniosgarbi.desafioapi.service.AccessTagRegisterService;
import tech.antoniosgarbi.desafioapi.service.AdminService;
import tech.antoniosgarbi.desafioapi.service.GeneratorService;
import tech.antoniosgarbi.desafioapi.service.TagService;


@Service
//@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final GeneratorService generatorService;
    private final MailSpringServiceImpl mailSpringService;
    private final TagService tagService;
    private final AccessTagRegisterService accessTagRegisterService;

    public AdminServiceImpl(UserRepository userRepository,
                            GeneratorService generatorService, MailSpringServiceImpl mailSpringService,
                            TagService tagService, AccessTagRegisterService accessTagRegisterService) {
        this.userRepository = userRepository;
        this.generatorService = generatorService;
        this.mailSpringService = mailSpringService;
        this.tagService = tagService;
        this.accessTagRegisterService = accessTagRegisterService;

        if(this.userRepository.findAll().isEmpty()) {
            String pass = "senha12";

            String passE = SecurityConfig.passwordEncoder().encode(pass);

            User user = new User(null, "admin", passE,"ADMIN");

            UserCustomer user1 = new UserCustomer();
            user1.setEmail("user");
            user1.setRole("USER");
            user1.setPassword(passE);

            this.userRepository.save(user);
            this.userRepository.save(user1);

            System.out.println("\n\n\n banco preenchido \n\n\n");
        }

    }


    @Override
    public UserDTO create(UserDTO userDTO) {
        User user = UserModelMapper.INSTANCE.toUser(userDTO);

        final String password = this.generatorService.generate((byte) 16);

        final String encodedPassword = SecurityConfig.passwordEncoder().encode(password);

        user.setPassword(encodedPassword);

        user = this.userRepository.save(user);

        mailSpringService.sendText(userDTO.getEmail(), "Senha da api",
                String.format("Suas credenciais%n%nLogin: %s%nSenha: %s", user.getEmail(), password));


        userDTO.setId(user.getId());

        return userDTO;
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Este id não existe"));
    }

    @Override
    public Page<AccessTagRegisterDTO> findTagsHistoryByUser(Long id, Pageable pageable) {
        User user = this.findById(id);

        if(user instanceof UserCustomer u) {
            return this.accessTagRegisterService.findTagsHistoryByUser(u, pageable);
        }
        throw new UserException("usuarios administradores não tem histórico de etiquetas");
    }

    @Override
    public Page<TagDTO> findAllTagsOrderByAccessCount(Pageable pageable) {
        return this.tagService.findAllOrderByAccessCount(pageable);
    }

}
