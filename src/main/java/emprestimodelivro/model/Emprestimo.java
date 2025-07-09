package emprestimodelivro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emprestimos")
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "emprestimo_livro",
        joinColumns = @JoinColumn(name = "emprestimo_id"),
        inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    private List<Livro> livros = new ArrayList<>();
    
    // Adicione o campo dataEmprestimo
    @Column(name = "data_emprestimo")
    private LocalDate data_emprestimo;
    
    @Column(name = "data_devolucao")
    private LocalDate data_devolucao;

    // Campo situação já existente
    @Enumerated(EnumType.STRING)
    private SituacaoEmprestimo situacao = SituacaoEmprestimo.ABERTO;

    // Construtores
    public Emprestimo() {
        this.data_emprestimo = LocalDate.now(); // Define a data atual por padrão
    }
    
    public Emprestimo(Usuario usuario) {
        this();
        this.usuario = usuario;
    }
    
    // Getters e Setters existentes
    public Long getCodigo() {
        return codigo;
    }
    
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public List<Livro> getLivros() {
        return livros;
    }
    
    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
    
    public SituacaoEmprestimo getSituacao() {
        return situacao;
    }
    
    public void setSituacao(SituacaoEmprestimo situacao) {
        this.situacao = situacao;
    }
    
    // Métodos úteis
    public void adicionarLivro(Livro livro) {
        if (livro != null && !this.livros.contains(livro)) {
            this.livros.add(livro);
        }
    }
    
    public void removerLivro(Livro livro) {
        this.livros.remove(livro);
    }
    
    public void finalizar() {
        this.situacao = SituacaoEmprestimo.FINALIZADO;
    }

    public LocalDate getData_emprestimo() {
        return data_emprestimo;
    }

    public void setData_emprestimo(LocalDate data_emprestimo) {
        this.data_emprestimo = data_emprestimo;
    }

    public LocalDate getData_devolucao() {
        return data_devolucao;
    }

    public void setData_devolucao(LocalDate data_devolucao) {
        this.data_devolucao = data_devolucao;
    }
}
