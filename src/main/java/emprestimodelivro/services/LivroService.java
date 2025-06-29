package emprestimodelivro.services;

import emprestimodelivro.model.Livro;
import emprestimodelivro.model.SituacaoLivro;
import emprestimodelivro.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public List<Livro> findAll() {
        return livroRepository.findAll();
    }

    public Optional<Livro> findById(Long id) {
        return livroRepository.findById(id);
    }

    public Livro save(Livro livro) {
        return livroRepository.save(livro);
    }

    public void deleteById(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new IllegalArgumentException("Livro não encontrado para exclusão.");
        }
        livroRepository.deleteById(id);
    }

    public Livro atualizar(Long id, Livro dadosAtualizados) {
        Livro livro = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de livro inválido: " + id));
        livro.setTitulo(dadosAtualizados.getTitulo());
        livro.setAutor(dadosAtualizados.getAutor());
        livro.setAnoPublicacao(dadosAtualizados.getAnoPublicacao());
        livro.setCategoria(dadosAtualizados.getCategoria());
        return save(livro);
    }

    public Livro atualizarSituacao(Long id, SituacaoLivro novaSituacao) {
        Livro livro = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de livro inválido: " + id));
        if ((livro.getSituacao() == SituacaoLivro.DISPONIVEL || livro.getSituacao() == SituacaoLivro.EMPRESTADO) &&
            (novaSituacao == SituacaoLivro.DISPONIVEL || novaSituacao == SituacaoLivro.EMPRESTADO)) {
            livro.setSituacao(novaSituacao);
            return save(livro);
        } else {
            throw new IllegalArgumentException(
                "A alteração de situação só é permitida entre DISPONIVEL e EMPRESTADO");
        }
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> buscarPorTituloAutorOuCategoria(String termo) {
        return livroRepository.buscarPorTituloAutorOuCategoria(termo);
    }
}