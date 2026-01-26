package com.kudryavtsevgennady.springrestaurantvoting.spring;

import com.kudryavtsevgennady.springrestaurantvoting.spring.model.User;
import com.kudryavtsevgennady.springrestaurantvoting.spring.to.UserTo;
import com.kudryavtsevgennady.springrestaurantvoting.spring.util.UserUtil;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.userTo = UserUtil.asTo(user);
    }

    public int getId() {
        return userTo.id();
    }

    public void update(UserTo newTo) {
        userTo = newTo;
    }

    public UserTo getUserTo() {
        return userTo;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}
