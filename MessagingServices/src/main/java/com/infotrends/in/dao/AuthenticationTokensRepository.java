package com.infotrends.in.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.infotrends.in.data.AuthenticationToken;

@Repository
public interface AuthenticationTokensRepository extends CrudRepository<AuthenticationToken, Integer> {

	AuthenticationToken findByToken(String token);
	int findMaxAuth_id();
}
