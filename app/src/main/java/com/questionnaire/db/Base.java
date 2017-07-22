package com.questionnaire.db;

import java.io.Serializable;

public class Base implements Serializable {
	private static final long serialVersionUID = 8851483479169703128L;
	private long id = -1;
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
}
