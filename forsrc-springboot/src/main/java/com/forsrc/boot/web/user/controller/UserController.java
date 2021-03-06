package com.forsrc.boot.web.user.controller;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.forsrc.core.web.user.service.UserService;
import com.forsrc.pojo.User;

//@RestController
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = { RequestMethod.GET })
    @ResponseBody
    public List<User> get() throws Exception {
        try {
            List<User> list = userService.get(0, 10);
            // System.out.println("---> " + ((Object[])(list.get(0)))[0]);
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/add", method = { RequestMethod.GET })
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public User add(String username, String password, String email) throws Exception {
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            userService.save(user, password.getBytes());
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequestMapping(value = "/", method = { RequestMethod.PUT })
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public User save(String username, String email) throws Exception {
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            userService.save(user);
            return user;
        } catch (Exception ex) {
            throw ex;
        }

    }

    @RequestMapping(value = "/{id}", method = { RequestMethod.DELETE })
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') and #id > 0")
    // @PreAuthorize("#id > 0")
    public User delete(@PathVariable("id") Long id) throws Exception {
        System.out.println(MessageFormat.format("delete({0}) --> {1}", id, new Date().toString()));
        try {
            User user = new User(id);
            userService.delete(user);
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequestMapping(value = "/{id}", method = { RequestMethod.GET })
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER') and #id > 0")
    public User get(@PathVariable("id") Long id) {
        System.out.println(MessageFormat.format("get({0}) --> {1}", id, new Date().toString()));
        User user = userService.get(id);

        return user;
    }

    @RequestMapping(value = "/{id}", method = { RequestMethod.PATCH, RequestMethod.PUT })
    @ResponseBody
    // @PreAuthorize("hasRole('ROLE_USER')")
    @PreAuthorize("principal.username.equals(#username)")
    public User update(@PathVariable("id") Long id, String username, String email) throws Exception {
        System.out.println(MessageFormat.format("update({0}) --> {1}", id, new Date().toString()));
        try {
            User user = userService.get(id);
            user.setEmail(email);
            user.setUsername(username);
            userService.update(user);
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
