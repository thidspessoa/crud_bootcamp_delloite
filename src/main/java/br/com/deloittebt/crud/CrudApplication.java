package br.com.deloittebt.crud;

import br.com.deloittebt.crud.model.User;
import br.com.deloittebt.crud.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada principal da aplicação CRUD de usuários.
 * Esta classe inicializa o contexto Spring Boot e executa uma interface
 * de linha de comando (CLI) para interagir com o sistema de usuários.
 * Ela também configura o servidor web para o frontend HTML.
 */
@SpringBootApplication
public class CrudApplication {

    /**
     * Método principal da aplicação.
     * Inicializa o contexto do Spring Boot, obtém o UserService via injeção
     * e executa o loop principal da interface de console.
     *
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {

        // Inicializa Spring Boot e obtém o ApplicationContext
        ApplicationContext context = SpringApplication.run(CrudApplication.class, args);

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

    /**
     * Exibe o menu de opções para o usuário no console.
     */
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

    /**
     * Solicita dados do usuário (nome e email) e cria um novo usuário.
     * @param userService O serviço de usuário para realizar a operação.
     * @param scanner O scanner para ler a entrada do console.
     */
    private static void createUser(UserService userService, Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        // A chamada ao método create foi corrigida para a nova assinatura
        userService.create(nome, email);
        System.out.println("Usuário criado com sucesso!");
        viewData(userService);
    }

    /**
     * Solicita o ID do usuário, novos dados (nome e email) e atualiza o usuário existente.
     * @param userService O serviço de usuário para realizar a operação.
     * @param scanner O scanner para ler a entrada do console.
     */
    private static void updateUser(UserService userService, Scanner scanner) {
        System.out.print("Informe o ID do usuário: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // Consome a nova linha
        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo email: ");
        String email = scanner.nextLine();
        userService.update(id, nome, email);
        System.out.println("Usuário atualizado com sucesso!");
        viewData(userService);
    }

    /**
     * Solicita o ID do usuário a ser removido e realiza a exclusão.
     * @param userService O serviço de usuário para realizar a operação.
     * @param scanner O scanner para ler a entrada do console.
     */
    private static void deleteUser(UserService userService, Scanner scanner) {
        System.out.print("Informe o ID do usuário: ");
        long id = scanner.nextLong();
        userService.deleteById(id);
        System.out.println("Usuário removido com sucesso!");
        viewData(userService);
    }

    /**
     * Solicita o ID do usuário a ser buscado e exibe seus detalhes.
     * @param userService O serviço de usuário para realizar a operação.
     * @param scanner O scanner para ler a entrada do console.
     */
    private static void findUserById(UserService userService, Scanner scanner) {
        System.out.print("Informe o ID do usuário: ");
        long id = scanner.nextLong();
        User user = userService.findById(id);
        System.out.println(user);
    }

    /**
     * Exibe a lista de todos os usuários cadastrados no console.
     * @param userService O serviço de usuário para obter a lista de usuários.
     */
    private static void viewData(UserService userService) {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            users.forEach(System.out::println);
        }
    }
}