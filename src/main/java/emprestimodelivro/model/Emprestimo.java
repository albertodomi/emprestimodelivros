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
    private Usuario pessoa;
    //@ManyToOne
    //private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status;

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Emprestimo other = (Emprestimo) obj;
        return Objects.equals(codigo, other.codigo);
    }

    @Override
    public String toString() {
        return "Emprestimo [codigo=" + codigo + ", pessoa=" + pessoa + /* ", livro=" + livro +*/
               ", dataEmprestimo=" + dataEmprestimo + ", dataDevolucao=" + dataDevolucao +
               ", status=" + status + "]";
    }
}
