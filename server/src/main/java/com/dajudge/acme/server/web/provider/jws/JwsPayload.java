package com.dajudge.acme.server.web.provider.jws;

public class JwsPayload {
    private String _protected;
    private String signature;
    private String payload;

    public String getProtected() {
        return _protected;
    }

    public void setProtected(final String _protected) {
        this._protected = _protected;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(final String signature) {
        this.signature = signature;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(final String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "JwsPayload{" +
                "protected='" + _protected + '\'' +
                ", signature='" + signature + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
