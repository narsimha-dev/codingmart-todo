package com.user.role.repository.travel;

import com.user.role.models.travel.AgentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentFileRepository extends JpaRepository<AgentFile, Long> {
}
