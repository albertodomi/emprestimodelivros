package emprestimodelivro.model;

public enum StatusEmprestimo {
    ATIVO,          // O empréstimo está em andamento
    DEVOLVIDO,      // O livro foi devolvido
    ATRASADO,       // O livro está em atraso
    CANCELADO       // O empréstimo foi cancelado
}