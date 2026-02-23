package main.java.br.com.deloittebt.crud.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import main.java.br.com.deloittebt.crud.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável exclusivamente pela persistência da entidade User.
 * Utiliza JPA/Hibernate para abstrair toda a lógica de acesso a dados.
 * Não deve conter regras de negócio, apenas operações de CRUD.
 * O Spring Boot gerencia transações automaticamente com @Transactional.
 */
@Repository
public class UserRepository {

    /**
     * EntityManager utilizado para realizar operações de persistência.
     * Injetado automaticamente pelo Spring Boot via @PersistenceContext.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Persiste um novo usuário no banco de dados.
     * O ID é gerado automaticamente pelo Hibernate.
     *
     * @param user usuário a ser salvo
     * @return usuário persistido com ID preenchido
     */
    @Transactional
    public User save(User user) {
        entityManager.persist(user); // Persistência JPA
        return user;
    }

    /**
     * Retorna todos os usuários cadastrados no banco.
     *
     * @return lista de usuários
     */
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    /**
     * Busca usuário por ID.
     *
     * @param id identificador do usuário
     * @return Optional contendo o usuário se encontrado
     */
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param user usuário com dados modificados
     * @return true se a atualização ocorreu com sucesso
     */
    @Transactional
    public boolean update(User user) {
        entityManager.merge(user); // Atualiza entidade gerenciada
        return true;
    }

    /**
     * Remove usuário do banco.
     *
     * @param user usuário a ser removido
     * @return true se a remoção ocorreu com sucesso
     */
    @Transactional
    public boolean delete(User user) {
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
        return true;
    }
}