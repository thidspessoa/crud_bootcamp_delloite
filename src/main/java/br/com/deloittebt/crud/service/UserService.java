package main.java.br.com.deloittebt.crud.service;

import java.util.List;
import main.java.br.com.deloittebt.crud.model.User;
import main.java.br.com.deloittebt.crud.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Camada de serviço responsável por regras de negócio da entidade User.
 * Faz a mediação entre a camada de apresentação e o repositório de persistência.
 * Segue o princípio SRP (Single Responsibility Principle) e valida regras de domínio.
 */
@Service
public class UserService {

    /**
     * Repositório responsável pela persistência de usuários.
     * Gerenciado pelo Spring via injeção de dependência.
     */
    private final UserRepository userRepository;

    /**
     * Construtor que recebe o repositório via injeção automatica do spring.
     * Garante que a dependência não seja nula.
     *
     * @param userRepository instância do UserRepository
     */
    public UserService(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository não pode ser nulo.");
        }
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo usuário e persiste no banco.
     *
     * @param user usuário a ser criado
     * @return usuário persistido
     */
    public User create(User user) {
        validateUser(user);
        return userRepository.save(user); // Persiste via JPA
    }

    /**
     * Retorna todos os usuários cadastrados.
     *
     * @return lista de usuários
     */
    public List<User> findAll() {
        return userRepository.findAll(); // Busca todos usuários via JPA
    }

    /**
     * Busca um usuário pelo ID.
     * Lança exceção se não encontrado.
     *
     * @param id identificador do usuário
     * @return usuário encontrado
     */
    public User findById(Long id) {
        validateId(id);
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));
    }

    /**
     * Atualiza nome e email de um usuário existente.
     *
     * @param id    identificador do usuário
     * @param name  novo nome
     * @param email novo email
     */
    public void update(Long id, String name, String email) {
        validateId(id);
        User user = findById(id);

        user.changeName(name);
        user.changeEmail(email);

        userRepository.update(user); // Atualiza via JPA
    }

    /**
     * Remove um usuário pelo ID.
     * Lança exceção caso a remoção falhe.
     *
     * @param id identificador do usuário
     */
    public void deleteById(Long id) {
        validateId(id);
        User user = findById(id);

        boolean deleted = userRepository.delete(user); // Remove via JPA

        if (!deleted) {
            throw new IllegalStateException("Erro ao remover usuário com id: " + id);
        }
    }

    /**
     * Valida se o usuário não é nulo.
     *
     * @param user usuário a validar
     */
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
    }

    /**
     * Valida se o ID é válido (não nulo e maior que zero).
     *
     * @param id identificador a validar
     */
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
    }
}