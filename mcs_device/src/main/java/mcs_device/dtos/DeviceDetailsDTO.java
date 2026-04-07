package mcs_device.dtos;

import mcs_device.dtos.validators.annotation.AgeLimit;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class DeviceDetailsDTO {

    private UUID id;
    private UUID personId;
    @NotNull
    private String description;
    @NotNull
    private String address;
    //@AgeLimit(limit = 18)
    private int energyConsumption;
    private String personName;

    public DeviceDetailsDTO() {
    }

    public DeviceDetailsDTO( String description, String address, int energyConsumption) {
        this.description = description;
        this.address = address;
        this.energyConsumption = energyConsumption;
    }

    public DeviceDetailsDTO(UUID id, String description, String address, int energyConsumption) {
        this.id = id;
        this.description= description;
        this.address = address;
        this.energyConsumption = energyConsumption;

    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEnergyConsumption(int energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public String getAddress() {
        return address;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }
}
