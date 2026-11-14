package com.transportadora.util;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Cria o EntityManagerFactory lendo as credenciais do banco de variáveis de ambiente
 * ou do arquivo .env na raiz do projeto (variáveis de ambiente têm prioridade).
 *
 * Variáveis esperadas: DB_URL, DB_USER, DB_PASSWORD (veja o .env.example).
 */
public class JPAUtil {

    private static final String PERSISTENCE_UNIT = "db_frete_transportadora";
    private static final String URL_PADRAO =
            "jdbc:mysql://localhost:3306/db_frete_transportadora?createDatabaseIfNotExist=true";

    private static final EntityManagerFactory FACTORY = criarFactory();

    private static EntityManagerFactory criarFactory() {
        Dotenv env = Dotenv.configure().ignoreIfMissing().load();

        String usuario = env.get("DB_USER");
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalStateException(
                    "Variável DB_USER não definida. Crie um arquivo .env a partir do .env.example.");
        }

        Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", env.get("DB_URL", URL_PADRAO));
        props.put("jakarta.persistence.jdbc.user", usuario);
        props.put("jakarta.persistence.jdbc.password", env.get("DB_PASSWORD", ""));

        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, props);
    }

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}
