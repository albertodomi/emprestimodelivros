package emprestimodelivro.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @ManyToOne
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "emprestimo_livro",
        joinColumns = @JoinColumn(name = "emprestimo_id"),
        inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    private List<Livro> livros;

    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    private SituacaoEmprestimo status;

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Emprestimo other = (Emprestimo) obj;
        return Objects.equals(codigo, other.codigo);
    }

    @Override
    public String toString() {
        return "Emprestimo [codigo=" + codigo +
               ", usuario=" + usuario +
               ", livros=" + livros +
               ", dataEmprestimo=" + dataEmprestimo +
               ", dataDevolucao=" + dataDevolucao +
               ", status=" + status + "]";
    }
}

