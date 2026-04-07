package mcs_device.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Device  implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name="id")
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "energyConsumption", nullable = false)
    private int energyConsumption;

    //@ManyToOne( optional = true)
    @Column(name="person_id")
    private UUID personId;

    public Device() {
    }

    public Device(String description, String address, int energyConsumption) {
        //this.personId = personId;
        this.description = description;
        this.address = address;
        this.energyConsumption = energyConsumption;
    }

//    public Person getPerson() {
//        return person;
//    }
//
//    public void setPerson(Person person) {
//        this.person = person;
//    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public UUID getId() {
        return id;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPersonId() {
        return personId;
    }


    public void setPersonId(UUID personId) {
        this.personId = personId;
    }
}
