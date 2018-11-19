package com.ecoservices.app.web.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ecoservices.app.business.service.NoveltyManagerService;
import com.ecoservices.app.model.Novelty;
import com.ecoservices.app.security.service.CustomUserDetailsService;

@Controller
public class NoveltyController {

  @Autowired
  NoveltyManagerService noveltyManagerService;
  @Autowired
  private CustomUserDetailsService userService;

  Logger logger = LoggerFactory.getLogger(NoveltyController.class);

  private static final String NOVEDAD_REGISTRADA = "Novedad registrada exitosamente" ;

  @RequestMapping(value = "/createNewNovelty", method = RequestMethod.POST)
  public ModelAndView createNewNovelty(@Valid Novelty novelty, BindingResult bindingResult) {
    ModelAndView modelAndView = new ModelAndView();
    novelty.setUser(userService.getUserByAuthenticationContext());
    novelty.setCreationDate(LocalDate.now());
    novelty.setStatus("EN REVISION");
    noveltyManagerService.saveNovelty(novelty);
    logger.debug(NOVEDAD_REGISTRADA);
    modelAndView.addObject("successMessage", NOVEDAD_REGISTRADA);
    modelAndView.addObject("currentUser", userService.getUserByAuthenticationContext());
    modelAndView.setViewName("userPages/dashboardUser");
    return modelAndView;
  }


  @GetMapping("/noveltyList")
  public ModelAndView getNoveltyList(){
    ModelAndView modelAndView = new ModelAndView();
    List<Novelty> listNovelties = noveltyManagerService.retrieveNoveltyListByUser(userService.getUserByAuthenticationContext());
    modelAndView.addObject("currentUser", userService.getUserByAuthenticationContext());
    modelAndView.addObject("listNovelties",listNovelties);
    modelAndView.setViewName("userPages/noveltyList");
    return modelAndView;
  }

}
