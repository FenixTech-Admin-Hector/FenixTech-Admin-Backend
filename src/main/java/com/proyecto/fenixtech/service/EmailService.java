package com.proyecto.fenixtech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendProposalStatusEmail(String to, String proposalTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Actualización de tu propuesta: ");
        message.setText("Hola,\n\nTe informamos que tu propuesta '" + proposalTitle +
                "' ha sido procesada con éxito y será tenida en cuenta para futuras ofertas.\n\nGracias por formar parte de FenixTech.");

        mailSender.send(message);
    }
}