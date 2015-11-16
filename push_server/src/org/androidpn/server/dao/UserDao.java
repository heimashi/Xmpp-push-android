
package org.androidpn.server.dao;

import java.util.Date;
import java.util.List;

import org.androidpn.server.model.User;
import org.androidpn.server.service.UserNotFoundException;

public interface UserDao {

    public User getUser(Long id);

    public User saveUser(User user);

    public void removeUser(Long id);

    public boolean exists(Long id);
    
    public List<User> getUsers();
    
    public List<User> getUsersFromCreatedDate(Date createDate);

    public User getUserByUsername(String username) throws UserNotFoundException;

}
