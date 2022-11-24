package tech.antoniosgarbi.desafioapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafioapi.exception.TagNotExistsException;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.repository.UserCustomerRepository;
import tech.antoniosgarbi.desafioapi.service.UserCustomerService;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserCustomerServiceImpl implements UserCustomerService {

    private final UserCustomerRepository userRepository;


    @Override
    public UserCustomer findModelByEmail(String username) {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não foi encontrado"));
    }

    @Override
    public void addTagToUser(Tag tag, String username) {
        UserCustomer user = this.findModelByEmail(username);
        if(!user.getRegisteredTags().contains(tag)) {
            Set<Tag> registered = new HashSet<>(user.getRegisteredTags());
            registered.add(tag);
            user.setRegisteredTags(registered);
            this.userRepository.save(user);
        }
    }

    @Override
    public void removeTagFromUser(Tag tag, String username) {
        UserCustomer user = this.findModelByEmail(username);

        Set<Tag> favorites = new HashSet<>(user.getRegisteredTags());

        if (favorites.remove(tag)) {
            user.setRegisteredTags(favorites);
            this.userRepository.save(user);
        } else {
            throw new TagNotExistsException("A etiqueta informada não está na sua lista de favoritas");
        }
    }

}
