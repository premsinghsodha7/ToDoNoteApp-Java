package app.com.mynote.ui;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import app.com.mynote.R;
import app.com.mynote.db.DatabaseClient;
import app.com.mynote.db.Note;


public class AddNoteFragment extends Fragment {

    EditText editTextTitle, editTextDesc;
    Note mNote = null;

    public AddNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNote = AddNoteFragmentArgs.fromBundle(getArguments()).getNote();

        editTextTitle = view.findViewById(R.id.edittextTitle);
        editTextDesc = view.findViewById(R.id.edittextNote);

        if(mNote != null){
            editTextTitle.setText(mNote.getTitle());
            editTextDesc.setText(mNote.getNote());
        }

        view.findViewById(R.id.addNoteAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNote != null)
                    updateNote(view);
                else
                    addNote(view);
            }
        });
    }

    private void addNote(final View view) {
        final String mNoteTitle = editTextTitle.getText().toString().trim();
        final String mDesc = editTextDesc.getText().toString().trim();

        if (mNoteTitle.isEmpty()) {
            editTextTitle.setError("Task required");
            editTextTitle.requestFocus();
            return;
        }

        if (mDesc.isEmpty()) {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }

        class AddNote extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                //creating a task
                Note note = new Note();
                note.setTitle(mNoteTitle);
                note.setNote(mDesc);

                //adding to database
                DatabaseClient.getInstance(getContext().getApplicationContext()).getAppDatabase()
                        .noteDao()
                        .addNote(note);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(getContext(), "Note Saved", Toast.LENGTH_LONG).show();
                NavDirections action = AddNoteFragmentDirections.actionAddNoteFragmentToHomeFragment();
                Navigation.findNavController(view).navigate(action);
            }
        }

        AddNote st = new AddNote();
        st.execute();
    }

    private void updateNote(final View view) {
        final String mNoteTitle = editTextTitle.getText().toString().trim();
        final String mDesc = editTextDesc.getText().toString().trim();

        if (mNoteTitle.isEmpty()) {
            editTextTitle.setError("Task required");
            editTextTitle.requestFocus();
            return;
        }

        if (mDesc.isEmpty()) {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }

        class UpdateNote extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                //creating a task
                Note note = new Note();
                note.setId(mNote.getId());
                note.setTitle(mNoteTitle);
                note.setNote(mDesc);

                DatabaseClient.getInstance(getContext().getApplicationContext()).getAppDatabase()
                        .noteDao()
                        .updateNote(note);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(getContext(), "Note Updated", Toast.LENGTH_LONG).show();
                NavDirections action = AddNoteFragmentDirections.actionAddNoteFragmentToHomeFragment();
                Navigation.findNavController(view).navigate(action);
            }
        }

        UpdateNote st = new UpdateNote();
        st.execute();
    }

    private void deleteNote() {

        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getContext().getApplicationContext()).getAppDatabase()
                        .noteDao()
                        .deleteNote(mNote);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getContext(), "Note Deleted", Toast.LENGTH_LONG).show();
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            if (mNote != null) {
                deleteNote();
            } else {
                Toast.makeText(getContext(), "Cannot delete", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
