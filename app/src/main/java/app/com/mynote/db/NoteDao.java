package app.com.mynote.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void addNote(Note note);

    @Query("SELECT * FROM note ORDER BY id DESC")
    List<Note> getAllNotes();

    @Insert
    void addMultipleNotes(Note...notes);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

}
