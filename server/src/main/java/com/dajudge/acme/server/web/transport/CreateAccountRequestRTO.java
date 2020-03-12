package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class CreateAccountRequestRTO {
    private List<String> contact;
    private Boolean termsOfServiceAgreed;
    private Boolean onlyReturnExisting;
    private Map<String, Object> externalAccountBinding;

    public List<String> getContact() {
        return contact;
    }

    public void setContact(final List<String> contact) {
        this.contact = contact;
    }

    public Boolean getTermsOfServiceAgreed() {
        return termsOfServiceAgreed;
    }

    public void setTermsOfServiceAgreed(final Boolean termsOfServiceAgreed) {
        this.termsOfServiceAgreed = termsOfServiceAgreed;
    }

    public Boolean getOnlyReturnExisting() {
        return onlyReturnExisting;
    }

    public void setOnlyReturnExisting(final Boolean onlyReturnExisting) {
        this.onlyReturnExisting = onlyReturnExisting;
    }

    public Map<String, Object> getExternalAccountBinding() {
        return externalAccountBinding;
    }

    public void setExternalAccountBinding(final Map<String, Object> externalAccountBinding) {
        this.externalAccountBinding = externalAccountBinding;
    }
}
