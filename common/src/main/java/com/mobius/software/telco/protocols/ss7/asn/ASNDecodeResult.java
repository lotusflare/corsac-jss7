package com.mobius.software.telco.protocols.ss7.asn;

public class ASNDecodeResult 
{
	private Object result;
	private Object parent;
	private Boolean hadErrors;
	public ASNDecodeResult firstError;
	
	public ASNDecodeResult(Object result,Object parent,Boolean hadErrors,ASNDecodeResult firstError) {
		this.result=result;
		this.parent=parent;
		this.hadErrors=hadErrors;
		this.firstError=firstError;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public Boolean getHadErrors() {
		return hadErrors;
	}

	public void setHadErrors(Boolean hadErrors) {
		this.hadErrors = hadErrors;
	}
	
	public ASNDecodeResult getFirstError() {
		return firstError;
	}		
}
