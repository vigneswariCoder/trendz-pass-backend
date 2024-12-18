package com.newtrendz.pass.repository;

import com.newtrendz.pass.entity.Membership;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MembershipRepository extends MongoRepository<Membership, String> {
}
