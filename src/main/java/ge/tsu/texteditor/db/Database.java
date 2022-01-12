package ge.tsu.texteditor.db;

import lombok.Getter;
import lombok.SneakyThrows;
import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;
import java.sql.Connection;

public class Database {

    private static Database INSTANCE;

    @Getter
    private DataSource dataSource;

    private Database() {
        DBConfig config = DBConfig.getInstance();
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(config.getJdbcUrl());
        dataSource.setUser(config.getUsername());
        dataSource.setPassword(config.getPassword());
        this.dataSource = dataSource;
    }

    public static synchronized Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

    @SneakyThrows
    public static Connection getConnection() {
        return getInstance().getDataSource().getConnection();
    }
}