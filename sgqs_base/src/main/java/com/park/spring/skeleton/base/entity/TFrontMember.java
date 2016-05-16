/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.park.spring.skeleton.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author park
 */
@Entity
@Table(name = "t_front_member")
@XmlRootElement
/*@NamedQueries({
    @NamedQuery(name = "TFrontMember.findAll", query = "SELECT t FROM TFrontMember t"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberKey", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberKey = :frontMemberKey"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberLoginId", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberLoginId = :frontMemberLoginId"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberLoginPassword", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberLoginPassword = :frontMemberLoginPassword"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberLoginDt", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberLoginDt = :frontMemberLoginDt"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberIsLocked", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberIsLocked = :frontMemberIsLocked"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberLoginFailCnt", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberLoginFailCnt = :frontMemberLoginFailCnt"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberLoginFailDt", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberLoginFailDt = :frontMemberLoginFailDt"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberApprovalDt", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberApprovalDt = :frontMemberApprovalDt"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberStatusTp", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberStatusTp = :frontMemberStatusTp"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberTp", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberTp = :frontMemberTp"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberName", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberName = :frontMemberName"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberEmail", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberEmail = :frontMemberEmail"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberRegDt", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberRegDt = :frontMemberRegDt"),
    @NamedQuery(name = "TFrontMember.findByFrontMemberUpdDt", query = "SELECT t FROM TFrontMember t WHERE t.frontMemberUpdDt = :frontMemberUpdDt")})
    */
public class TFrontMember implements Serializable, BaseEntity {
	//FieldHandled
	
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "front_member_key")
    private Integer frontMemberKey;
    @Size(max = 12)
    @Column(name = "front_member_login_id")
    private String frontMemberLoginId;
    @Size(max = 64)
    @Column(name = "front_member_login_password")
    private String frontMemberLoginPassword;
    @Column(name = "front_member_login_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frontMemberLoginDt;
    @Column(name = "front_member_is_locked")
    private Boolean frontMemberIsLocked;
    @Column(name = "front_member_login_fail_cnt")
    private Short frontMemberLoginFailCnt;
    @Column(name = "front_member_login_fail_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frontMemberLoginFailDt;
    @Column(name = "front_member_approval_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frontMemberApprovalDt;
    @Column(name = "front_member_status_tp")
    private Short frontMemberStatusTp;
    @Column(name = "front_member_tp")
    private Short frontMemberTp;
    @Size(max = 50)
    @Column(name = "front_member_name")
    private String frontMemberName;
    @Size(max = 100)
    @Column(name = "front_member_email")
    private String frontMemberEmail;
    @Column(name = "front_member_reg_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frontMemberRegDt;
    @Column(name = "front_member_upd_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frontMemberUpdDt;

    public TFrontMember() {
    }

    public TFrontMember(Integer frontMemberKey) {
        this.frontMemberKey = frontMemberKey;
    }

    public Integer getFrontMemberKey() {
        return frontMemberKey;
    }

    public void setFrontMemberKey(Integer frontMemberKey) {
        this.frontMemberKey = frontMemberKey;
    }

    public String getFrontMemberLoginId() {
        return frontMemberLoginId;
    }

    public void setFrontMemberLoginId(String frontMemberLoginId) {
        this.frontMemberLoginId = frontMemberLoginId;
    }

    public String getFrontMemberLoginPassword() {
        return frontMemberLoginPassword;
    }

    public void setFrontMemberLoginPassword(String frontMemberLoginPassword) {
        this.frontMemberLoginPassword = frontMemberLoginPassword;
    }

    public Date getFrontMemberLoginDt() {
        return frontMemberLoginDt;
    }

    public void setFrontMemberLoginDt(Date frontMemberLoginDt) {
        this.frontMemberLoginDt = frontMemberLoginDt;
    }

    public Boolean getFrontMemberIsLocked() {
        return frontMemberIsLocked;
    }

    public void setFrontMemberIsLocked(Boolean frontMemberIsLocked) {
        this.frontMemberIsLocked = frontMemberIsLocked;
    }

    public Short getFrontMemberLoginFailCnt() {
        return frontMemberLoginFailCnt;
    }

    public void setFrontMemberLoginFailCnt(Short frontMemberLoginFailCnt) {
        this.frontMemberLoginFailCnt = frontMemberLoginFailCnt;
    }

    public Date getFrontMemberLoginFailDt() {
        return frontMemberLoginFailDt;
    }

    public void setFrontMemberLoginFailDt(Date frontMemberLoginFailDt) {
        this.frontMemberLoginFailDt = frontMemberLoginFailDt;
    }

    public Date getFrontMemberApprovalDt() {
        return frontMemberApprovalDt;
    }

    public void setFrontMemberApprovalDt(Date frontMemberApprovalDt) {
        this.frontMemberApprovalDt = frontMemberApprovalDt;
    }

    public Short getFrontMemberStatusTp() {
        return frontMemberStatusTp;
    }

    public void setFrontMemberStatusTp(Short frontMemberStatusTp) {
        this.frontMemberStatusTp = frontMemberStatusTp;
    }

    public Short getFrontMemberTp() {
        return frontMemberTp;
    }

    public void setFrontMemberTp(Short frontMemberTp) {
        this.frontMemberTp = frontMemberTp;
    }

    public String getFrontMemberName() {
        return frontMemberName;
    }

    public void setFrontMemberName(String frontMemberName) {
        this.frontMemberName = frontMemberName;
    }

    public String getFrontMemberEmail() {
        return frontMemberEmail;
    }

    public void setFrontMemberEmail(String frontMemberEmail) {
        this.frontMemberEmail = frontMemberEmail;
    }

    public Date getFrontMemberRegDt() {
        return frontMemberRegDt;
    }

    public void setFrontMemberRegDt(Date frontMemberRegDt) {
        this.frontMemberRegDt = frontMemberRegDt;
    }

    public Date getFrontMemberUpdDt() {
        return frontMemberUpdDt;
    }

    public void setFrontMemberUpdDt(Date frontMemberUpdDt) {
        this.frontMemberUpdDt = frontMemberUpdDt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (frontMemberKey != null ? frontMemberKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TFrontMember)) {
            return false;
        }
        TFrontMember other = (TFrontMember) object;
        if ((this.frontMemberKey == null && other.frontMemberKey != null) || (this.frontMemberKey != null && !this.frontMemberKey.equals(other.frontMemberKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.park.spring.skeleton.base.entity.TFrontMember[ frontMemberKey=" + frontMemberKey + " ]";
    }
        
}
