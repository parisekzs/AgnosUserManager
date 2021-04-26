/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.manager.controller;

import hu.mi.user.properties.converter.UserConverter;
import hu.mi.user.properties.entity.User;
import hu.mi.user.properties.model.UserDTO;
import hu.mi.user.properties.repository.UserRepo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author parisek
 */
@RestController
@RequestMapping("/aum")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepo userRepo;

    public UserController() {
    }

    @GetMapping("/users")
    Collection<UserDTO> users() {
        return UserConverter.dao2dto(userRepo.findAll());
    }

    @GetMapping("/user/{name}")
    ResponseEntity<?> getUser(@PathVariable String name) {
        Optional<UserDTO> user = UserConverter.dao2dto(userRepo.findById(name));
        return user.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/user")
    ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user) throws URISyntaxException {
        log.info("Request to create AgnosUser: {}", user.getName());
        User daoUser = UserConverter.dto2dao(user);
        if (userRepo.findById(daoUser.getName()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Optional<UserDTO> result = UserConverter.dao2dto(userRepo.save(daoUser));
        return ResponseEntity.created(new URI("/aum/user/" + result.get().getName()))
                .body(result.get());
    }

    @PutMapping("/user/{userName}")
    ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO user, @PathVariable String userName) {
        log.info("Request to update AgnosUser: {}", user.getName());
        User daoUser = UserConverter.dto2dao(user);

        if (userRepo.findById(daoUser.getName()).isPresent() && !user.getName().equals(userName)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Optional<User> optStoredUser = userRepo.findById(userName);
        if (optStoredUser.isPresent()) {
            User storedUser = optStoredUser.get();
            if (user.getPlainPassword().isEmpty()) {
                daoUser.setEncodedPassword(storedUser.getEncodedPassword());
            }
            userRepo.deleteById(userName);

        }
        Optional<UserDTO> result = UserConverter.dao2dto(userRepo.save(daoUser));
        return ResponseEntity.ok().body(result.get());
    }

    @DeleteMapping("/user/{userName}")
    public ResponseEntity<?> deleteUser(@PathVariable String userName) {
        log.info("Request to delete AgnosUser: {}", userName);
        userRepo.deleteById(userName);
        return ResponseEntity.ok().build();
    }
}
