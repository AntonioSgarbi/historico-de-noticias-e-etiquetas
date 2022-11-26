package tech.antoniosgarbi.desafioapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.antoniosgarbi.desafioapi.configuration.SecurityConfig;
import tech.antoniosgarbi.desafioapi.dto.*;
import tech.antoniosgarbi.desafioapi.exception.UserException;
import tech.antoniosgarbi.desafioapi.mapper.UserMapper;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.model.User;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.repository.UserCustomerRepository;
import tech.antoniosgarbi.desafioapi.repository.UserRepository;
import tech.antoniosgarbi.desafioapi.service.*;

import java.util.LinkedList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserCustomerRepository userCustomerRepository;
    private final GeneratorService generatorService;
    private final MailSpringServiceImpl mailSpringService;
    private final TagService tagService;
    private final AccessTagRegisterService accessTagRegisterService;
    private final IntegrationService integrationService;


    @Override
    public UserDTO create(UserDTO userDTO) {
        User user = UserMapper.toUser(userDTO);

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

        if (user instanceof UserCustomer u) {
            return this.accessTagRegisterService.findTagsHistoryByUser(u, pageable);
        }
        throw new UserException("Administradores não tem histórico de etiquetas");
    }

    @Override
    public Page<TagDTO> findAllTagsOrderByAccessCount(Pageable pageable) {
        return this.tagService.findAllOrderByAccessCount(pageable);
    }

    @Override
    public String sendNewsToUsers() {
        String dateToday = this.integrationService.getDateToday();

        int totalPages = this.userCustomerRepository.findAll(Pageable.unpaged()).getTotalPages();

        for (int pageNumber = 0; pageNumber < totalPages; pageNumber++) {
            Page<UserCustomer> page = this.userCustomerRepository.findAll(PageRequest.of(pageNumber, 20));
            for (UserCustomer user : page.getContent()) {
                sendMail(user, dateToday);
            }
        }
        return "e-mails enviados";
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User user = this.findById(userId);

        if (user instanceof UserCustomer u) {
            this.accessTagRegisterService.deleteHistoryFromUser(u);
        }

        this.userRepository.delete(user);
    }

    @Override
    public Page<UserDTO> listUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable).map(UserMapper::toDTO);
    }

    private void sendMail(UserCustomer user, String dateToday) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h1>Noticias de hoje</h1>\n\n");

        for (Tag tag : user.getRegisteredTags()) {
            tag.setAccessCount(tag.getAccessCount() + 1);
            this.tagService.save(tag);

            stringBuilder
                    .append("<h2>- ")
                    .append(tag.getTag())
                    .append("\n")
                    .append("</h2>");

            IntegrationDTO integrationDTO = this.integrationService.query(tag.getTag(), dateToday);

            List<NewsIntegrationDTO> newsTodayList = new LinkedList<>();

            if (integrationDTO.getList() != null) {
                for (NewsIntegrationDTO news : integrationDTO.getList()) {
                    if (news.getDate().equals(dateToday)) {
                        newsTodayList.add(news);
                    }
                }
            }

            if (!newsTodayList.isEmpty()) {
                stringBuilder.append("<ul>");
                for (NewsIntegrationDTO news : newsTodayList) {

                    stringBuilder
                            .append("<li>")
                            .append("<h3>")
                            .append("<a href=\"").append(news.getLink()).append("\">")
                            .append(news.getTitle())
                            .append("</a>")
                            .append("</h3>")
                            .append("</li>")
                    ;
                }
                stringBuilder.append("</ul>");
                stringBuilder.append("\n\n");
            } else {
                stringBuilder.append("Sem noticias dessa etiqueta hoje!\n\n");
            }
        }
        this.mailSpringService.sendText(user.getEmail(), "Notícias de hoje", stringBuilder.toString());
    }

}
