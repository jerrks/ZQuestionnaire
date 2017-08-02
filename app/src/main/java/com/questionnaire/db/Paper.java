package com.questionnaire.db;

public class Paper extends Base {
	private static final long serialVersionUID = 7460737037921555466L;
	private String subjectIds;
	private String name;
	private String description;
	private String author;
	private String markes;
	private long date;

	// inner usage
	boolean selected;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the subjectIds
	 */
	public String getSubjectIds() {
		return subjectIds;
	}
	/**
	 * @param subjectIds the subjectIds to set
	 */
	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the markes
	 */
	public String getMarkes() {
		return markes;
	}
	/**
	 * @param markes the markes to set
	 */
	public void setMarkes(String markes) {
		this.markes = markes;
	}
	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "id=" + getId() + "name="+name+",description="+description+",author="+author+",date="+date;
	}
}
