package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserMapper userMapper;

    @PostMapping
    public String handleAddUpdateNote(Authentication authentication, Note note){
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userMapper.getUser(loggedInUserName);
        Integer userId = user.getUserId();

        if (note.getNoteId() != null) {
            noteService.updateNote(note);
        } else {
            noteService.addNote(note, userId);
        }

        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int noteId, RedirectAttributes redirectAttributes){
        if(noteId > 0){
            noteService.deleteNote(noteId);
            return "redirect:/result?success";
        }

        redirectAttributes.addAttribute("error", "Unable to delete the note.");
        return "redirect:/result?error";
    }
}
