package com.rocky.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	public static String getMd5(String plainText)
	{
		StringBuffer buffer = new StringBuffer("");
		
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			
			byte b[] = md.digest();
			
			int i, len;
			len = b.length;
	
			for(int offset = 0; offset < len; ++offset)
			{
				i = b[offset];
				if(i < 0)
					i += 256;
				if(i < 16)
					buffer.append("0");
				buffer.append(Integer.toHexString(i));
			}
			
		}
		catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
