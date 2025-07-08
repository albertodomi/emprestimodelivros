package emprestimodelivro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import emprestimodelivro.model.Usuario;
import emprestimodelivro.services.UsuarioService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/novo")
public String criarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
        BindingResult result,
        Model model,
        RedirectAttributes redirectAttributes) {

    if (result.hasErrors()) {
        model.addAttribute("usuario", usuario);
        model.addAttribute("livro", new emprestimodelivro.model.Livro()); // para o outro form
        return "admin/dashboard"; 
    }

    try {
        usuarioService.save(usuario);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário criado com sucesso!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("mensagem", "Erro ao criar usuário: " + e.getMessage());
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
    }
    
    return "redirect:/admin/dashboard";
}

    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/list";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuarioForm(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }

    @PostMapping("/editar/{id}")
    public String atualizarUsuario(@PathVariable Long id,
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "usuarios/form";
        }

        try {
            usuario.setId(id);
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao atualizar usuário: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário deletado com sucesso!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao deletar usuário: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        
        return "redirect:/usuarios";
    }
}