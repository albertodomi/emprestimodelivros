package emprestimodelivro.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import emprestimodelivro.model.Emprestimo;
import emprestimodelivro.model.Livro;
import emprestimodelivro.model.SituacaoEmprestimo;
import emprestimodelivro.model.SituacaoLivro;
import emprestimodelivro.request.dto.EmprestimoRequest;
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

    @GetMapping
    public String listEmprestimos(Model model) {
        List<Emprestimo> emprestimos = emprestimoService.findAll();
        model.addAttribute("emprestimos", emprestimos);
        return "emprestimo/list";
    }

    @PostMapping("/solicitar-multiplos")
    public String solicitarEmprestimoMultiplos(
            @org.springframework.web.bind.annotation.RequestParam(value = "livroIds", required = false) List<Long> livroIds,
            RedirectAttributes redirectAttributes) {
        if (livroIds == null || livroIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Selecione pelo menos um livro para solicitar empréstimo.");
            redirectAttributes.addFlashAttribute("type", "warning");
            return "redirect:/emprestimos/usuarioComum/dashboard";
        }
        try {
            Long usuarioId = 1L; // Troque pelo ID do usuário logado
            emprestimoService.realizarEmprestimo(usuarioId, livroIds);
            redirectAttributes.addFlashAttribute("message", "Empréstimo(s) solicitado(s) com sucesso!");
            redirectAttributes.addFlashAttribute("type", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao solicitar empréstimo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("type", "error");
        }
        return "redirect:/emprestimos/usuarioComum/dashboard";
    }

 @GetMapping("/usuarioComum/dashboard")
public String dashboard(Model model) {
    Long usuarioId = 1L; // ou pegue do usuário logado
    
    // Buscar TODOS os empréstimos do usuário (com 1 ou mais livros)
    List<Emprestimo> meusEmprestimos = emprestimoService.listarEmprestimosAbertosPorUsuario(usuarioId);
    model.addAttribute("meusEmprestimos", meusEmprestimos);
    
    // Buscar todos os livros disponíveis para mostrar na tela
    List<Livro> livros = livroService.findAll();
    model.addAttribute("livros", livros);
    
    // Debug - adicione este log para verificar se os empréstimos estão sendo carregados
    System.out.println("Todos os empréstimos encontrados: " + meusEmprestimos.size());
    
    return "usuarioComum/dashboard";
}

// Método simples para devolver livros selecionados
@PostMapping("/emprestimos/devolverlivros")
public String devolverLivrosSelecionados(@RequestParam String codigoEmprestimo,
                                        @RequestParam(required = false) List<Long> livrosIds,
                                        RedirectAttributes redirectAttributes) {
    
    // Verificar se algum livro foi selecionado
    if (livrosIds == null || livrosIds.isEmpty()) {
        redirectAttributes.addFlashAttribute("erro", "Selecione pelo menos um livro para devolver!");
        return "redirect:/usuarioComum/dashboard";
    }
    
    try {
        // Chamar o serviço para processar a devolução
        emprestimoService.devolverLivros(codigoEmprestimo, livrosIds);
        
        redirectAttributes.addFlashAttribute("sucesso", 
            "Livros devolvidos com sucesso! Total: " + livrosIds.size());
            
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("erro", 
            "Erro ao devolver livros: " + e.getMessage());
    }
    
    return "redirect:/usuarioComum/dashboard";
}

}