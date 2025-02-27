package com.company.journal_app.service;

import com.company.journal_app.repository.JournalEntryRepository;
import com.company.journal_app.entity.JournalEntry;
import com.company.journal_app.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService ;


    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    @Transactional
    public void saveNewEntry(JournalEntry journalEntry, String userName){
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        }
        catch (Exception e){
           log.error("Error",e);
            throw new RuntimeException("An error occur while saving the entry.",e);
        }

    }

    public List<JournalEntry> getAll(String userName){
        User user = userService.findByUserName(userName);
        return user.getJournalEntries();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName){
        boolean removed;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }

        }
        catch (Exception e){
            log.error(String.valueOf(e));
            throw  new RuntimeException("An error occur while deleting the entry.",e);
        }
    return removed ;
    }
}
