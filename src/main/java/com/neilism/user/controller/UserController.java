package com.neilism.user.controller;

import com.neilism.user.model.User;
import com.neilism.user.model.command.UserCommand;
import com.neilism.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> findById(@PathVariable("username") String username) {
        return new ResponseEntity<User>(userService.findByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody UserCommand userCommand) {
        userService.saveUser(new User(userCommand));
        return new ResponseEntity<>(userService.findByUsername(userCommand.getUsername()), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<User> update(@RequestBody UserCommand userCommand) {
        User user = userService.findByUsername(userCommand.getUsername());
        user.setEmail(userCommand.getEmail());
        user.setFirstName(userCommand.getFirstName());
        user.setLastName(userCommand.getLastName());
        user.setRoles(userCommand.getRoles());
        user.setActive(userCommand.isActive());
        userService.saveUser(user);
        return new ResponseEntity<>(userService.findByUsername(userCommand.getUsername()), HttpStatus.OK);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("username") String username) {
        userService.deleteByUsername(username);
        return new ResponseEntity(HttpStatus.OK);
    }

}
