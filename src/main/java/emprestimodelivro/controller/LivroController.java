package emprestimodelivro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import emprestimodelivro.model.Livro;
import emprestimodelivro.model.Livro.Categoria;
import emprestimodelivro.model.SituacaoLivro;
import emprestimodelivro.services.LivroService;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;



    //CRIAR LIVRO
    @PostMapping("/novo")
    public String criarLivro(@RequestParam String titulo,
                             @RequestParam String autor,
                             @RequestParam(required = false) Integer anoPublicacao,
                             @RequestParam SituacaoLivro situacao,
                             @RequestParam(required = false) String categoria,
                             RedirectAttributes redirectAttributes) {
        try {
            
            Livro livro = new Livro();
            livro.setTitulo(titulo);
            livro.setAutor(autor);
            livro.setAnoPublicacao(anoPublicacao);
            livro.setSituacao(situacao);
            
            
            if (categoria != null && !categoria.isEmpty()) {
                
                livro.setCategoria(Categoria.valueOf(categoria));
            }
            
           
            livroService.save(livro);
            
            redirectAttributes.addFlashAttribute("message", "Livro '" + titulo + "' criado com sucesso!");
            redirectAttributes.addFlashAttribute("type", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao criar livro: " + e.getMessage());
            redirectAttributes.addFlashAttribute("type", "error");
        }
        return "redirect:/admin/dashboard";
    }

   
    // Excluir livro
    @GetMapping("/deletar/{id}")
    public String deleteLivro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            livroService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Livro excluído com sucesso!");
            redirectAttributes.addFlashAttribute("type", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao excluir livro: " + e.getMessage());
            redirectAttributes.addFlashAttribute("type", "error");
        }
        return "redirect:/livros";
    }

   //listar todos os livros 
    @GetMapping
    public String listarLivros(Model model) {
        List<Livro> livros = livroService.findAll();
        model.addAttribute("livros", livros);
        return "livros/list"; //DIRECIONA PARA OUTRA PAGINA LISTANDO OS LIVROS (LA TEM COMO EXCLUIR e ATUALIZAR)
        
    }
    
    // Atualiza a situação de um livro entre DISPONIVEL e EMPRESTADO
    @GetMapping("/atualizar-situacao/{id}")
    public String atualizarSituacaoLivro(@PathVariable Long id, 
                                  @RequestParam SituacaoLivro novaSituacao,
                                  RedirectAttributes redirectAttributes) {
        try {
            
            Livro livroAtualizado = livroService.atualizarSituacao(id, novaSituacao);
            
            
            redirectAttributes.addFlashAttribute("message", 
                "Situação do livro '" + livroAtualizado.getTitulo() + "' atualizada para " + novaSituacao);
            redirectAttributes.addFlashAttribute("type", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("type", "warning");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao atualizar situação: " + e.getMessage());
            redirectAttributes.addFlashAttribute("type", "error");
        }
        
        return "redirect:/livros";
    }
    
    // Buscar livros por título ou categoria
    @GetMapping("/buscar")
    public String buscarLivros(@RequestParam(required = false) String termo, Model model) {
        List<Livro> livros = (termo == null || termo.isBlank())
            ? livroService.findAll()
            : livroService.buscarPorTituloAutorOuCategoria(termo);
        model.addAttribute("livros", livros);
        return "usuarioComum/dashboard";
    }
}