package mcs_person.dtos;

import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
public class PersonDTO {
    private UUID id;
    private String name;
    private int age;
    private String address;

    public PersonDTO() {
    }

    public PersonDTO(UUID id, String name, int age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return age == personDTO.age &&
                Objects.equals(name, personDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
