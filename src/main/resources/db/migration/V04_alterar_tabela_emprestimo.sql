-- Adicione ou altere colunas conforme necessário

-- Exemplo: adicionar coluna 'livro' se não existir
ALTER TABLE emprestimo ADD COLUMN IF NOT EXISTS livro VARCHAR(100);

-- Exemplo: adicionar coluna 'data_emprestimo' se não existir
ALTER TABLE emprestimo ADD COLUMN IF NOT EXISTS data_emprestimo DATE NOT NULL;

-- Exemplo: adicionar coluna 'data_devolucao' se não existir
ALTER TABLE emprestimo ADD COLUMN IF NOT EXISTS data_devolucao DATE;

-- Exemplo: adicionar coluna 'status' se não existir
ALTER TABLE emprestimo ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ABERTO';

-- Exemplo: adicionar chave estrangeira para usuario_id se não existir
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_usuario'
          AND table_name = 'emprestimo'
    ) THEN
        ALTER TABLE emprestimo ADD CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id);
    END IF;
END$$;