package br.com.deloittebt.crud.model;

/**
 * Camada de dominio responsavel por determinar a entidade, seus processos e regras de negócio.
 */
public class User {

    /**
     * Atributo de indentidade da entidade, a chave primária dessa entidade.
     */
    private Long id;

    /**
     * Atributo nome do usuário
     */
    private String name;

    /**
     * Atributo E-mail do usuário
     */
    private String email;

    /**
     * Construtor para criação de novo usuário (ainda não persistido).
     * O ID começa como null e será atribuído após persistência. (Wrapper Class -> Long)
     * @param name
     * @param email
     */
    public User(String name, String email) {
        // Validações
        validateName(name);
        validateEmail(email);

        this.id = null;
        this.name = name;
        this.email = email;
    }

    /**
     * Construtor para reconstrução da entidade a partir da persistência.
     * Utilizado exclusivamente pelo repositório.
     * @param id
     * @param name
     * @param email
     */
    public User(Long id, String name, String email) {

        // Só atribui ID se o ID atual for vazil, ou seja, ainda não persistiu o usuario no banco
        if (id == null) {
            throw new IllegalArgumentException("Usuário persistido deve possuir ID.");
        }

        // Validações
        validateName(name);
        validateEmail(email);

        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    /**
     * Método controlado para atribuição de ID.
     * Só pode ser chamado uma única vez (após persistência).
     * @param id
     */
    public void assignId(Long id) {
        if (this.id != null) { // Verifica se o atributo ID já está preenchido
            throw new IllegalStateException("ID já foi atribuído e não pode ser alterado.");
        }

        if (id == null) { // Verifica se o ID passado como parametro não é nullo
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }

        this.id = id;
    }

    /**
     * Atualiza nome garantindo invariantes.
     * @param name
     * @return void
     */
    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    /**
     * Atualiza email garantindo invariantes.
     * @param email
     * @return void
     */
    public void changeEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    /**
     * Valida o atributo name
     * @param name
     * @return void
     */
    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
    }

    /**
     * Valida o atributo email
     * @param email
     * @return void
     */
    private void validateEmail(String email) {
        if (email == null || email.isBlank()) { // Email nulo ou em branco
            throw new IllegalArgumentException("Email não pode ser vazio.");
        }

        if (!email.contains("@")) { // Verifica se de fato é um email
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
