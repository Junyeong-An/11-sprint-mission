package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

// 유저 JPA 레포지토리 - 쿼리 메소드 기반
public interface UserRepository extends JpaRepository<User, UUID> {

    // 유저이름으로 조회
    Optional<User> findByUsername(String username);

    // 이메일로 조회
    Optional<User> findByEmail(String email);

    // 유저이름 중복 확인
    boolean existsByUsername(String username);

    // 이메일 중복 확인
    boolean existsByEmail(String email);
}
