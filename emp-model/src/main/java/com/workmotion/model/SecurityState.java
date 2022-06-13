package com.workmotion.model;

public enum SecurityState {
	
	SECURITY_CHECK_STARTED ("SECURITY_CHECK_STARTED") {
		@Override
		public SecurityState nextState() {
			return SECURITY_CHECK_FINISHED;
		}
	},
	SECURITY_CHECK_FINISHED ("SECURITY_CHECK_FINISHED") {
		@Override
		public SecurityState nextState() {
			return null;
		}
	};

	private String name;
	
	private SecurityState(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public abstract SecurityState nextState();
}
