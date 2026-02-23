package br.com.deloittebt.crud.repository;

import br.com.deloittebt.crud.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Classe responsável exclusivamente pela persistência da entidade User.
 * Seu papel é encapsular toda a lógica de acesso a dados (JDBC).
 * Não deve conter regras de negócio.
 */
public class UserRepository {
    private static final String url = "jdbc:h2:file:./cruddb";
    private static final String user = "sa";
    private static final String password = "";

    /**
     * Construtor responsável por garantir que a tabela exista.
     */
    public UserRepository() {
        createTableIfNotExists();
    }

    /**
     * Cria a tabela caso ainda não exista.
     */
    private void createTableIfNotExists() {

        String sql = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao criar tabela de usuários.", e);
        }
    }

    /**
     * Obtém uma conexão com o banco de dados.
     * @return Connection
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.user, this.password);
    }

    /**
     * Persiste um novo usuário.
     * @param user usuário a ser salvo
     * @return User ->  usuário com ID gerado
     */
    public User save(User user) {

        String sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());

            statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    Long generatedId = rs.getLong(1);
                    user.assignId(generatedId); // Regra de negocio para atualizar ID apos persistência
                }
            }

            return user;

        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar usuário.", e);
        }
    }

    /**
     * Retorna todos os usuários cadastrados.
     * @return List<User>
     */
    public List<User> findAll() {

        final String sql = "SELECT id, nome, email FROM usuarios";

        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapToUser(resultSet));
            }

            return users;

        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar usuários.", e);
        }
    }

    /**
     * Busca usuário por ID.
     * @param id
     * @return Optional<User>
     */
    public Optional<User> findById(Long id) {

        final String sql = """
                SELECT id, nome, email
                FROM usuarios
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapToUser(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao buscar usuário por ID.", e);
        }
    }


    /**
     * Atualiza um usuário existente.
     * @param user usuário a ser atualizado
     * @return true se atualizado com sucesso
     */
    public boolean update(User user) {

        String sql = """
                UPDATE usuarios
                SET nome = ?, email = ?
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setLong(3, user.getId());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao atualizar usuário.", e);
        }
    }

    /**
     * Remove usuário pelo ID.
     * @param id identificador
     * @return true se removido com sucesso
     */
    public boolean deleteById(Long id) {

        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao remover usuário.", e);
        }
    }

    /**
     * Responsável por mapear um ResultSet
     * para a entidade User.
     * Centraliza a lógica de transformação.
     */
    private User mapToUser(ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong("id");
        String name = resultSet.getString("nome");
        String email = resultSet.getString("email");

        User user = new User(name, email);
        user.assignId(id);

        return user;
    }

}
