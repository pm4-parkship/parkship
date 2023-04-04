package ch.zhaw.parkship.entities;

import java.util.HashSet;
import java.util.Set;

import ch.zhaw.parkship.dtos.TagDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(nullable = false)
	private String category;

	@ManyToMany(mappedBy = "tags")
	private Set<ParkingLotEntity> parkingLots;

	public TagEntity() {
		this.parkingLots = new HashSet<>();
	}
}
