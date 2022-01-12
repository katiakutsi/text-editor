package ge.tsu.texteditor.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenedFile {
    private int id;
    private String fileName;

    public OpenedFile(String fileName) {
        this.fileName = fileName;
    }
}
