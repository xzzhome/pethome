package com.xzz.org.util;

import lombok.Data;

@Data
public class AjaxResult {
	
	private Boolean success=true;
	private String msg;

	public AjaxResult(Boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}
	public AjaxResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
