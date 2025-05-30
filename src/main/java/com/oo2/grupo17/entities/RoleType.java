package com.oo2.grupo17.entities;

public enum RoleType {
	
	ADMIN,
	CLIENTE,
	PROFESIONAL;
	
	public String getPrefixedName() {
		return "ROLE_" + this.name();
	}

}
