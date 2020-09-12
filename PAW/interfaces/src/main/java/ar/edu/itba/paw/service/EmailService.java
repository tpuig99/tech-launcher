package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.User;

public interface EmailService {
    void sendEmailConfirmation(User user,String appUrl);
}
