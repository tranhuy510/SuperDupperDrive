package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.util.UserUtil;
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
    private final UserUtil userUtil;

    @PostMapping
    public String createOrUpdateNot(Authentication authentication, Note note){
        Integer userId = userUtil.getCurrentUserId(authentication);

        if (note.getNoteId() != null) {
            noteService.updateNote(note);
        } else {
            noteService.createNote(note, userId);
        }

        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") Integer noteId, RedirectAttributes redirectAttributes) {
        if (noteId != null && noteId > 0) {
            noteService.deleteNote(noteId);
            return "redirect:/result?success";
        }

        redirectAttributes.addFlashAttribute("error", "Cannot delete note with noteId = " + noteId);
        return "redirect:/result?error";
    }
}
