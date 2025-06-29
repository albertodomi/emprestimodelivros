package emprestimodelivro.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import emprestimodelivro.model.Usuario;
import emprestimodelivro.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    //ROTA PARA ACESSAR O FORMULARIO DE CADASTRO
    @GetMapping("/novo")
    public String novoUsuarioForm(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "usuarios/form";
}

    // CRIAR USUÁRIO
    @PostMapping("/novo")
    public String criarUsuario(@RequestParam String nome,
                               @RequestParam String email,
                               @RequestParam(required = false) String telefone,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNasc,
                               RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setTelefone(telefone);
            usuario.setDataNasc(dataNasc);

            usuarioService.save(usuario);

            redirectAttributes.addFlashAttribute("mensagem", "Usuário '" + nome + "' criado com sucesso!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao criar usuário: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/admin/dashboard";
    }

    // LISTAR USUÁRIOS
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/list";
    }

    // Exibir formulário de edição
    @GetMapping("/editar/{id}")
    public String editarUsuarioForm(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }

    // Salvar edição
    @PostMapping("/editar/{id}")
    public String atualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        usuario.setId(id);
        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/usuarios";
    }

    // Deletar usuário
    @GetMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário deletado com sucesso!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/usuarios";
    }
}