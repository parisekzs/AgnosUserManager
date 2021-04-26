package hu.mi.user.manager;

import hu.mi.user.properties.repository.RoleRepo;
import hu.mi.user.properties.repository.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgnosUserManagerApplication {

     @Value("${AGNOS_HOME}/global/conf")
    private String configurationURI;

    public static void main(String[] args) {
        SpringApplication.run(AgnosUserManagerApplication.class, args);
    }

    @Bean
    public UserRepo getUserPropertyRepository() {
        UserRepo userRepo = new UserRepo(configurationURI);
        return userRepo;
    }

    @Bean
    public RoleRepo getRolePropertyRepository() {
        RoleRepo roleRepo = new RoleRepo(configurationURI);
        return roleRepo;
    }

}
