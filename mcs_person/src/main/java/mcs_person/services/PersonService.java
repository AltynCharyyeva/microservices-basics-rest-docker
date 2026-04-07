package mcs_person.services;

import mcs_person.dtos.*;
import mcs_person.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import mcs_person.controllers.handlers.exceptions.model.ResourceNotFoundException;
import mcs_person.dtos.builders.PersonBuilder;
import mcs_person.entities.Person;
import mcs_person.repositories.PersonRepository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDetailsDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDetailsDTO)
                .collect(Collectors.toList());
    }

    public PersonDetailsDTO findPersonById(UUID id) {
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }

    public PersonDetailsDTO updatePerson(UUID personId, PersonDetailsDTO  personDTO){
        boolean exists = personRepository.findById(personId).isPresent();
        if (exists){
            Person originalPerson = personRepository.findById(personId).get();
            if(personDTO.getName() != null){ // if person updates name
                originalPerson.setName(personDTO.getName());
            }
            if(personDTO.getAge() != 0){ // if person updates age
                originalPerson.setAge(personDTO.getAge());
            }
            if(personDTO.getAddress() != null){
                originalPerson.setAddress(personDTO.getAddress()); // if person updates address
            }
            if(personDTO.getPassword() != null){
                originalPerson.setPassword(personDTO.getPassword()); // if person updates password
            }
            personRepository.save(originalPerson);


//            // Prepare the DTO with only the ID and Name for the Device Microservice
//            PersonNameUpdateDTO nameUpdateDTO = new PersonNameUpdateDTO(personId, personDTO.getName());
//
//            // Send the update to the Device Microservice
//            String deviceServiceUrl = "http://localhost:8081/device/person_name_update/" + personId;
//            try {
//                HttpEntity<PersonNameUpdateDTO> request = new HttpEntity<>(nameUpdateDTO);
//                restTemplate.exchange(deviceServiceUrl, HttpMethod.POST, request, Void.class);
//            } catch (HttpClientErrorException e) {
//                // Log and handle errors for the device service call
//                System.err.println("Error updating device service: " + e.getMessage());
//            }

            return PersonBuilder.toPersonDetailsDTO(originalPerson);
        }
        return null;
    }

    public boolean deletePerson(UUID personId){
        boolean exists = personRepository.findById(personId).isPresent();
        if(exists){
            personRepository.deleteById(personId);
            // Notify the device microservice to update devices associated with this person
            notifyDeviceMicroservice(personId);
            return true;
        }
        else{
            return false;
        }
    }

    private void notifyDeviceMicroservice(UUID personId) {
        // URL for the device microservice API endpoint
        String deviceServiceUrl = "http://172.16.0.12:8081/device/unlinkPerson/" + personId;

        // Send a DELETE request to the device microservice to unlink the person from associated devices
        restTemplate.delete(deviceServiceUrl);
    }

    public UUID insert(PersonDetailsDTO personDTO) {
        Person person = PersonBuilder.toEntity(personDTO);

        // Check the naming conventions for role assignment
        String name = person.getName();
        String password = person.getPassword(); // Ensure you have a password field in your DTO

        // Determine the role based on the naming conventions
        Role role;
        if (name.endsWith("_admin") && password.endsWith("_88")) {
            role = Role.ADMIN; // Assuming you have an enum named Role with ADMIN value
        } else {
            role = Role.CLIENT; // Default role for other users
        }

        // Set the role in the person entity (make sure you have a field for this)
        person.setRole(role);

        // Save the person entity to the database
        person = personRepository.save(person);
        LOGGER.debug("Person with id {} was inserted in db", person.getId());

        return person.getId();
    }


//    public List<DeviceDTO> getDevicesForPerson(UUID personId) {
//        String deviceServiceUrl = "http://device_service:8081/device/person/" + personId;
//
//        // Make GET request to device microservice
//        ResponseEntity<List<DeviceDTO>> response = restTemplate.exchange(
//                deviceServiceUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<DeviceDTO>>() {
//                }
//        );
//
//        // Return the list of devices
//        return response.getBody();
//    }

    public UUID findPersonByNameAndPassword(LoginDTO loginDTO){
        Optional<Person> prosumerOptional = personRepository.findByNameAndPassword(loginDTO.getName(),
                loginDTO.getPassword());
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person was not found in db");
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: ");
        }
        return prosumerOptional.get().getId();
    }

    public Role findRoleByPersonId(UUID personId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person not found with id: " + personId);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + personId);
        }
        return personOptional.get().getRole(); // Returns the role ENUM
    }



}
