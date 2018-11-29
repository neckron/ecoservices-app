package com.ecoservices.app.web.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecoservices.app.model.User;
import com.ecoservices.app.security.service.CustomUserDetailsService;
import com.ecoservices.app.util.EmailService;

@Controller
public class PasswordController {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    EmailService emailService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    Logger logger = LoggerFactory.getLogger(PasswordController.class);

    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public ModelAndView displayForgotPasswordPage() {
        return new ModelAndView("forgotPassword");
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ModelAndView processForgotPasswordForm(ModelAndView modelAndView, @RequestParam("email") String userEmail, HttpServletRequest request) {
        logger.debug("Sending email");
        Optional<User> optionalUser = customUserDetailsService.findUserByEmail(userEmail);
        logger.debug("requested password change for: "+userEmail);
        if(!optionalUser.isPresent()){
            modelAndView.addObject("errorMessage", "No se ha encontrado cuenta relacionada con el email requerido");
        }else{
            User user = optionalUser.get();
            user.setResetToken(UUID.randomUUID().toString());
            customUserDetailsService.saveUser(user);
            String appUrl = request.getScheme()+"://"+request.getServerName();
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("support@demo.com");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
                    + "/reset?token=" + user.getResetToken());

            emailService.sendEmail(passwordResetEmail);
            // Add success message to view
            modelAndView.addObject("successMessage", "A password reset link has been sent to " + userEmail);
        }

        modelAndView.setViewName("forgotPassword");
        return modelAndView;
        }


    // Display form to reset password
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView displayResetPasswordPage(ModelAndView modelAndView, @RequestParam("token") String token) {

        Optional<User> user = customUserDetailsService.findUserByResetToken(token);

        if (user.isPresent()) { // Token found in DB
            modelAndView.addObject("resetToken", token);
        } else { // Token not found in DB
            modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
        }

        modelAndView.setViewName("resetPassword");
        return modelAndView;
    }

    // Process reset password form
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestParam Map<String, String> requestParams, RedirectAttributes redir) {

        Optional<User> user = customUserDetailsService.findUserByResetToken(requestParams.get("token"));

        if (user.isPresent()) {

            User resetUser = user.get();
            resetUser.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));
            resetUser.setResetToken(null);
            customUserDetailsService.saveUser(resetUser);
            redir.addFlashAttribute("successMessage", "You have successfully reset your password.  You may now login.");
            modelAndView.setViewName("redirect:login");
            return modelAndView;
        } else {
            modelAndView.addObject("errorMessage", "Oops!  This is an invalid password reset link.");
            modelAndView.setViewName("resetPassword");
        }

        return modelAndView;
    }

    // Going to reset page without a token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:login");
    }

    }
