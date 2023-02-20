package rs.raf.demo.services;

import rs.raf.demo.model.User;

import java.util.List;

public interface UserService {

    public User createUser();

    public void deleteUser(int id);

    public List<User> findAll();

    public User editUser();
}
