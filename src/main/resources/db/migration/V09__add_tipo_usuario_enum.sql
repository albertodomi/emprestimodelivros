-- Cria o tipo ENUM no PostgreSQL se não existir
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tipo_usuario') THEN
        CREATE TYPE tipo_usuario AS ENUM ('ADMIN', 'CLIENT');
    END IF;
END $$;

-- Adiciona a coluna permitindo nulo temporariamente
ALTER TABLE usuarios ADD COLUMN tipo_usuario tipo_usuario;

-- Preenche os usuários antigos como CLIENT
UPDATE usuarios SET tipo_usuario = 'CLIENT' WHERE tipo_usuario IS NULL;

-- Agora torna a coluna NOT NULL e define o default
ALTER TABLE usuarios ALTER COLUMN tipo_usuario SET NOT NULL;
ALTER TABLE usuarios ALTER COLUMN tipo_usuario SET DEFAULT 'CLIENT';