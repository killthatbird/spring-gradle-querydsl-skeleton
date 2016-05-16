package com.park.spring.skeleton.base.util;


import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SecurityUtilTest {

	@Test
	public void testCheckPasswordEncoder() {
		
		String salt = "test1234#%";
		String password = "ea2eR332)#";
		
		String encPass = SecurityUtil.shaPasswordEncoder(salt, password);
		
	    assertEquals(SecurityUtil.isShaPasswordType(encPass, salt, password), true);
	}
}
