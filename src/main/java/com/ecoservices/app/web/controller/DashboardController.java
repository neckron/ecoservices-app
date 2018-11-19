package com.ecoservices.app.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ecoservices.app.security.service.CustomUserDetailsService;

@Controller
public class DashboardController {

    Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private CustomUserDetailsService userService;

    @RequestMapping(value = "/admin/board", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currentUser", userService.getUserByAuthenticationContext());
        logger.debug("rdirecting user "+userService.getUserByAuthenticationContext());
        modelAndView.setViewName("/adminPages/dashboard");
        return modelAndView;
    }

    @RequestMapping(value = "/user/board", method = RequestMethod.GET)
    public ModelAndView userDashboard() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("currentUser", userService.getUserByAuthenticationContext());
        modelAndView.setViewName("/userPages/dashboardUser");
        return modelAndView;
    }

}
