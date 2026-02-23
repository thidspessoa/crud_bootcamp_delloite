package br.com.deloittebt.crud.repository;

import br.com.deloittebt.crud.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável exclusivamente pela persistência da entidade User.
 * Utiliza JPA/Hibernate para abstrair toda a lógica de acesso a dados.
 * Não deve conter regras de negócio, apenas operações de CRUD.
 */
public class UserRepository {

    /**
     * EntityManager utilizado para realizar operações de persistência.
     */
    private final EntityManager entityManager;

    /**
     * Construtor que recebe um EntityManager via injeção.
     * @param entityManager gerenciador de entidades JPA/Hibernate
     */
    public UserRepository(EntityManager entityManager) {
        if (entityManager == null) {
            throw new IllegalArgumentException("EntityManager não pode ser nulo.");
        }
        this.entityManager = entityManager;
    }

    /**
     * Persiste um novo usuário no banco de dados.
     * O ID é gerado automaticamente pelo Hibernate.
     * @param user usuário a ser salvo
     * @return usuário persistido com ID preenchido
     */
    public User save(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user); // Persistência JPA
            transaction.commit();
            return user;
        } catch (RuntimeException e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    /**
     * Retorna todos os usuários cadastrados no banco.
     * @return lista de usuários
     */
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    /**
     * Busca usuário por ID.
     * @param id identificador do usuário
     * @return Optional contendo o usuário se encontrado
     */
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    /**
     * Atualiza os dados de um usuário existente.
     * @param user usuário com dados modificados
     * @return true se a atualização ocorreu com sucesso
     */
    public boolean update(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(user); // Atualiza entidade gerenciada
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    /**
     * Remove usuário do banco.
     * @param user usuário a ser removido
     * @return true se a remoção ocorreu com sucesso
     */
    public boolean delete(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }
}