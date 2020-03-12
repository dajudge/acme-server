package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class FinalizeOrderRTO {
    private  String csr;

    public String getCsr() {
        return csr;
    }

    public void setCsr(final String csr) {
        this.csr = csr;
    }
}
