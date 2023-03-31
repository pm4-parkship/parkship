package ch.zhaw.parkship.dtos;

import java.io.Serializable;

import ch.zhaw.parkship.entities.TagEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDto implements Serializable {
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String category;

	public TagDto(TagEntity tagEntity) {
		this.id = tagEntity.getId();
		this.name = tagEntity.getName();
		this.category = tagEntity.getCategory();
	}

	public TagDto() {
	}
}
