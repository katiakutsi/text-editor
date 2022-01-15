package ge.tsu.texteditor;

import ge.tsu.texteditor.texteditor.db.model.OpenedFile;
import ge.tsu.texteditor.texteditor.db.repository.OpenedFileRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.SQLException;

public class OpenedFileRepositoryTest {

    private static OpenedFileRepository OPENEDFILEREPOSITORY;
    private static OpenedFile openedFile = new OpenedFile("/Users/tbc/Desktop/Screenshot 2021-10-22 at 12.49.00 AM");

    @BeforeAll
    static void beforeAll() {
        OPENEDFILEREPOSITORY = new OpenedFileRepository();
    }

    @Test
    void testCreateTable() {
        Assertions.assertThrows(SQLException.class, () -> {
            OPENEDFILEREPOSITORY.createTable();
        });
    }

    @Test
    void testOpenFile() {
        Assertions.assertEquals(openedFile, OPENEDFILEREPOSITORY.save(openedFile));
    }

    @Test
    void testDropTable() {
        Assertions.assertThrows(SQLException.class, () -> {
            OPENEDFILEREPOSITORY.dropTable();
        });
    }
}