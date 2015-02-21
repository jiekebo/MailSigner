
package com.mailsigner.control.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for signatureFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="signatureFormat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HTML"/>
 *     &lt;enumeration value="RTF"/>
 *     &lt;enumeration value="TXT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "signatureFormat", namespace = "http://ws.control.mailsigner.com/")
@XmlEnum
public enum SignatureFormat {

    HTML,
    RTF,
    TXT;

    public String value() {
        return name();
    }

    public static SignatureFormat fromValue(String v) {
        return valueOf(v);
    }

}
