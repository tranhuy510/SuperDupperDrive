package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteMapper noteMapper;

    public List<Note> getNotes(int userid){
        return noteMapper.getNotesByUserId(userid);
    }

    public void createNote(Note note, int userId){
        Note newNote = new Note();
        newNote.setUserId(userId);
        newNote.setNoteDescription(note.getNoteDescription());
        newNote.setNoteTitle(note.getNoteTitle());

        noteMapper.insertNote(newNote);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public void deleteNote(int noteId){
        noteMapper.deleteNote(noteId);
    }
}
