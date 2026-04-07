package mcs_device.services;

import mcs_device.dtos.*;
import mcs_device.dtos.builders.DeviceBuilder;
import mcs_device.entities.Device;
import mcs_device.entities.Person;
import mcs_device.exceptions.DeviceNotFoundException;
import mcs_device.exceptions.PersonNotFoundException;
import mcs_device.repositories.DeviceRepository;
import mcs_device.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mcs_device.controllers.handlers.exceptions.model.ResourceNotFoundException;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final PersonRepository personRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, PersonRepository personRepository) {

        this.deviceRepository = deviceRepository;
        this.personRepository = personRepository;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(prosumerOptional.get());
    }

    public DeviceDetailsDTO updateDevice(UUID deviceId, DeviceDetailsDTO  deviceDTO){
        boolean exists = deviceRepository.findById(deviceId).isPresent();
        if (exists){
            Device originalDevice = deviceRepository.findById(deviceId).get();
            if(deviceDTO.getDescription() != null){ // if we update device description
                originalDevice.setDescription(deviceDTO.getDescription());
            }
            if(deviceDTO.getEnergyConsumption() != 0){ // if we update device energy consumption
                originalDevice.setEnergyConsumption(deviceDTO.getEnergyConsumption());
            }
            if(deviceDTO.getAddress() != null){
                originalDevice.setAddress(deviceDTO.getAddress()); // if we update device address
            }
            deviceRepository.save(originalDevice);
            return DeviceBuilder.toDeviceDetailsDTO(originalDevice);
        }
        return null;
    }

    public boolean deleteDevice(UUID deviceId){
        boolean exists = deviceRepository.findById(deviceId).isPresent();
        if(exists){
            deviceRepository.deleteById(deviceId);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean updatePersonName(UUID personId, PersonNameUpdateDTO personNameUpdateDTO){
        boolean exists = personRepository.findById(personId).isPresent();
        if(exists){
            Person originalPerson = personRepository.findById(personId).get();
            if(personNameUpdateDTO.getName() != null){
                originalPerson.setName(personNameUpdateDTO.getName());
            }
            personRepository.save(originalPerson);
            return true;
        }
        return false;
    }


    public UUID insertDevice(OnlyDeviceDTO deviceDTO){

        Device device = DeviceBuilder.toOnlyDeviceEntity(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();

    }


    public void associateDeviceToPerson(DevicePersonAssociationDTO associationDTO) {


        Device device = deviceRepository.findById(associationDTO.getDeviceId())
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));

        // Fetch the person details from Person Microservice
        String personServiceUrl = "http://172.16.0.11:8080/person/" + associationDTO.getPersonId();
        System.out.println("PersonServiceURL: "+ personServiceUrl);
        PersonDTO personDetails = restTemplate.getForObject(personServiceUrl, PersonDTO.class);

        if (personDetails == null) {
            throw new PersonNotFoundException("Person not found in Person Microservice");
        }

        // Save or update the person in the Device Microservice database
        Person person = personRepository.findById(personDetails.getId()).orElse(new Person());
        person.setId(personDetails.getId());
        person.setName(personDetails.getName());
        // Set other details if needed

        personRepository.save(person);  // Save person in the Device Microservice database

        // Associate the device with the person
        device.setPersonId(person.getId());
        deviceRepository.save(device);
    }

    public void unlinkPersonFromDevices(UUID personId) {
        // Find devices associated with the personId
        List<Device> devices = deviceRepository.findByPersonId(personId);

        // If devices list is not empty, set person to null and save changes
        if (!devices.isEmpty()) {
            for (Device device : devices) {
                device.setPersonId(null);  // Set the person reference to null for each device
            }
            deviceRepository.saveAll(devices);
        }

        // Delete the person record in the local person table in the device microservice
        if (personRepository.existsById(personId)) {
            personRepository.deleteById(personId);
        }
    }

    public List<DeviceDTO> getDevicesByPersonId(UUID personId) {
        List<Device> deviceList = deviceRepository.findByPersonId(personId);
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }


}
