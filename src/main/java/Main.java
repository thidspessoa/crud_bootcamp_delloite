import br.com.deloittebt.crud.model.User;
import br.com.deloittebt.crud.repository.UserRepository;
import br.com.deloittebt.crud.service.UserService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Inicialização manual das dependências (injeção manual)
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            printMenu();

            int option = readInteger(scanner);

            try {
                switch (option) {
                    case 1 -> createUser(scanner, userService);
                    case 2 -> listUsers(userService);
                    case 3 -> findUserById(scanner, userService);
                    case 4 -> updateUser(scanner, userService);
                    case 5 -> deleteUser(scanner, userService);
                    case 0 -> {
                        running = false;
                        System.out.println("Encerrando o sistema...");
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
     * Exibe o menu principal.
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
     * Lê um número inteiro do usuário de forma segura.
     */
    private static int readInteger(Scanner scanner) {
        int value = scanner.nextInt();
        scanner.nextLine(); // limpa buffer
        return value;
    }

    /**
     * Fluxo de criação de usuário.
     */
    private static void createUser(Scanner scanner, UserService userService) {

        System.out.print("Nome: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        User user = new User(name, email);
        userService.create(user);

        System.out.println("Usuário criado com sucesso!");
        listUsers(userService);
    }

    /**
     * Lista todos os usuários cadastrados.
     */
    private static void listUsers(UserService userService) {

        List<User> users = userService.findAll();

        System.out.println("==============================");

        if (users.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            users.forEach(System.out::println);
        }

        System.out.println("==============================");
    }

    /**
     * Busca usuário por ID.
     */
    private static void findUserById(Scanner scanner, UserService userService) {

        System.out.print("Informe o ID do usuário: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        User user = userService.findById(id);

        System.out.println("==============================");
        System.out.println(user);
        System.out.println("==============================");
    }

    /**
     * Atualiza dados de um usuário.
     */
    private static void updateUser(Scanner scanner, UserService userService) {

        System.out.print("Informe o ID do usuário: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Novo nome: ");
        String name = scanner.nextLine();

        System.out.print("Novo email: ");
        String email = scanner.nextLine();

        userService.update(id, name, email);

        System.out.println("Usuário atualizado com sucesso!");
        listUsers(userService);
    }

    /**
     * Remove usuário pelo ID.
     */
    private static void deleteUser(Scanner scanner, UserService userService) {

        System.out.print("Informe o ID do usuário: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        userService.deleteById(id);

        System.out.println("Usuário removido com sucesso!");
        listUsers(userService);
    }
}