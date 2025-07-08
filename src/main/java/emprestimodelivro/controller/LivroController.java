package emprestimodelivro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import emprestimodelivro.model.Livro;
import emprestimodelivro.model.SituacaoLivro;
import emprestimodelivro.model.Usuario;
import emprestimodelivro.services.LivroService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping("/novo")
    public String criarLivro(@Valid Livro livro,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("livro", livro);
            model.addAttribute("usuario", new Usuario()); // se precisar para outro form
            return "admin/dashboard";
        }

        livroService.save(livro);
        redirectAttributes.addFlashAttribute("message", "Livro '" + livro.getTitulo() + "' criado com sucesso!");
        redirectAttributes.addFlashAttribute("type", "success");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/deletar/{id}")
    public String deleteLivro(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            livroService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Livro excluído com sucesso!");
            redirectAttributes.addFlashAttribute("type", "success");
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("violates foreign key constraint")) {
                redirectAttributes.addFlashAttribute("message",
                        "Não é possível excluir o livro pois ele está vinculado a um ou mais empréstimos.");
            } else {
                redirectAttributes.addFlashAttribute("message", "Erro ao excluir livro: " + e.getMessage());
            }
            redirectAttributes.addFlashAttribute("type", "error");
        }
        return "redirect:/livros";
    }

    @GetMapping
    public String listarLivros(Model model) {
        List<Livro> livros = livroService.findAll();
        model.addAttribute("livros", livros);
        return "livros/list";
    }

    @PostMapping("/atualizar-situacao/{id}")
    public String atualizarSituacaoLivro(@PathVariable Long id,
            @RequestParam("novaSituacao") SituacaoLivro novaSituacao,
            RedirectAttributes redirectAttributes) {

        try {
            Livro livroAtualizado = livroService.atualizarSituacao(id, novaSituacao);
            redirectAttributes.addFlashAttribute("message", "Situação do livro alterada com sucesso!");
            redirectAttributes.addFlashAttribute("type", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("type", "error");
        }
        return "redirect:/livros";
    }

    @GetMapping("/buscar")
    public String buscarLivros(@RequestParam(required = false) String termo, Model model) {
        List<Livro> livros = (termo == null || termo.isBlank())
                ? livroService.findAll()
                : livroService.buscarPorTituloAutorOuCategoria(termo);
        model.addAttribute("livros", livros);
        return "usuarioComum/dashboard";
    }
}