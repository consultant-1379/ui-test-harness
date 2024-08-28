package com.ericsson.cifwk.taf.ui;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * Browser cookie representation
 */
public class BrowserCookie {
    private final String name;
    private final String value;
    private final String domain;
    private final String path;
    private final Date expiry;
    private final boolean isSecure;

    public BrowserCookie(String name, String value, String domain, String path, Date expiry, boolean isSecure) {
        this.name = name;
        this.value = value;
        this.domain = domain;
        this.path = path;
        this.expiry = expiry;
        this.isSecure = isSecure;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public Date getExpiry() {
        return expiry;
    }

    public boolean isSecure() {
        return isSecure;
    }


    @Override
    public String toString() {
        return "BrowserCookie [name=" + name + ", value=" + value + ", domain=" + domain + ", path=" + path + ", expiry=" + expiry
                + ", isSecure=" + isSecure + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name, this.value, this.domain, this.path, this.expiry, this.isSecure);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BrowserCookie other = (BrowserCookie) obj;
        if (domain == null) {
            if (other.domain != null)
                return false;
        } else if (!domain.equals(other.domain))
            return false;
        if (expiry == null) {
            if (other.expiry != null)
                return false;
        } else if (!expiry.equals(other.expiry))
            return false;
        if (isSecure != other.isSecure)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }


}
