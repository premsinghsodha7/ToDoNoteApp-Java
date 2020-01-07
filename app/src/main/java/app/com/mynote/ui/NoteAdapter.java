package app.com.mynote.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.com.mynote.R;
import app.com.mynote.db.DatabaseClient;
import app.com.mynote.db.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteAdapterViewHolder> {

    private Context mCtx;
    private List<Note> notes;

    public NoteAdapter(Context mCtx, List<Note> notes) {
        this.mCtx = mCtx;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.note_item_layout, parent, false);
        return new NoteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapterViewHolder holder, final int position) {
        final Note n = notes.get(position);
        holder.textViewTitle.setText(n.getTitle());
        holder.textViewNote.setText(n.getNote());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote(n, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToAddNoteFragment().setNote(notes.get(position));
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void deleteNote(final Note n, final int position) {

        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(mCtx.getApplicationContext()).getAppDatabase()
                        .noteDao()
                        .deleteNote(n);
                    notes.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,notes.size());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(mCtx, "Note Deleted", Toast.LENGTH_LONG).show();
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteTask dt = new DeleteTask();
                dt.execute();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog ad = builder.create();
        ad.show();

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewNote;
        ImageView deleteButton;

        public NoteAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewNote = itemView.findViewById(R.id.tv_desc);
            deleteButton = itemView.findViewById(R.id.deleteNote);
        }
    }
}
