package de.uniregensburg.oauthclient.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.uniregensburg.oauthclient.model.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Token findById(long id);

    List<Token> findByUsername(String username);
    
}
