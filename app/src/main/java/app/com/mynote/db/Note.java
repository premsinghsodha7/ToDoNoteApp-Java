package app.com.mynote.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "note")
    private String note;

    /*
     * Getters and Setters
     * */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
