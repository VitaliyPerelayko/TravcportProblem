package by.vit.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "transporter")
@PrimaryKeyJoinColumn(name = "user_id")
public class Transporter extends User {

    private String license;

    @OneToMany(mappedBy = "transporter")
    private Set<Car> cars;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Set<Car> getCars() {
        return cars;
    }
}
