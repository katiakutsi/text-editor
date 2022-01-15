package ge.tsu.texteditor.texteditor.db.repository;

import ge.tsu.texteditor.texteditor.db.Database;
import ge.tsu.texteditor.texteditor.db.model.OpenedFile;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class OpenedFileRepository {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS OPENEDFILES (ID INT PRIMARY KEY AUTO_INCREMENT, FILENAME VARCHAR NOT NULL);";
    private static final String INSERT = "INSERT INTO OPENEDFILES(FILENAME) VALUES('%s')";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS OPENEDFILES;";

    public void createTable() throws SQLException {
        try (Statement statement = Database.getConnection().createStatement()) {
            statement.executeUpdate(CREATE_TABLE);
        }
    }

    public void dropTable() throws SQLException {
        try (Statement statement = Database.getConnection().createStatement()) {
             statement.executeUpdate(DROP_TABLE);
        }
    }

    @SneakyThrows
    public OpenedFile save(OpenedFile file) {
        try (Statement statement = Database.getConnection().createStatement()) {
            int insertedRows = statement.executeUpdate(
                    String.format(INSERT, file.getFileName()),
                    Statement.RETURN_GENERATED_KEYS
            );
            if (insertedRows > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    file.setId(resultSet.getInt(1));
                    return file;
                }
            } else {
                log.error("File has not been opened: " + file.getFileName());
            }
        }
        return null;
    }

    @SneakyThrows
    public static void main(String[] args) {
        OpenedFileRepository openedFileRepository = new OpenedFileRepository();
        openedFileRepository.dropTable();
    }
}