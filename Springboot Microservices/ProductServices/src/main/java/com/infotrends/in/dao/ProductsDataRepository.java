package com.infotrends.in.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.infotrends.in.data.ProductsData;


@Repository
public interface ProductsDataRepository extends MongoRepository<ProductsData, Integer> {

	Optional<ProductsData> findBypId(int id);

}
