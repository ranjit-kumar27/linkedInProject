package com.example.linkedinProject.ConnectionService.service;

import com.example.linkedinProject.ConnectionService.entity.Person;
import com.example.linkedinProject.ConnectionService.repository.PersonRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {
    private final PersonRepository personRepository;

    public void createPerson(Long userId,String name){
        Person person=Person.builder().name(name).userId(userId).build();
       personRepository.save(person);
    }

}
