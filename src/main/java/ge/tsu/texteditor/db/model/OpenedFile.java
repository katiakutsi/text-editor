package ge.tsu.texteditor.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenedFile {
    private int id;
    private Path filePath;

    public OpenedFile(Path filePath) {
        this.filePath = filePath;
    }
}
