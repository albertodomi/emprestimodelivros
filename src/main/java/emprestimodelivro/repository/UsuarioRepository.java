package emprestimodelivro.repository;

import emprestimodelivro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Custom queries can be added here if needed, e.g., findByEmail
}