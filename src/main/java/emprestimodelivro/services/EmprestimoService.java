package emprestimodelivro.services;

import java.time.LocalDate; // Importa a classe Emprestimo
import java.util.List;
import java.util.Optional; // Importa a classe Livro

import org.springframework.stereotype.Service; // Importa o enum SituacaoEmprestimo
import org.springframework.transaction.annotation.Transactional; // Importa o enum SituacaoLivro

import emprestimodelivro.model.Emprestimo; // Importa a classe Usuario
import emprestimodelivro.model.Livro; // Importa o EmprestimoRepository
import emprestimodelivro.model.SituacaoEmprestimo; // Importa o LivroRepository (NOVO)
import emprestimodelivro.model.SituacaoLivro; // Importa o UsuarioRepository (NOVO)
import emprestimodelivro.model.Usuario;
import emprestimodelivro.repository.EmprestimoRepository;
import emprestimodelivro.repository.LivroRepository;
import emprestimodelivro.repository.UsuarioRepository;

@Service
public class EmprestimoService {

    private EmprestimoRepository emprestimoRepository;
    private LivroRepository livroRepository;
    private UsuarioRepository usuarioRepository;

    // Construtor com @Autowired (opcional no Spring moderno)
    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                           LivroRepository livroRepository,
                           UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }
    public List<Emprestimo> findAll() {
        return emprestimoRepository.findAll();
    }

    public Optional<Emprestimo> findById(Long id) {
        return emprestimoRepository.findById(id);
    }

    // Método para alterar um empréstimo existente
    public void alterar(Emprestimo emprestimo) {
        emprestimoRepository.save(emprestimo);
    }

    public void deleteById(Long id) {
        emprestimoRepository.deleteById(id);
    }

    public List<Emprestimo> findEmprestimosAbertosPorUsuario(Long usuarioId) {
    return emprestimoRepository.findEmprestimosAbertos(usuarioId, SituacaoEmprestimo.ABERTO);
    }


    @Transactional
    public Emprestimo realizarEmprestimo(Long usuarioId, List<Long> livroIds) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        // ✅ ADICIONAR: Validação de empréstimos abertos
        List<Emprestimo> emprestimosAbertos = findEmprestimosAbertosPorUsuario(usuarioId);
        if (!emprestimosAbertos.isEmpty()) {
            throw new RuntimeException("Você já possui empréstimo(s) em aberto. Devolva os livros antes de solicitar novos empréstimos.");
        }

        List<Livro> livros = livroRepository.findAllById(livroIds);

        if (livros.size() != livroIds.size()) {
            throw new RuntimeException("Um ou mais livros não foram encontrados.");
        }

        for (Livro livro : livros) {
            if (livro.getSituacao() == SituacaoLivro.EMPRESTADO) { 
                throw new RuntimeException("O livro '" + livro.getTitulo() + "' não está disponível para empréstimo.");
            }
        }

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivros(livros);
        emprestimo.setData_emprestimo(LocalDate.now());
        emprestimo.setSituacao(SituacaoEmprestimo.ABERTO);

        Emprestimo savedEmprestimo = emprestimoRepository.save(emprestimo);

        for (Livro livro : livros) {
            livro.setSituacao(SituacaoLivro.EMPRESTADO);
            livroRepository.save(livro);
        }

        return savedEmprestimo;
    }

    public void devolverLivros(Long emprestimoId, List<Long> livrosIds) {
    Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
        .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

    for (Livro livro : emprestimo.getLivros()) {
        if (livrosIds.contains(livro.getId())) {
            livro.setSituacao(SituacaoLivro.DISPONIVEL);
            livroRepository.save(livro);
        }
    }

    // Recarregue os livros do banco para garantir o status atualizado
    List<Livro> livrosAtualizados = livroRepository.findAllById(
        emprestimo.getLivros().stream().map(Livro::getId).toList()
    );

    boolean todosDevolvidos = livrosAtualizados.stream()
        .allMatch(l -> l.getSituacao() == SituacaoLivro.DISPONIVEL);

    if (todosDevolvidos) {
        emprestimo.setSituacao(SituacaoEmprestimo.FINALIZADO);
    } else {
        emprestimo.setSituacao(SituacaoEmprestimo.ABERTO);
    }
    emprestimoRepository.save(emprestimo);
    }


public List<Emprestimo> findBySituacao(String situacao) {
    SituacaoEmprestimo enumSituacao = SituacaoEmprestimo.valueOf(situacao);
    System.out.println("Buscando empréstimos com situação: " + enumSituacao);
    
    List<Emprestimo> resultado = emprestimoRepository.findBySituacao(enumSituacao);
    System.out.println("Resultados encontrados: " + resultado.size());
    
    // Log adicional para debug
    for (Emprestimo emp : resultado) {
        System.out.println("Empréstimo " + emp.getCodigo() + " - Situação: " + emp.getSituacao());
    }
    
    
    return resultado;
    }

}
