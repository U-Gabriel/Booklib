package com.esgi.booklib;

import com.esgi.booklib.repositories.SessionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static int getAuthorization(HttpHeaders headers, SessionRepository sessionRepository){
        String token = getTokenFromHeader(headers);
        if (token == null) {
            return -1;
        }
        return sessionRepository.getAuthorization(token);
    }

    public static String getTokenFromHeader(HttpHeaders headers) {
        List<String> authorizations = headers.get("API_KEY");
        if (authorizations == null) {
            return null;
        } else if (authorizations.size() <= 0) {
            return null;
        }
        return authorizations.get(0);
    }


}
