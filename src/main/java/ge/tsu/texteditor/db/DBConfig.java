package ge.tsu.texteditor.db;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Properties;

public class DBConfig {

    private static DBConfig INSTANCE;

    @Getter
    private String jdbcUrl;

    @Getter
    private String username;

    @Getter
    private String password;

    @SneakyThrows
    private DBConfig() {
        Properties props = new Properties();

        try(InputStream inputStream = DBConfig.class.getResourceAsStream("/db.properties")) {
            props.load(inputStream);
        }

        jdbcUrl = props.getProperty("jdbc-url");
        username = props.getProperty("username");
        password = props.getProperty("password");
    }

    public static synchronized DBConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBConfig();
        }
        return INSTANCE;
    }
}
