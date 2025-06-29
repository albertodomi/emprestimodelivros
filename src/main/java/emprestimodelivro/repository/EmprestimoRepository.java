package emprestimodelivro.repository;

import emprestimodelivro.model.Emprestimo;
import emprestimodelivro.model.SituacaoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    @Query("SELECT e FROM Emprestimo e WHERE e.usuario.id = :usuarioId AND e.situacao = :situacao")
    List<Emprestimo> findAbertosByUsuario(@Param("usuarioId") Long usuarioId, @Param("situacao") SituacaoEmprestimo situacao);

    Optional<Emprestimo> findByCodigo(String codigo);
}