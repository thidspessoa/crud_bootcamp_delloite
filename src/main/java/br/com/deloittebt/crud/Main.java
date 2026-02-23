package main.java.br.com.deloittebt.crud;

import main.java.br.com.deloittebt.crud.model.User;
import main.java.br.com.deloittebt.crud.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicação CRUD de usuários via console.
 *
 * Agora integrado ao Spring Boot:
 * - O Spring gerencia as dependências (injeção automática de UserService)
 * - Permite expansão futura para Web ou APIs REST sem modificar a lógica de domínio
 */
@SpringBootApplication
public class Main {

    /**
     * Método principal da aplicação.
     * Inicializa o contexto do Spring Boot e obtém o UserService via injeção.
     *
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {

        // Inicializa Spring Boot e obtém o ApplicationContext
        ApplicationContext context = SpringApplication.run(Main.class, args);

        // Recupera o UserService gerenciado pelo Spring
        UserService userService = context.getBean(UserService.class);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Loop principal do menu de console
        while (running) {
            printMenu();
            int option = scanner.nextInt();
            scanner.nextLine(); // limpa buffer

            try {
                switch (option) {
                    case 1 -> createUser(userService, scanner);
                    case 2 -> viewData(userService);
                    case 3 -> findUserById(userService, scanner);
                    case 4 -> updateUser(userService, scanner);
                    case 5 -> deleteUser(userService, scanner);
                    case 0 -> {
                        running = false;
                        System.out.println("Saindo...");
                        System.exit(0);
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } catch (RuntimeException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== MENU =====");
        System.out.println("1 - Criar usuário");
        System.out.println("2 - Listar usuários");
        System.out.println("3 - Buscar usuário por ID");
        System.out.println("4 - Atualizar usuário");
        System.out.println("5 - Remover usuário");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void createUser(UserService userService, Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        User user = new User(nome, email);
        userService.create(user);
        System.out.println("Usuário criado com sucesso!");
        viewData(userService);
    }

    private static void updateUser(UserService userService, Scanner scanner) {
        System.out.print("Informe o ID do usuário: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo email: ");
        String email = scanner.nextLine();
        userService.update(id, nome, email);
        System.out.println("Usuário atualizado com sucesso!");
        viewData(userService);
    }

    private static void deleteUser(UserService userService, Scanner scanner) {
        System.out.print("Informe o ID do usuário: ");
        long id = scanner.nextLong();
        userService.deleteById(id);
        System.out.println("Usuário removido com sucesso!");
        viewData(userService);
    }

    private static void findUserById(UserService userService, Scanner scanner) {
        System.out.print("Informe o ID do usuário: ");
        long id = scanner.nextLong();
        User user = userService.findById(id);
        System.out.println(user);
    }

    private static void viewData(UserService userService) {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            users.forEach(System.out::println);
        }
    }
}