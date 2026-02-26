package br.com.deloittebt.crud.model;

import jakarta.persistence.*;

/**
 * Entidade User do domínio do sistema.
 * Esta classe representa um usuário e encapsula as regras de negócio.
 * Está mapeada como entidade JPA para persistência com Hibernate.
 */
@Entity
@Table(name = "usuarios")
public class User {

    /**
     * Atributo de identidade da entidade, chave primária.
     * ID é gerado automaticamente pelo banco de dados (auto-increment).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Atributo nome do usuário.
     */
    @Column(name = "nome", nullable = false)
    private String name;

    /**
     * Atributo email do usuário.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * Construtor público sem argumentos, necessário para o data binding do Spring/Thymeleaf.
     * As validações serão aplicadas quando os métodos changeName/changeEmail forem chamados ou no construtor completo.
     */
    public User() {
        // Construtor vazio para Spring/Thymeleaf
    }

    /**
     * Construtor para criação de novo usuário (não persistido).
     * O ID será atribuído automaticamente após persistência.
     * @param name  Nome do usuário
     * @param email Email do usuário
     */
    public User(String name, String email) {
        validateName(name);
        validateEmail(email);
        this.name = name;
        this.email = email;
        this.id = null; // ID será atribuído pelo Hibernate após persistência
    }

    // -------------------------
    // GETTERS
    // -------------------------

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // -------------------------
    // MÉTODOS DE DOMÍNIO
    // -------------------------

    /**
     * Atribui ID ao usuário após persistência.
     * Pode ser chamado apenas uma vez.
     * @param id Identificador gerado pelo banco
     */
    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("ID já foi atribuído e não pode ser alterado.");
        }
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        this.id = id;
    }

    /**
     * Atualiza o nome do usuário, garantindo as invariantes do domínio.
     * @param name Novo nome
     */
    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    /**
     * Atualiza o email do usuário, garantindo as invariantes do domínio.
     * @param email Novo email
     */
    public void changeEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    // -------------------------
    // VALIDAÇÕES PRIVADAS
    // -------------------------

    /**
     * Valida o atributo name
     * Verifica se é vazil ou está em branco.
     * @param name
     */
    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
    }

    /**
     * Valida o atributo email
     * Verifica se está vazil, se contem @, ou se está em branco.
     * @param email
     */
    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser vazio.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}