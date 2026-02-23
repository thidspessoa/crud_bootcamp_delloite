package br.com.deloittebt.crud.service;

import br.com.deloittebt.crud.model.User;
import br.com.deloittebt.crud.repository.UserRepository;

import java.util.List;
import java.util.Optional;
/**
 * Camada de serviço responsável por orquestrar as regras de negócio
 * relacionadas à entidade User.
 * Atua como intermediário entre a camada de apresentação (controller)
 * e a camada de persistência (repository).
 * Segue o princípio de responsabilidade única (SRP),
 * delegando persistência ao repositório e mantendo regras de negócio aqui.
 */
public class UserService {

    /**
     * Dependência responsável pelo acesso a dados.
     * É final para garantir imutabilidade após construção.
     */
    private final UserRepository userRepository;

    /**
     * Construtor que recebe a dependência via injeção.
     * Isso segue o princípio da Inversão de Dependência (DIP).
     * @param userRepository repositório de persistência
     */
    public UserService(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository não pode ser nulo.");
        }
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo usuário.
     * @param user usuário a ser criado
     * @return usuário persistido
     */
    public User create(User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    /**
     * Retorna todos os usuários cadastrados.
     * @return lista de usuários
     */
    public List<User>  findAll() {
        return userRepository.findAll();
    }

    /**
     * Busca um usuário pelo ID.
     * @param id identificador do usuário
     * @return usuário encontrado
     */
    public User findById(Long id) {
        validateId(id);

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário não encontrado com id: " + id));
    }


    /**
     * Atualiza os dados de um usuário existente.
     * @param id identificador do usuário
     * @param name novo nome
     * @param email novo email
     */
    public void update(Long id, String name, String email) {
        validateId(id);

        User user = findById(id);

        user.changeName(name);
        user.changeEmail(email);

        userRepository.update(user);
    }


    /**
     * Remove um usuário pelo ID.
     * @param id identificador do usuário
     */
    public void deleteById(Long id) {
        validateId(id);

        User user = findById(id);

        boolean deleted = userRepository.deleteById(user.getId());

        if (!deleted) {
            throw new IllegalStateException("Erro ao remover usuário com id: " + id);
        }
    }

    /**
     * Validação de regras básicas do usuário antes de persistir.
     * @param user usuário a validar
     */
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
    }

    /**
     * Valida se o ID é válido.
     * @param id identificador a validar
     */
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
    }


}
