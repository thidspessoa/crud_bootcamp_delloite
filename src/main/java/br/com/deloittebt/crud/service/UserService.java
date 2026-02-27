package br.com.deloittebt.crud.service;

import java.util.List;
import br.com.deloittebt.crud.model.User;
import br.com.deloittebt.crud.repository.UserRepository;
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
     * Cria um novo usuário com os dados fornecidos e persiste no banco.
     * @param name Nome do usuário.
     * @param email Email do usuário.
     * @return Usuário persistido.
     */
    public User create(String name, String email) {
        // As validações de name e email já estão no construtor de User
        User user = new User(name, email);
        return userRepository.save(user); // Persiste via JPA
    }

    /**
     * Retorna todos os usuários cadastrados.
     * @return Lista de usuários.
     */
    public List<User> findAll() {
        return userRepository.findAll(); // Busca todos usuários via JPA
    }

    /**
     * Busca um usuário pelo ID.
     * Lança exceção se não encontrado.
     * @param id Identificador do usuário.
     * @return Usuário encontrado.
     * @throws IllegalArgumentException se o ID for inválido ou usuário não encontrado.
     */
    public User findById(Long id) {
        validateId(id);
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com id: " + id));
    }

    /**
     * Atualiza nome e email de um usuário existente.
     * @param id Identificador do usuário.
     * @param name Novo nome.
     * @param email Novo email.
     * @throws IllegalArgumentException se o ID for inválido ou usuário não encontrado.
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
     * @param id Identificador do usuário.
     * @throws IllegalArgumentException se o ID for inválido ou usuário não encontrado.
     * @throws IllegalStateException se a remoção falhar.
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
     * Valida se o ID é válido (não nulo e maior que zero).
     * @param id Identificador a validar.
     * @throws IllegalArgumentException se o ID for nulo ou menor ou igual a zero.
     */
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
    }
}