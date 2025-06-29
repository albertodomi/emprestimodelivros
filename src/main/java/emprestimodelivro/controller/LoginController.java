package emprestimodelivro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm() {
        return "login/form";
    }
    
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String email, 
            @RequestParam String senha,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Simulação simples - aceita qualquer usuário/senha
        session.setAttribute("usuarioLogado", true);
        session.setAttribute("nomeUsuario", "Usuário Demonstração");
        session.setAttribute("emailUsuario", email);
        
        // Redireciona para o dashboard após login
        return "redirect:/usuarioComum/dashboard";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/index";
    }

     @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/usuarioComum/dashboard")
    public String usuarioComumDashboard() {
        return "usuarioComum/dashboard";
    }
}