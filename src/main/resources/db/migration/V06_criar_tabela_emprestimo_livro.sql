CREATE TABLE emprestimo_livro (
    emprestimo_id BIGINT NOT NULL,
    livro_id BIGINT NOT NULL,
    PRIMARY KEY (emprestimo_id, livro_id),
    FOREIGN KEY (emprestimo_id) REFERENCES emprestimo(codigo),
    FOREIGN KEY (livro_id) REFERENCES livro(id)
);
--relacionamento N para N entre empréstimo e livro