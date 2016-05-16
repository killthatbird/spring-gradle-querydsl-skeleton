/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.park.spring.skeleton.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author park
 */
@Entity
@Table(name = "t_mst_code")
@XmlRootElement
/*
@NamedQueries({
    @NamedQuery(name = "TMstCode.findAll", query = "SELECT t FROM TMstCode t"),
    @NamedQuery(name = "TMstCode.findByMstCodeMasterKey", query = "SELECT t FROM TMstCode t WHERE t.tMstCodePK.mstCodeMasterKey = :mstCodeMasterKey"),
    @NamedQuery(name = "TMstCode.findByMstCodeSubKey", query = "SELECT t FROM TMstCode t WHERE t.tMstCodePK.mstCodeSubKey = :mstCodeSubKey"),
    @NamedQuery(name = "TMstCode.findByMstCodeNm", query = "SELECT t FROM TMstCode t WHERE t.mstCodeNm = :mstCodeNm"),
    @NamedQuery(name = "TMstCode.findByMstCodeDesc", query = "SELECT t FROM TMstCode t WHERE t.mstCodeDesc = :mstCodeDesc"),
    @NamedQuery(name = "TMstCode.findByMstCodeIsEnabled", query = "SELECT t FROM TMstCode t WHERE t.mstCodeIsEnabled = :mstCodeIsEnabled"),
    @NamedQuery(name = "TMstCode.findByMstCodeVarNm", query = "SELECT t FROM TMstCode t WHERE t.mstCodeVarNm = :mstCodeVarNm")})
*/
public class TMstCode implements Serializable, BaseEntity {
	private static final long serialVersionUID = -5476723231019515329L;
	
    @EmbeddedId
    protected TMstCodePK tMstCodePK;
    @Size(max = 50)
    @Column(name = "mst_code_nm")
    private String mstCodeNm;
    @Size(max = 200)
    @Column(name = "mst_code_desc")
    private String mstCodeDesc;
    @Column(name = "mst_code_is_enabled")
    private Boolean mstCodeIsEnabled;
    @Size(max = 50)
    @Column(name = "mst_code_var_nm")
    private String mstCodeVarNm;
    @Size(max = 2)
    @Column(name = "mst_code_priority")
    private short mstCodePriority;
    public TMstCode() {
    }

    public TMstCode(TMstCodePK tMstCodePK) {
        this.tMstCodePK = tMstCodePK;
    }

    public TMstCode(short mstCodeMasterKey, short mstCodeSubKey) {
        this.tMstCodePK = new TMstCodePK(mstCodeMasterKey, mstCodeSubKey);
    }

    public TMstCodePK getTMstCodePK() {
        return tMstCodePK;
    }

    public void setTMstCodePK(TMstCodePK tMstCodePK) {
        this.tMstCodePK = tMstCodePK;
    }

    public String getMstCodeNm() {
        return mstCodeNm;
    }

    public void setMstCodeNm(String mstCodeNm) {
        this.mstCodeNm = mstCodeNm;
    }

    public String getMstCodeDesc() {
        return mstCodeDesc;
    }

    public void setMstCodeDesc(String mstCodeDesc) {
        this.mstCodeDesc = mstCodeDesc;
    }

    public Boolean getMstCodeIsEnabled() {
        return mstCodeIsEnabled;
    }

    public void setMstCodeIsEnabled(Boolean mstCodeIsEnabled) {
        this.mstCodeIsEnabled = mstCodeIsEnabled;
    }

    public String getMstCodeVarNm() {
        return mstCodeVarNm;
    }

    public void setMstCodeVarNm(String mstCodeVarNm) {
        this.mstCodeVarNm = mstCodeVarNm;
    }
    
    public short getMstCodePriority() {
        return mstCodePriority;
    }

    public void setMstCodePriority(short mstCodePriority) {
        this.mstCodePriority = mstCodePriority;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tMstCodePK != null ? tMstCodePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TMstCode)) {
            return false;
        }
        TMstCode other = (TMstCode) object;
        if ((this.tMstCodePK == null && other.tMstCodePK != null) || (this.tMstCodePK != null && !this.tMstCodePK.equals(other.tMstCodePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ideacross.mocs.app.TMstCode[ tMstCodePK=" + tMstCodePK + " ]";
    }
    
}
