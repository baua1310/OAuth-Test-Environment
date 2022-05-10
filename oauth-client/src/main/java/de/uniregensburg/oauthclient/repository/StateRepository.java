package de.uniregensburg.oauthclient.repository;

import org.springframework.data.repository.CrudRepository;

import de.uniregensburg.oauthclient.model.State;

public interface StateRepository extends CrudRepository<State, Long> {

    State findById(long id);
    
}
