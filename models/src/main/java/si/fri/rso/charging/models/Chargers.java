package si.fri.rso.charging.models;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
@Entity(name = "chargers")
@NamedQueries(value =
        {
                @NamedQuery(name = "Charger.getAll", query = "SELECT r FROM chargers r"),
        })
@UuidGenerator(name = "idGenerator")
public class Chargers {
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "Name")
    private String Name;

    @Column(name = "Location")
    private String Location;

    @Column(name = "Comments")
    private String Comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String Comments) {
        this.Comments = Comments;
    }


}
