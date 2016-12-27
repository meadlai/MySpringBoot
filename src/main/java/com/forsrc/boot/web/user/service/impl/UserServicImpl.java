package com.forsrc.boot.web.user.service.impl;

import com.forsrc.boot.web.user.dao.UserDao;
import com.forsrc.boot.web.user.dao.UserPrivacyDao;
import com.forsrc.boot.web.user.service.UserService;
import com.forsrc.pojo.User;
import com.forsrc.pojo.UserPrivacy;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class UserServicImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserPrivacyDao userPrivacyDao;

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void save(User user, char [] password) {
        userDao.save(user);
        UserPrivacy userPrivacy = new UserPrivacy();
        userPrivacy.setUserId(user.getId());
        userPrivacy.setPassword(password.toString());
        userPrivacyDao.save(userPrivacy);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    public User get(long id) {
        return userDao.get(id);
    }

    @Override
    public List<User> get(int start, int size) {
        //return userDao.get("select '******' as password, user.id, user.username, user.email from com.forsrc.pojo.User user", null, start, size);
        return userDao.get(start, size);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }
}
