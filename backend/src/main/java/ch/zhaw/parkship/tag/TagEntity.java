package ch.zhaw.parkship.tag;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Tags")
public class TagEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;


  @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
  private Set<ParkingLotEntity> parkingLots;

  public TagEntity() {
    this.parkingLots = new HashSet<>();
  }

  @Override
  public String toString() {
    return "TagEntity{" + "id=" + id + ", name='" + name + '\'' + ", parkingLots=" + parkingLots + '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof TagEntity))
      return false;
    TagEntity tag = (TagEntity) o;
    return Objects.equals(id, tag.id);
  }
}
