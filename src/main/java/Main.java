import br.com.deloittebt.crud.model.User;
import br.com.deloittebt.crud.repository.UserRepository;
import br.com.deloittebt.crud.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicação CRUD de usuários.
 * Cria o EntityManager, instancia o repositório e o serviço,
 * e fornece interface de console para interagir com os usuários.
 */
public class Main {

    /**
     * Método principal da aplicação.
     * Configura JPA/Hibernate, inicializa repositório e serviço,
     * e executa o loop do menu de console.
     *
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {

        // Criação do EntityManagerFactory e EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("thipssPU");
        EntityManager em = emf.createEntityManager();

        // Criação do repositório e serviço com injeção de dependência
        UserRepository userRepository = new UserRepository(em);
        UserService userService = new UserService(userRepository);

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
                    case 0 -> running = false;
                    default -> System.out.println("Opção inválida!");
                }
            } catch (RuntimeException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        // Fecha recursos
        scanner.close();
        em.close();
        emf.close();
    }

    /**
     * Imprime o menu principal no console.
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
     * Cria um novo usuário lendo dados do console.
     */
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

    /**
     * Atualiza um usuário existente lendo dados do console.
     */
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

    /**
     * Remove um usuário pelo ID.
     */
    private static void deleteUser(UserService userService, Scanner scanner) {
        System.out.print("Informe o ID do usuário: ");
        long id = scanner.nextLong();
        userService.deleteById(id);
        System.out.println("Usuário removido com sucesso!");
        viewData(userService);
    }

    /**
     * Busca um usuário pelo ID.
     */
    private static void findUserById(UserService userService, Scanner scanner) {
        System.out.print("Informe o ID do usuário: ");
        long id = scanner.nextLong();
        User user = userService.findById(id);
        System.out.println(user);
    }

    /**
     * Exibe todos os usuários cadastrados no console.
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