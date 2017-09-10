package com.questionnaire.db;

import com.questionnaire.QuestApp;
import com.questionnaire.R;
import com.questionnaire.content.QuestManager;

public class Tester extends Base{
	private static final long serialVersionUID = 3318559450714226182L;
	private int gender = 0;
	private int age = 25;
	private String nation = "汉族";
	private String place = "北京市海淀区中央民族大学";
	private String profession = "教授";
	private String contant = "oneword_010@sina.com";
	private long date = System.currentTimeMillis();
	
	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	/**
	 * @return the nation
	 */
	public String getNation() {
		return nation;
	}
	/**
	 * @param nation the nation to set
	 */
	public void setNation(String nation) {
		this.nation = nation;
	}
	/**
	 * @return the profession
	 */
	public String getProfession() {
		return profession;
	}
	/**
	 * @param profession the profession to set
	 */
	public void setProfession(String profession) {
		this.profession = profession;
	}
	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}
	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}
	/**
	 * @return the contant
	 */
	public String getContant() {
		return contant;
	}
	/**
	 * @param contant the contant to set
	 */
	public void setContant(String contant) {
		this.contant = contant;
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
	
	public String getGengerString() {
		String[] gengers = QuestApp.getContext().getResources()
				.getStringArray(R.array.gender);
		String genger = gender == 1 ? gengers[1] : gengers[0];
		return genger;
	}
	
	@Override
	public String toString() {
		return QuestManager.getmInstance().getTesterInfo(getId());
	}
}
