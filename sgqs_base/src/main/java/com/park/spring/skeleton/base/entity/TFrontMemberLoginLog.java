/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
@Table(name = "t_front_member_login_log")
@XmlRootElement
/*@NamedQueries({
    @NamedQuery(name = "TFrontMemberLoginLog.findAll", query = "SELECT t FROM TFrontMemberLoginLog t"),
    @NamedQuery(name = "TFrontMemberLoginLog.findByFrontMemberLoginLogKey", query = "SELECT t FROM TFrontMemberLoginLog t WHERE t.frontMemberLoginLogKey = :frontMemberLoginLogKey"),
    @NamedQuery(name = "TFrontMemberLoginLog.findByFrontMemberLoginLogLoginId", query = "SELECT t FROM TFrontMemberLoginLog t WHERE t.frontMemberLoginLogLoginId = :frontMemberLoginLogLoginId"),
    @NamedQuery(name = "TFrontMemberLoginLog.findByFrontMemberLoginLogLoginIp", query = "SELECT t FROM TFrontMemberLoginLog t WHERE t.frontMemberLoginLogLoginIp = :frontMemberLoginLogLoginIp"),
    @NamedQuery(name = "TFrontMemberLoginLog.findByFrontMemberLoginLogLoginDt", query = "SELECT t FROM TFrontMemberLoginLog t WHERE t.frontMemberLoginLogLoginDt = :frontMemberLoginLogLoginDt")})*/
public class TFrontMemberLoginLog implements Serializable, BaseEntity {
	private static final long serialVersionUID = 3762191987735510056L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "front_member_login_log_key")
    private Integer frontMemberLoginLogKey;
    @Size(max = 12)
    @Column(name = "front_member_login_log_login_id")
    private String frontMemberLoginLogLoginId;
    @Size(max = 20)
    @Column(name = "front_member_login_log_login_ip")
    private String frontMemberLoginLogLoginIp;
    @Column(name = "front_member_login_log_login_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date frontMemberLoginLogLoginDt;

    public TFrontMemberLoginLog() {
    }

    public TFrontMemberLoginLog(Integer frontMemberLoginLogKey) {
        this.frontMemberLoginLogKey = frontMemberLoginLogKey;
    }

    public Integer getFrontMemberLoginLogKey() {
        return frontMemberLoginLogKey;
    }

    public void setFrontMemberLoginLogKey(Integer frontMemberLoginLogKey) {
        this.frontMemberLoginLogKey = frontMemberLoginLogKey;
    }

    public String getFrontMemberLoginLogLoginId() {
        return frontMemberLoginLogLoginId;
    }

    public void setFrontMemberLoginLogLoginId(String frontMemberLoginLogLoginId) {
        this.frontMemberLoginLogLoginId = frontMemberLoginLogLoginId;
    }

    public String getFrontMemberLoginLogLoginIp() {
        return frontMemberLoginLogLoginIp;
    }

    public void setFrontMemberLoginLogLoginIp(String frontMemberLoginLogLoginIp) {
        this.frontMemberLoginLogLoginIp = frontMemberLoginLogLoginIp;
    }

    public Date getFrontMemberLoginLogLoginDt() {
        return frontMemberLoginLogLoginDt;
    }

    public void setFrontMemberLoginLogLoginDt(Date frontMemberLoginLogLoginDt) {
        this.frontMemberLoginLogLoginDt = frontMemberLoginLogLoginDt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (frontMemberLoginLogKey != null ? frontMemberLoginLogKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TFrontMemberLoginLog)) {
            return false;
        }
        TFrontMemberLoginLog other = (TFrontMemberLoginLog) object;
        if ((this.frontMemberLoginLogKey == null && other.frontMemberLoginLogKey != null) || (this.frontMemberLoginLogKey != null && !this.frontMemberLoginLogKey.equals(other.frontMemberLoginLogKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.co.ideacross.fas.ma.base.model.TFrontMemberLoginLog[ frontMemberLoginLogKey=" + frontMemberLoginLogKey + " ]";
    }
    
}
