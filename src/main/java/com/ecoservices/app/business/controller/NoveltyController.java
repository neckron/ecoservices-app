package com.ecoservices.app.business.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ecoservices.app.business.service.NoveltyManagerService;
import com.ecoservices.app.model.Novelty;
import com.ecoservices.app.model.User;
import com.ecoservices.app.security.repository.UserRepository;

@Controller
public class NoveltyController {

  @Autowired
  NoveltyManagerService noveltyManagerService;
  @Autowired
  UserRepository userRepository;

  @RequestMapping(value = "/createNewNovelty", method = RequestMethod.POST)
  public ModelAndView createNewNovelty(@Valid Novelty novelty, BindingResult bindingResult) {
    ModelAndView modelAndView = new ModelAndView();
    System.out.println("FEchas"+novelty.getStartDate()+" - "+novelty.getEndDate());
    novelty.setUser(getUserFromRepository());
    noveltyManagerService.saveNovelty(novelty);
    modelAndView.addObject("successMessage", "novedad registrada");
    //modelAndView.addObject("novelty", new User());
    modelAndView.setViewName("/user/board");

    return modelAndView;
  }

  private User getUserFromRepository(){
    String currentUserName =null;
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!(auth instanceof AnonymousAuthenticationToken)) {
      currentUserName = auth.getName();
    }
    return userRepository.findByEmail(currentUserName);
  }

  @GetMapping("/dashboardUser/noveltyList")
  public ModelAndView getNoveltyList(){
    ModelAndView modelAndView = new ModelAndView();
    List<Novelty> listNovelties = new ArrayList();
    Novelty novelty = new Novelty();
    novelty.setComment("New Comment");
    novelty.setType("New Type");
    listNovelties.add(novelty);
    listNovelties.add(novelty);
    modelAndView.addObject("listNovelties",listNovelties);
    return modelAndView;
  }

}
