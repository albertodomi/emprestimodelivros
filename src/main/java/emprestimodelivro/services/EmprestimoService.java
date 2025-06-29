package emprestimodelivro.services;

import java.time.LocalDate; // Importa a classe Emprestimo
import java.time.LocalDateTime;
import java.util.List; // Importa a classe Livro
import java.util.Optional; // Importa o enum SituacaoEmprestimo

import org.springframework.stereotype.Service; // Importa o enum SituacaoLivro
import org.springframework.transaction.annotation.Transactional; // Importa a classe Usuario

import emprestimodelivro.model.Emprestimo; // Importa o EmprestimoRepository
import emprestimodelivro.model.Livro; // Importa o LivroRepository (NOVO)
import emprestimodelivro.model.SituacaoEmprestimo; // Importa o UsuarioRepository (NOVO)
import emprestimodelivro.model.SituacaoLivro;
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

    // --- Lógica de Negócios ---

    @Transactional 
    public Emprestimo realizarEmprestimo(Long usuarioId, List<Long> livroIds) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId)); // Busca o usuário

        List<Livro> livros = livroRepository.findAllById(livroIds); // Busca todos os livros pelos IDs

        if (livros.size() != livroIds.size()) {
            throw new RuntimeException("Um ou mais livros não foram encontrados.");
        }

        for (Livro livro : livros) {
            if (livro.getSituacao() == SituacaoLivro.EMPRESTADO) { // Verifica se o livro está disponível
                throw new RuntimeException("O livro '" + livro.getTitulo() + "' não está disponível para empréstimo.");
            }
        }

        Emprestimo emprestimo = new Emprestimo(); // Cria um novo empréstimo
        emprestimo.setUsuario(usuario); // Define o usuário do empréstimo
        emprestimo.setLivros(livros); // Define os livros do empréstimo
        emprestimo.setData_emprestimo(LocalDate.now()); // Define a data de empréstimo como hoje
        emprestimo.setSituacao(SituacaoEmprestimo.ABERTO); // Define o status como ABERTO

        // Salva o empréstimo para gerar o ID, se necessário para o relacionamento ManyToMany
        Emprestimo savedEmprestimo = emprestimoRepository.save(emprestimo);

        // Atualiza o status dos livros para EMPRESTADO
        for (Livro livro : livros) {
            livro.setSituacao(SituacaoLivro.EMPRESTADO); // Altera o status do livro
            livroRepository.save(livro);
        }

        return savedEmprestimo;
    }


@Transactional
public void devolverLivros(String codigoEmprestimo, List<Long> livrosIds) {
    Emprestimo emprestimo = emprestimoRepository.findByCodigo(codigoEmprestimo)
        .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

    for (Long livroId : livrosIds) {
        Livro livro = emprestimo.getLivros().stream()
            .filter(l -> l.getId().equals(livroId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Livro não encontrado no empréstimo"));

        livro.setSituacao(SituacaoLivro.DISPONIVEL);
        livroRepository.save(livro);
    }

    // Verifica se todos os livros do empréstimo estão DISPONIVEL
    boolean todosDevolvidos = emprestimo.getLivros().stream()
        .allMatch(l -> l.getSituacao() == SituacaoLivro.DISPONIVEL);

    if (todosDevolvidos) {
        emprestimo.setSituacao(SituacaoEmprestimo.FINALIZADO);
        emprestimo.setData_devolucao(LocalDate.now());
    }

    emprestimoRepository.save(emprestimo);
}
}
