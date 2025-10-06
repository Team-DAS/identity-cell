package com.udeajobs.identity.account_service.service.interfaces;

import java.util.Map;

public interface MailService {
    void sendEmail(String to, String subject, String template, Map<String, Object> model);
}
