package com.example.eventplatform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payment")
public class PaymentConfig {

    private Toss toss = new Toss();
    private Bank bank = new Bank();

    public static class Toss {
        private String clientKey;
        private String secretKey;

        // getters and setters
        public String getClientKey() { return clientKey; }
        public void setClientKey(String clientKey) { this.clientKey = clientKey; }

        public String getSecretKey() { return secretKey; }
        public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    }

    public static class Bank {
        private String account;
        private String holder;

        // getters and setters
        public String getAccount() { return account; }
        public void setAccount(String account) { this.account = account; }

        public String getHolder() { return holder; }
        public void setHolder(String holder) { this.holder = holder; }
    }

    // getters and setters
    public Toss getToss() { return toss; }
    public void setToss(Toss toss) { this.toss = toss; }

    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }
}