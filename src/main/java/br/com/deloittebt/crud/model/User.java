package main.java.br.com.deloittebt.crud.model;

public class User {

    private Long id;
    private String name;
    private String email;

    // Construtor para criação (sem o ID)
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Construtor para reconstrução no banco
    public User(Long id, String name, String email) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
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
