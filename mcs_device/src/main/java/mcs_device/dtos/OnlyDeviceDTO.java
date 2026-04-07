package mcs_device.dtos;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class OnlyDeviceDTO {

    private UUID id;

    @NotNull
    private String description;
    @NotNull
    private String address;
    //@AgeLimit(limit = 18)
    private int energyConsumption;

    public OnlyDeviceDTO() {
    }

    public OnlyDeviceDTO( String description, String address, int energyConsumption) {
        this.description = description;
        this.address = address;
        this.energyConsumption = energyConsumption;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public UUID getId() {
        return id;
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

    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public OnlyDeviceDTO(UUID id, String description, String address, int energyConsumption) {
        this.id = id;
        this.description= description;
        this.address = address;
        this.energyConsumption = energyConsumption;

    }
}
