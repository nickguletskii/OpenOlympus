package org.ng200.openolympus.model;

public interface OlympusPrincipal {
	public default String getType() {
		return this.getClass().getSimpleName();
	}
}
