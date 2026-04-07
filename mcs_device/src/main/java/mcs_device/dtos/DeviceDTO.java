package mcs_device.dtos;

import java.util.Objects;
import java.util.UUID;

public class DeviceDTO {
    private UUID id;
    private String description;
    private String address;
    private int energyConsumption;

    public DeviceDTO() {
    }

    public DeviceDTO(UUID id, String description, String address, int energyConsumption) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.energyConsumption = energyConsumption;
    }

    public UUID getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnergyConsumption(int energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return energyConsumption == deviceDTO.energyConsumption &&
                Objects.equals(description, deviceDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, energyConsumption);
    }
}
