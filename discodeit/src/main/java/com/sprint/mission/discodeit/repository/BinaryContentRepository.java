package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

// 첨부파일(이진 데이터) JPA 레포지토리
public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

    // ID 목록으로 일괄 조회
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
}
