package br.com.deloittebt.crud.presentation;

import br.com.deloittebt.crud.model.User;
import br.com.deloittebt.crud.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador responsável por lidar com as requisições HTTP relacionadas à gestão de usuários.
 * Atua como a camada de apresentação, interagindo com o serviço de usuários e retornando
 * as views apropriadas.
 */
@Controller
public class HomeController {

    private final UserService userService;

    /**
     * Construtor para injeção de dependência do UserService.
     * @param userService O serviço de usuários.
     */
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Exibe a página inicial com a lista de todos os usuários.
     * @param model O modelo para adicionar atributos à view.
     * @return O nome da view "index".
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "index";
    }

    /**
     * Exibe o formulário para criação de um novo usuário.
     * @param model O modelo para adicionar atributos à view.
     * @return O nome da view "form".
     */
    @GetMapping("/novo")
    public String mostrarFormularioDeCriacao(Model model) {
        model.addAttribute("user", new User()); // Envia um objeto User vazio para o formulário
        return "form";
    }

    /**
     * Salva um novo usuário ou atualiza um usuário existente.
     * Redireciona para a página inicial após a operação.
     * @param id O ID do usuário (nulo para novos usuários, preenchido para edição).
     * @param name O nome do usuário.
     * @param email O email do usuário.
     * @return Redirecionamento para a página inicial.
     */
    @PostMapping("/salvar")
    public String salvarUsuario(@RequestParam(required = false) Long id, @RequestParam String name, @RequestParam String email) {
        if (id == null) {
            // Refatoração: o UserService agora lida com a criação da instância de User
            userService.create(name, email);
        } else {
            userService.update(id, name, email);
        }
        return "redirect:/";
    }

    /**
     * Busca um usuário pelo ID e exibe seus detalhes.
     * Em caso de erro (usuário não encontrado), exibe uma mensagem de erro.
     * @param id O ID do usuário a ser buscado.
     * @param model O modelo para adicionar atributos à view.
     * @return O nome da view "usuario-detalhe".
     */
    @GetMapping("/buscar")
    public String buscarUsuarioPorId(@RequestParam Long id, Model model) {
        try {
            User user = userService.findById(id);
            model.addAttribute("usuarioEncontrado", user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
        }
        return "usuario-detalhe";
    }

    /**
     * Exclui um usuário pelo ID e redireciona para a página inicial.
     * @param id O ID do usuário a ser excluído.
     * @return Redirecionamento para a página inicial.
     */
    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/";
    }

    /**
     * Exibe o formulário pré-preenchido para edição de um usuário existente.
     * @param id O ID do usuário a ser editado.
     * @param model O modelo para adicionar atributos à view.
     * @return O nome da view "form".
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEdicao(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "form";
    }
}
