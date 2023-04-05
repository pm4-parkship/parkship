package ch.zhaw.parkship.tag;

import java.io.Serializable;
import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDto implements Serializable {
    private Integer id;

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

    @Override
    public String toString() {
        return "TagDto{" + "id=" + id + ", name='" + name + '\'' + ", category='" + category + '\'' + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TagDto))
            return false;
        TagDto tagDto = (TagDto) o;
        return Objects.equals(id, tagDto.id);
    }
}
