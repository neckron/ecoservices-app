package com.ecoservices.app.business.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecoservices.app.model.Novelty;

public interface NoveltyRepository extends MongoRepository<Novelty, String>{

  List<Novelty> findByUser(String id);
  List<Novelty> findByApprover(String id);

}


