package tech.antoniosgarbi.desafioapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.dto.IntegrationDTO;
import tech.antoniosgarbi.desafioapi.dto.NewsDTO;
import tech.antoniosgarbi.desafioapi.dto.NewsIntegrationDTO;
import tech.antoniosgarbi.desafioapi.exception.NewsNotFoundException;
import tech.antoniosgarbi.desafioapi.model.AccessTagRegister;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.service.*;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final TagService tagService;
    private final UserCustomerService userCustomerService;
    private final IntegrationService integrationService;
    private final AccessTagRegisterService accessTagRegisterService;


    @Override
    @Transactional
    public List<NewsDTO> query(String query, String date, Principal principal) {
        Tag tag = this.tagService.createTagIfNotExists(query);

        UserCustomer user = userCustomerService.findModelByEmail(principal.getName());

        AccessTagRegister accessTagRegister = new AccessTagRegister(tag, user);

        accessTagRegisterService.save(accessTagRegister);

        IntegrationDTO integrationDTO = this.integrationService.query(query, date);

        if(integrationDTO.getList().isEmpty()) {
            throw new NewsNotFoundException("Não existem notícias para esta busca");
        }

        return integrationDTO.getList().stream().map(NewsDTO::new).toList();
    }

    @Override
    public Page<AccessTagRegisterDTO> getTagsHistory(Principal principal, Pageable pageable) {
        UserCustomer userCustomer = this.userCustomerService.findModelByEmail(principal.getName());

        return this.accessTagRegisterService.findTagsHistoryByUser(userCustomer, pageable);
    }

    @Override
    public String addTag( Principal principal, String tag) {
        Tag tagEntity = this.tagService.createTagIfNotExists(tag);

        this.userCustomerService.addTagToUser(tagEntity, principal.getName());

        return "tag adicionada";
    }

    @Override
    public String removeTag(Principal principal, String tag) {
        Tag tagEntity = this.tagService.createTagIfNotExists(tag);

        this.userCustomerService.removeTagFromUser(tagEntity, principal.getName());

        return "tag removida";
    }

    @Override
    public List<NewsDTO> getTodayNews(Principal principal) {
        UserCustomer user = this.userCustomerService.findModelByEmail(principal.getName());
        List<NewsDTO> newsTodayList = new LinkedList<>();
        String today = this.integrationService.getDateToday();

        for (Tag tag: user.getRegisteredTags()) {
            tag.setAccessCount(tag.getAccessCount() + 1);
            this.tagService.save(tag);

            IntegrationDTO integrationDTO = this.integrationService.query(tag.getValue(), today);

            if (integrationDTO.getList() != null) {
                for (NewsIntegrationDTO news : integrationDTO.getList()) {
                    if (news.getDate().equals(today)) {
                        newsTodayList.add(new NewsDTO(news));
                    }
                }
            }
        }
        if(!newsTodayList.isEmpty()) {
            return newsTodayList;
        }
        throw new NewsNotFoundException("Não há noticias para suas etiquetas hoje!");
    }

}
