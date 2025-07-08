package emprestimodelivro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import emprestimodelivro.model.Login;
import emprestimodelivro.model.Usuario;
import emprestimodelivro.services.EmprestimoService;
import emprestimodelivro.services.LivroService;
import emprestimodelivro.services.UsuarioService;

@Controller
public class LoginController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private LivroService livroService;
    
    @Autowired
    private EmprestimoService emprestimoService;

    
    
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("login", new Login());
        return "login/form";
    }
    

    
 
    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        Usuario usuario = usuarioService.findByEmail(email);
        
        // Redireciona conforme o tipo de usuário
        if ("ADMIN".equals(String.valueOf(usuario.getTipoUsuario()))) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/usuarioComum/dashboard";
        }
    }
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        
        model.addAttribute("livro", new emprestimodelivro.model.Livro());
        model.addAttribute("usuario", new emprestimodelivro.model.Usuario());
        model.addAttribute("emprestimosAbertos", emprestimoService.findBySituacao("ABERTO"));
    model.addAttribute("emprestimosFinalizados", emprestimoService.findBySituacao("FINALIZADO"));
        return "admin/dashboard";
    }
    
    @GetMapping("/usuarioComum/dashboard")
    public String usuarioComumDashboard(Model model) {
       
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findByEmail(email);
        
       
        model.addAttribute("livros", livroService.findAll()); 
        model.addAttribute("meusEmprestimos", emprestimoService.findEmprestimosAbertosPorUsuario(usuario.getId())); // Para "Meus Empréstimos"
        model.addAttribute("nomeUsuario", usuario.getNome()); 
        
        return "usuarioComum/dashboard";
    }
}