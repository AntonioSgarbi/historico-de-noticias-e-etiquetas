package tech.antoniosgarbi.desafioapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.dto.NewsDTO;

import java.security.Principal;
import java.util.List;


public interface CustomerService {

    List<NewsDTO> query(String query, String date, Principal principal);

    Page<AccessTagRegisterDTO> getTagsHistory(Principal principal, Pageable pageable);

    String addTag(Principal principal, String tag);

    String removeTag(Principal principal, String tag);

    List<NewsDTO> getTodayNews(Principal principal);
}
