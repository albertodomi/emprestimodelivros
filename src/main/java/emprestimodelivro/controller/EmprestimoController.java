package emprestimodelivro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import emprestimodelivro.model.Emprestimo;
import emprestimodelivro.model.Livro;
import emprestimodelivro.model.Usuario;
import emprestimodelivro.services.EmprestimoService;
import emprestimodelivro.services.LivroService;
import emprestimodelivro.services.UsuarioService;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private LivroService livroService;

    @PostMapping("/solicitar")
    public String solicitarEmprestimo(
            @RequestParam(value = "livroIds", required = false) List<Long> livroIds,
            RedirectAttributes redirectAttributes) {
        
        if (livroIds == null || livroIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Selecione pelo menos um livro para solicitar empréstimo.");
            redirectAttributes.addFlashAttribute("type", "warning");
            return "redirect:/usuarioComum/dashboard";
        }
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Usuario usuario = usuarioService.findByEmail(email);
            
            emprestimoService.realizarEmprestimo(usuario.getId(), livroIds);
            redirectAttributes.addFlashAttribute("message", "Empréstimo(s) solicitado(s) com sucesso!");
            redirectAttributes.addFlashAttribute("type", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao solicitar empréstimo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("type", "error");
        }
        return "redirect:/usuarioComum/dashboard";
    }

    @GetMapping("/usuarioComum/dashboard")
    public String emprestimosAbertos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.findByEmail(email);
        
        List<Emprestimo> meusEmprestimos = emprestimoService.findEmprestimosAbertosPorUsuario(usuario.getId());
        List<Livro> livros = livroService.findAll();
        
        model.addAttribute("meusEmprestimos", meusEmprestimos);
        model.addAttribute("livros", livros);
        model.addAttribute("nomeUsuario", usuario.getNome());
        
        return "usuarioComum/dashboard";
    }

    @PostMapping("/devolverlivros")
    public String devolverLivrosSelecionados(
            @RequestParam("codigoEmprestimo") Long codigoEmprestimo,
            @RequestParam(value = "livrosIds", required = false) List<Long> livrosIds,
            RedirectAttributes redirectAttributes) {

        if (livrosIds == null || livrosIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Selecione pelo menos um livro para devolver.");
            redirectAttributes.addFlashAttribute("type", "warning");
            return "redirect:/usuarioComum/dashboard";
        }

        try {
            emprestimoService.devolverLivros(codigoEmprestimo, livrosIds);
            redirectAttributes.addFlashAttribute("message", "Livro(s) devolvido(s) com sucesso!");
            redirectAttributes.addFlashAttribute("type", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao devolver livro(s): " + e.getMessage());
            redirectAttributes.addFlashAttribute("type", "error");
        }
        return "redirect:/usuarioComum/dashboard";
    }

    @GetMapping("/admin/painel")
    public String verEmprestimosAdmin(Model model) {
        List<Emprestimo> emprestimosAbertos = emprestimoService.findBySituacao("ABERTO");
        List<Emprestimo> emprestimosFinalizados = emprestimoService.findBySituacao("FINALIZADO");
        model.addAttribute("emprestimosAbertos", emprestimosAbertos);
        model.addAttribute("emprestimosFinalizados", emprestimosFinalizados);
        
        if (!model.containsAttribute("livro")) {
            model.addAttribute("livro", new emprestimodelivro.model.Livro());
        }
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new emprestimodelivro.model.Usuario());
        }
        return "admin/emprestimos/painel";
    }
}