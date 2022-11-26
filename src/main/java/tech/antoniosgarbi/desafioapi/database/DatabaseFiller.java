package tech.antoniosgarbi.desafioapi.database;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tech.antoniosgarbi.desafioapi.model.AccessTagRegister;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.model.User;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.repository.AccessTagRepository;
import tech.antoniosgarbi.desafioapi.repository.TagRepository;
import tech.antoniosgarbi.desafioapi.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseFiller {

    @Value("${generate.admin.email}")
    private String adminEmail;
    @Value("${generate.admin.password}")
    private String adminPassword;
    @Value("${generate.user.email}")
    private String userEmail;
    @Value("${generate.user.password}")
    private String userPassword;

    private final PasswordEncoder passwordEncoder;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final AccessTagRepository accessTagRepository;
    private static final Logger loggerInstance = LoggerFactory.getLogger(DatabaseFiller.class);


    @PostConstruct
    private void init() {
        if (this.userRepository.findAll().isEmpty()) {
            this.run();
        }
    }

    private void run() {
        Tag tag1 = this.tagRepository.save(Tag.builder().tag("floripa").accessCount(10L).build());
        Tag tag2 = this.tagRepository.save(Tag.builder().tag("tecnologia").accessCount(20L).build());
        Tag tag3 = this.tagRepository.save(Tag.builder().tag("agricultura").accessCount(30L).build());
        Tag tag4 = this.tagRepository.save(Tag.builder().tag("medicina").accessCount(40L).build());
        Tag tag5 = this.tagRepository.save(Tag.builder().tag("politica").accessCount(50L).build());
        Tag tag6 = this.tagRepository.save(Tag.builder().tag("esportes").accessCount(60L).build());
        Tag tag7 = this.tagRepository.save(Tag.builder().tag("festas").accessCount(70L).build());
        Tag tag8 = this.tagRepository.save(Tag.builder().tag("java").accessCount(80L).build());
        Tag tag9 = this.tagRepository.save(Tag.builder().tag("javascript").accessCount(90L).build());

        loggerInstance.info("etiquetas criadas e salvas");

        User admin1 = new User();

        admin1.setRole("ADMIN");
        admin1.setPassword(this.passwordEncoder.encode(adminPassword));
        admin1.setEmail(adminEmail);
        this.userRepository.save(admin1);

        User admin2 = new User();

        admin2.setRole("ADMIN");
        admin2.setPassword("qualquersenha1");
        admin2.setEmail("admin@mail.com");
        this.userRepository.save(admin2);

        UserCustomer user1 = new UserCustomer();
        user1.setRole("USER");
        user1.setPassword(this.passwordEncoder.encode(userPassword));
        user1.setEmail(userEmail);
        user1.setRegisteredTags(Set.of(tag1, tag2, tag3));
        user1 = this.userRepository.save(user1);


        UserCustomer user2 = new UserCustomer();
        user2.setRole("USER");
        user2.setPassword("qualquersenha2");
        user2.setEmail("antonio.sgarbi@hotmail.com");
        user2.setRegisteredTags(Set.of(tag4, tag5, tag6));
        user2 = this.userRepository.save(user2);

        UserCustomer user3 = new UserCustomer();
        user3.setRole("USER");
        user3.setPassword("qualquersenha3");
        user3.setEmail("antonio.gabriel.sgarbi@gmail.com");
        user3.setRegisteredTags(Set.of(tag7, tag8, tag9));
        user3 = this.userRepository.save(user3);

        UserCustomer user4 = new UserCustomer();
        user4.setRole("USER");
        user4.setPassword("qualquersenha4");
        user4.setEmail("aokk@gft.com");
        user4.setRegisteredTags(Set.of(tag3, tag5, tag7));
        user4 = this.userRepository.save(user4);

        UserCustomer user5 = new UserCustomer();
        user5.setRole("USER");
        user5.setPassword("qualquersenha5");
        user5.setEmail("usuario@hotmail.com");
        user5.setRegisteredTags(Set.of(tag2, tag4, tag8));
        user5 = this.userRepository.save(user5);

        loggerInstance.info("usuarios criados e salvos");

        List<UserCustomer> users = List.of(user1, user2, user3, user4, user5);

        for (UserCustomer user: users) {
            for (Tag tag: user.getRegisteredTags()) {
                for (int i = 0; i < 50; i++) {
                    this.accessTagRepository.save(new AccessTagRegister(tag, user));
                }
            }
            loggerInstance.info("criado histórico do usuário: {}", user.getEmail());
        }
    }

}
