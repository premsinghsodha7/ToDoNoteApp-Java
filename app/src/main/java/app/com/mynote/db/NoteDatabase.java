package app.com.mynote.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase  extends RoomDatabase {
    public abstract NoteDao noteDao();
}
