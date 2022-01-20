package com.esgi.booklib.repositories;


import com.esgi.booklib.entities.Session;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {

    @Procedure(value = "sign_in")
    public String signIn(@Param("identifier") String login, @Param("pwd") String password);

    @Procedure(value="get_authorization")
    public int getAuthorization(@Param("token") String token);


}
