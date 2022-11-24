package tech.antoniosgarbi.desafioapi.service;

import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;


public interface UserCustomerService {

    UserCustomer findModelByEmail(String email);

    void addTagToUser(Tag tag, String username);

    void removeTagFromUser(Tag tag, String username);

}
