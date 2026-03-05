package com.example.devotionals.repo;

import com.example.devotionals.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {}