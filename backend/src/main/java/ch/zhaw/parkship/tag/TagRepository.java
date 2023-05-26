package ch.zhaw.parkship.tag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findByNameIgnoreCase(String name);
}
