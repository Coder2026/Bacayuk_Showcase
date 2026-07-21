package com.sharesphere.usermanagement.adapter;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.sharesphere.usermanagement.config.SendGridTemplateProperties;
import com.sharesphere.usermanagement.domain.EmailService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class SendGridEmailService implements EmailService {

    private final SendGrid sendGridClient;
    private final SendGridTemplateProperties templateProperties;

    @Override
    public void sendVerificationEmail(String toEmail, Map<String, String> data) {
        send(templateProperties.getVerification(), toEmail, data);
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, Map<String, String> data) {
        send(templateProperties.getResetPassword(), toEmail, data);
    }


    private void send(String templateId, String toEmail, Map<String, String> dynamicTemplateData) {
        try {
            Request request = buildRequest(toEmail, templateId, dynamicTemplateData);
            sendGridClient.api(request);
        } catch (IOException ex) {
            log.error("SendGrid I/O failure", ex);
            throw new IllegalStateException("Gagal mengirim email", ex);
        }
    }

    private Request buildRequest(String toEmail, String templateId, Map<String, String> dynamicTemplateData) throws IOException {
        Mail mail = new Mail();
        mail.setFrom(new Email("support@bacayuk.online", "Bacayuk"));
        mail.setTemplateId(templateId);

        Personalization personalization = new Personalization();
        personalization.addTo(new Email(toEmail));
        if (dynamicTemplateData != null) {
            dynamicTemplateData.forEach(personalization::addDynamicTemplateData);
        }
        mail.addPersonalization(personalization);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        return request;
    }
}
