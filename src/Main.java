import br.com.deloittebt.crud.model.User;
import br.com.deloittebt.crud.service.UserService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("\n===== MENU =====");
            System.out.println("1 - Criar usuário");
            System.out.println("2 - Listar usuários");
            System.out.println("3 - Buscar usuário por ID");
            System.out.println("4 - Atualizar usuário");
            System.out.println("5 - Remover usuário");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // limpa buffer

            try {
                switch (option) {

                    case 1 -> {
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();

                        System.out.print("Email: ");
                        String email = scanner.nextLine();

                        User user = new User(nome, email);
                        User savedUser = userService.create(user);

                        // Exibi o estado atual do banco
                        System.out.println("==============================");
                        System.out.println("Usuário criado com sucesso.");
                        viewData(userService);
                        System.out.println("==============================");

                    }

                    case 2 -> {
                        System.out.println("==============================");
                        System.out.println("Usuarios cadastrados: ");
                        viewData(userService);
                        System.out.println("==============================");
                    }

                    case 3 -> {
                        System.out.print("Informe o ID do usuário: ");
                        long id = scanner.nextLong();

                        User user = userService.findById(id);

                        System.out.println("==============================");
                        System.out.println(user);
                        System.out.println("==============================");
                    }

                    case 4 -> {
                        System.out.print("Informe o ID do usuário: ");
                        long id = scanner.nextLong();
                        scanner.nextLine();

                        System.out.print("Novo nome: ");
                        String nome = scanner.nextLine();

                        System.out.print("Novo email: ");
                        String email = scanner.nextLine();

                        userService.update(id, nome, email);

                        System.out.println("==============================");
                        System.out.println("Usuário atualizado com sucesso!");
                        viewData(userService);
                        System.out.println("==============================");
                    }

                    case 5 -> {
                        System.out.print("Informe o ID do usuário: ");
                        long id = scanner.nextLong();

                        userService.deleteById(id);

                        System.out.println("==============================");
                        System.out.println("Usuário removido com sucesso!");
                        viewData(userService);
                        System.out.println("==============================");
                    }

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

    public static void viewData(UserService userService) {

        List<User> users = userService.findAll();

        if (users.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            users.forEach(System.out::println);
        }
    }

}
