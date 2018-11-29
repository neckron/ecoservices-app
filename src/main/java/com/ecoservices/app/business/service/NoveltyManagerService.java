package com.ecoservices.app.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecoservices.app.business.repository.NoveltyRepository;
import com.ecoservices.app.model.Novelty;
import com.ecoservices.app.model.User;

@Service
public class NoveltyManagerService {

  @Autowired
  private NoveltyRepository noveltyRepository;

  public void saveNovelty(Novelty novelty){
    noveltyRepository.save(novelty);
  }

  public List<Novelty> retrieveNoveltyListByUser(User user){
    return noveltyRepository.findByUser(user.getId());
  }

  public List<Novelty> retrieveNoveltyListByApprover(User user){
    return noveltyRepository.findByApprover(user.getId());
  }

}
