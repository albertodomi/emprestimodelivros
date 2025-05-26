package emprestimodelivro.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Indica que essa classe será mapeada como uma tabela no banco de dados
@Data //adiciona getters, setters, toString, equals e hashCode
@NoArgsConstructor //construtor vazio (obrigatório para o JPA)
@AllArgsConstructor //construtor com todos os campos da classe
public class Usuario {
    @Id // Marca o campo 'id' como chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define que o valor do 'id' será gerado automaticamente pelo banco de dados (auto-incremento)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNasc;

    @OneToMany(mappedBy = "usuario")
    private List<Emprestimo> emprestimos;
}
