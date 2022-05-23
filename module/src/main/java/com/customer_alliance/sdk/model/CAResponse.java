package com.customer_alliance.sdk.model;

import java.io.Serializable;
import java.util.List;


public class CAResponse implements Serializable {
    private List<Element> elements;
    private String token;
    private String hash;
    private String version;
    private Translations translations;
    private Assets assets;

    public CAResponse(List<Element> elements, String token, String hash, String version, Translations translations, Assets assets) {
        this.elements = elements;
        this.token = token;
        this.hash = hash;
        this.version = version;
        this.translations = translations;
        this.assets = assets;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Translations getTranslations() {
        return translations;
    }

    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    @Override
    public String toString() {
        return "CAResponse{" +
                "elements=" + elements +
                ", token='" + token + '\'' +
                ", hash='" + hash + '\'' +
                ", version='" + version + '\'' +
                ", translations=" + translations +
                ", assets=" + assets +
                '}';
    }
}
