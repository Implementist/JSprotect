package com.rocky.protect;

import java.util.ArrayList;

public class Protection {
	public ArrayList<Integer> protect(String fileName, int flatternStrength, int opaqueStrength){


		ArrayList<Integer> strength = new ArrayList<Integer>();
		Antidbg antidbg = new Antidbg(fileName);
		Obfuscation obfuscation = new Obfuscation();
		//设定混淆强度
		obfuscation.setFlatternStrength(flatternStrength);
		obfuscation.setOpaqueStrength(opaqueStrength);
		antidbg.setObfuscationObject(obfuscation);
		antidbg.injectAntiDbg();

		strength.add(obfuscation.getFlatternCount());
		strength.add(obfuscation.getOpaqueCount());

		//System.out.println("Protection successfully!");

		return strength;
	}

}
