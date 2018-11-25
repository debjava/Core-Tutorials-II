package com.dds.core.statefull.session.ejb;

import javax.ejb.SessionBean;	
import javax.ejb.SessionContext;	

public class SayMyNameBean implements SessionBean {	

	private static final long serialVersionUID = -4124364436623456451L;
	String myName;   
	int countNameCalled;
	//All methods in the Remote interface must be implemented here:
	public String getMyName() 
	{
		System.out.println("getMyName() method called ......");
		if (++countNameCalled > 10) {
			setMyName("ok");
		}
		return myName;
	}

	public int getCount() {
		return countNameCalled;
	}

	public void setMyName(String nameIn) {
		myName = nameIn;
		countNameCalled = 0;
	}   

	public void SayMyNameBean() {}

	//the following five methods must be in the Enterprise Bean Class:
	public void ejbCreate() {
		countNameCalled = 0;
	}
	//ejbCreate() must match Home Interface's create() method signature
	public void ejbRemove() {}
	public void ejbActivate() {}
	public void ejbPassivate() {}
	public void setSessionContext(SessionContext sessionContext) {}
}
