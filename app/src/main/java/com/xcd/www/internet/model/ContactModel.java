package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/18.
 */

public class ContactModel implements Serializable {

    /**
     * firstName : 张
     * lastname : 恒
     * mobile : 18464288292
     * mobileEmail : 18464288292
     */

    private String firstName;
    private String lastname;
    private String mobile;
    private String mobileEmail;
    private String prefix;
    private String middleName;
    private String suffix;
    private String phoneticFirstName;
    private String phoneticMiddleName;
    private String phoneticLastName;
    private String letters;//显示拼音的首字母
    private String Logo;
    private String name;
    private boolean enable;//是都可選擇
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getHomeNum() {
        return homeNum;
    }

    public void setHomeNum(String homeNum) {
        this.homeNum = homeNum;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getWorkFax() {
        return workFax;
    }

    public void setWorkFax(String workFax) {
        this.workFax = workFax;
    }

    private String homeNum;

private String jobNum;
    private String workFax;
    public String getPhoneticMiddleName() {
        return phoneticMiddleName;
    }

    public void setPhoneticMiddleName(String phoneticMiddleName) {
        this.phoneticMiddleName = phoneticMiddleName;
    }

    public String getPhoneticLastName() {
        return phoneticLastName;
    }

    public void setPhoneticLastName(String phoneticLastName) {
        this.phoneticLastName = phoneticLastName;
    }

    public String getPhoneticFirstName() {
        return phoneticFirstName;
    }

    public void setPhoneticFirstName(String phoneticFirstName) {
        this.phoneticFirstName = phoneticFirstName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileEmail() {
        return mobileEmail;
    }

    public void setMobileEmail(String mobileEmail) {
        this.mobileEmail = mobileEmail;
    }

    @Override
    public String toString() {
        return "{" +
                "firstName='" + firstName + '\'' +
                ", lastname='" + lastname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mobileEmail='" + mobileEmail + '\'' +
                ", prefix='" + prefix + '\'' +
                ", middleName='" + middleName + '\'' +
                ", suffix='" + suffix + '\'' +
                ", phoneticFirstName='" + phoneticFirstName + '\'' +
                ", phoneticMiddleName='" + phoneticMiddleName + '\'' +
                ", phoneticLastName='" + phoneticLastName + '\'' +
                ", letters='" + letters + '\'' +
                ", Logo='" + Logo + '\'' +
                ", name='" + name + '\'' +
                ", homeNum='" + homeNum + '\'' +
                ", jobNum='" + jobNum + '\'' +
                ", workFax='" + workFax + '\'' +
                '}';
    }
}
