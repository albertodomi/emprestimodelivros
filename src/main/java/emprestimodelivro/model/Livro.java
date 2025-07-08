package emprestimodelivro.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    @Column(nullable = false)
    private String autor;
    
    @NotNull(message = "Ano de publicação é obrigatório")
    @Min(value = 1000, message = "Ano de publicação deve ser válido")
    @Max(value = 2100, message = "Ano de publicação deve ser válido")
    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    @NotNull(message = "Situação é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoLivro situacao = SituacaoLivro.DISPONIVEL;

    @NotNull(message = "Categoria é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria = Categoria.OUTROS;
    
    @ManyToMany(mappedBy = "livros")
    private List<Emprestimo> emprestimos = new ArrayList<>();
    
    // Definindo enum Categoria
    public enum Categoria {
        FICCAO("Ficção"),
        NAO_FICCAO("Não Ficção"),
        ROMANCE("Romance"),
        BIOGRAFIA("Biografia"),
        TECNOLOGIA("Tecnologia"),
        CIENCIA("Ciência"),
        HISTORIA("História"),
        INFANTIL("Infantil"),
        AUTOAJUDA("Autoajuda"),
        POESIA("Poesia"),
        EDUCACIONAL("Educacional"),
        RELIGIAO("Religião"),
        CULINARIA("Culinária"),
        OUTROS("Outros");
        
        private final String displayValue;
        
        Categoria(String displayValue) {
            this.displayValue = displayValue;
        }
        
        public String getDisplayValue() {
            return displayValue;
        }
        
        @Override
        public String toString() {
            return displayValue;
        }
    }
    
    // Construtores
    public Livro() {
    }
    
    public Livro(Long id, String titulo, String autor, Integer anoPublicacao, SituacaoLivro situacao, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.situacao = situacao;
        this.categoria = categoria;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }
    
    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }
    
    public SituacaoLivro getSituacao() {
        return situacao;
    }
    
    public void setSituacao(SituacaoLivro situacao) {
        this.situacao = situacao;
    }
    
    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
    
    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
    
    // Adicionando getter e setter para categoria
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    // equals, hashCode e toString atualizados
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Livro livro = (Livro) o;
        return id != null && id.equals(livro.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Livro{" +
               "id=" + id +
               ", titulo='" + titulo + '\'' +
               ", autor='" + autor + '\'' +
               ", anoPublicacao=" + anoPublicacao +
               ", situacao=" + situacao +
               ", categoria=" + categoria +
               '}';
    }
}