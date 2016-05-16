package com.park.spring.skeleton.front.web.form;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FrontMember implements Serializable {

	private static final long serialVersionUID = -2607866753866511623L;
	
	private Integer frontMemberKey;
	
	@NotNull
	@Size(min=6, max = 12)
    private String frontMemberLoginId;
	
	@Size(min=6, max = 12)
    private String frontMemberLoginPassword;
	
	@NotNull
	private Boolean frontMemberIsLocked;
	
	@NotNull
	private Short frontMemberStatusTp;
    
	@NotNull
    private Short frontMemberTp;
	
	@NotNull
    @Size(max = 50)
    private String frontMemberName;

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

	public Boolean getFrontMemberIsLocked() {
		return frontMemberIsLocked;
	}

	public void setFrontMemberIsLocked(Boolean frontMemberIsLocked) {
		this.frontMemberIsLocked = frontMemberIsLocked;
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
    
    
    
	
}
