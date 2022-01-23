package logic.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.controller.RegisterController;

public class RegBean {
private String email;
private String password;
private String password2;
private String username;
private String compName;



private String pIVA;
private String error;
private Pattern patternPsw;
private Pattern patternEmail;
private RegisterController regController;
private String radio;

public RegBean() {
}

public String getRadio() {
    return radio;
}

public void setRadio(String radio) {
    this.radio = radio;
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

public String getCompName() {
	return compName;
}

public void setCompName(String compName) {
	this.compName = compName;
}

public String getpIVA() {
	return pIVA;
}

public void setpIVA(String pIVA) {
	this.pIVA = pIVA;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
} 

public String getPassword2() {
	return password2;
}

public void setPassword2(String password2) {
	this.password2 = password2;
}

public String getError() {
    return error;
}

public void setError(String error) {
    this.error = error;
} 

}
