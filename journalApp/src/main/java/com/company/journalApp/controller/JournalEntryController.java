package com.company.journalApp.controller;

import com.company.journalApp.entity.JournalEntry;
import com.company.journalApp.entity.User;
import com.company.journalApp.service.JournalEntryService;
import com.company.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService ;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
        String userName = authentication.getName() ;
        return  new ResponseEntity<>(journalEntryService.getAll(userName),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JournalEntry>  createEntry(@RequestBody JournalEntry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
            String userName = authentication.getName();
            journalEntryService.saveNewEntry(myEntry,userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED) ;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntriesById (@PathVariable ObjectId myId){
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication() ;
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect  = user.getJournalEntries().stream().filter(x->x.getId().equals(myId)).toList();
        if (!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
        String userName = authentication.getName() ;
        boolean removed =  journalEntryService.deleteById(id,userName);
        if (removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry>  updateJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry myEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName() ;
        User user = userService.findByUserName(userName);

        List<JournalEntry> collect  = user.getJournalEntries().stream().filter(x->x.getId().equals(id)).toList();
        if (!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if (journalEntry.isPresent()){
                JournalEntry oldEntry =journalEntry.get();
                oldEntry.setTitle(myEntry.getTitle() != null && !myEntry.getTitle().isEmpty() ? myEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(myEntry.getContent() != null && !myEntry.getContent().isEmpty() ? myEntry.getContent() : oldEntry.getContent());
                journalEntryService.saveEntry(oldEntry);

                return new ResponseEntity<>(myEntry,HttpStatus.OK);
            }
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
