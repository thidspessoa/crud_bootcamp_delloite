package main.java.br.com.deloittebt.crud.service;

import main.java.br.com.deloittebt.crud.model.User;
import main.java.br.com.deloittebt.crud.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    // CREATE
    public User create(User user) {
       return userRepository.save(user);
    }

    // READ - LIST ALL
    public List<User>  findAll() {
        return userRepository.findAll();
    }

    // READ - FIND BY ID
    public User findById(long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com id: " + id);
        }

        return optionalUser.get();
    }

    // UPDATE
    public void update(long id, String nome, String email) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Não é possível atualizar. Usuário não encontrado com id: " + id);
        }

        User user = optionalUser.get();
        user.setName(nome);
        user.setEmail(email);

        userRepository.update(user);
    }

    // DELETE
    public void deleteById(long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Não é possível remover. Usuário não encontrado com id: " + id);
        }

        boolean deleted = userRepository.deleteById(id);

        if (!deleted) {
            throw new RuntimeException("Erro ao remover usuário com id: " + id);
        }
    }

}
