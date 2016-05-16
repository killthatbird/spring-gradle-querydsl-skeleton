/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.park.spring.skeleton.base.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author park
 */
@Embeddable
public class TMstCodePK implements Serializable, BaseEntity {
	private static final long serialVersionUID = -231787095970064270L;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "mst_code_master_key")
    private short mstCodeMasterKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mst_code_sub_key")
    private short mstCodeSubKey;

    public TMstCodePK() {
    }

    public TMstCodePK(short mstCodeMasterKey, short mstCodeSubKey) {
        this.mstCodeMasterKey = mstCodeMasterKey;
        this.mstCodeSubKey = mstCodeSubKey;
    }

    public short getMstCodeMasterKey() {
        return mstCodeMasterKey;
    }

    public void setMstCodeMasterKey(short mstCodeMasterKey) {
        this.mstCodeMasterKey = mstCodeMasterKey;
    }

    public short getMstCodeSubKey() {
        return mstCodeSubKey;
    }

    public void setMstCodeSubKey(short mstCodeSubKey) {
        this.mstCodeSubKey = mstCodeSubKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) mstCodeMasterKey;
        hash += (int) mstCodeSubKey;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TMstCodePK)) {
            return false;
        }
        TMstCodePK other = (TMstCodePK) object;
        if (this.mstCodeMasterKey != other.mstCodeMasterKey) {
            return false;
        }
        if (this.mstCodeSubKey != other.mstCodeSubKey) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jp.ideacross.mocs.app.TMstCodePK[ mstCodeMasterKey=" + mstCodeMasterKey + ", mstCodeSubKey=" + mstCodeSubKey + " ]";
    }
    
}
