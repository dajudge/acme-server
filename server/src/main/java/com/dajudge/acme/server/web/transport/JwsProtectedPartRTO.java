package com.dajudge.acme.server.web.transport;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

@ToString
@EqualsAndHashCode
public class JwsProtectedPartRTO {
    private String alg;
    private Map<String, Object> jwk;
    private String nonce;
    private String url;
    private String kid;

    public String getAlg() {
        return alg;
    }

    public void setAlg(final String alg) {
        this.alg = alg;
    }

    public Map<String, Object> getJwk() {
        return jwk;
    }

    public void setJwk(final Map<String, Object> jwk) {
        this.jwk = jwk;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(final String nonce) {
        this.nonce = nonce;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(final String kid) {
        this.kid = kid;
    }
}
