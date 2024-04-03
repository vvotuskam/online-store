package com.halyk.onlinestore.repository;

import com.halyk.onlinestore.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, UUID> {
}
