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

public class UserRepository {

    public UserRepository() {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL
            )
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela de usuários", e);
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:h2:mem:cruddb";
        String user = "sa";
        String password = "";

        return DriverManager.getConnection(url, user, password);
    }

    public User save(User usuario) {

        String sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            stmt.setString(1, usuario.getName());
            stmt.setString(2, usuario.getEmail());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                }
            }

            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }

    public List<User> findAll() {

        List<User> usuarios = new ArrayList<>();

        String sql = "SELECT id, nome, email FROM usuarios";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User usuario = new User(
                        rs.getString("nome"),
                        rs.getString("email")
                );

                usuarios.add(usuario);
            }

            return usuarios;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }
    }

    public Optional<User> findById(Long id) {

        String sql = """
        SELECT id, nome, email
        FROM usuarios
        WHERE id = ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    User user = new User(
                            rs.getString("nome"),
                            rs.getString("email")
                    );
                    return Optional.of(user);
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por id", e);
        }
    }

    public int update(User usuario) {

        String sql = """
            UPDATE usuarios
            SET nome = ?, email = ?
            WHERE id = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getName());
            stmt.setString(2, usuario.getEmail());
            stmt.setLong(3, usuario.getId());

            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    public boolean deleteById(long id) {

        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário com id: " + id, e);
        }
    }
}
