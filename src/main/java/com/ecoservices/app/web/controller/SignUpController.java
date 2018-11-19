package com.ecoservices.app.web.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ecoservices.app.model.User;
import com.ecoservices.app.security.service.CustomUserDetailsService;

@Controller
public class SignUpController {

    @Autowired
    private CustomUserDetailsService userService;

    Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currentUser", userService.getUserByAuthenticationContext());
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("signup");
        return modelAndView;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currentUser", userService.getUserByAuthenticationContext());
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult.rejectValue("email", "error.user",
                    "Ya exite un usuario registrado con ese email");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("signup");
        } else {
            user.setPassword(user.getEmail());
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "Usuario registrado exitosamente");
            modelAndView.addObject("user", new User());


        }
        modelAndView.setViewName("signup");
        return modelAndView;
    }

}
