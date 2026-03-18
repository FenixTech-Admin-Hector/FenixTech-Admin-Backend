package com.proyecto.fenixtech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.proyecto.fenixtech.model.Contact;
import com.proyecto.fenixtech.model.Users;

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

    public void sendContactAdminNotification(String adminEmail, Contact contact, Users sender, String categoryName) {
        String asunto = "NUEVA SOLICITUD EN FENIXTECH: " + contact.getTitle();
        String mensaje = "¡Hola Admin!\n\n"
                + "El usuario " + sender.getFirstName() + " " + sender.getLastName() 
                + " (" + sender.getEmail() + ") ha publicado una nueva solicitud.\n\n"
                + "DETALLES DE LA SOLICITUD:\n"
                + "-----------------------------------\n"
                + "Título: " + contact.getTitle() + "\n"
                + "Categoría: " + categoryName + "\n"
                + "Descripción:\n" + contact.getDescription() + "\n"
                + "-----------------------------------\n\n"
                + "Revisa la base de datos o el panel de administración para más detalles.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(adminEmail);
        email.setSubject(asunto);
        email.setText(mensaje);

        email.setReplyTo(sender.getEmail());
        
        mailSender.send(email); 
    }
}