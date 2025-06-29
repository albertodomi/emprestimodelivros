package emprestimodelivro.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity // Indica que essa classe será mapeada como uma tabela no banco de dados
@Table(name = "usuarios")
public class Usuario {
    @Id // Marca o campo 'id' como chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define que o valor do 'id' será gerado automaticamente pelo banco de dados (auto-incremento)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(length = 20)
    private String telefone;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNasc;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Emprestimo> emprestimos = new ArrayList<>();
    
    // Construtores
    public Usuario() {
    }
    
    public Usuario(Long id, String nome, String email, String telefone, LocalDate dataNasc) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNasc = dataNasc;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public LocalDate getDataNasc() {
        return dataNasc;
    }
    
    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }
    
    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
    
    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
    
    // Métodos úteis
    public void addEmprestimo(Emprestimo emprestimo) {
        emprestimos.add(emprestimo);
        emprestimo.setUsuario(this);
    }
    
    public void removeEmprestimo(Emprestimo emprestimo) {
        emprestimos.remove(emprestimo);
        emprestimo.setUsuario(null);
    }
    
    // Métodos equals, hashCode e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Usuario usuario = (Usuario) o;
        return id != null && id.equals(usuario.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", email='" + email + '\'' +
               ", telefone='" + telefone + '\'' +
               ", dataNasc=" + dataNasc +
               '}';
    }
}
