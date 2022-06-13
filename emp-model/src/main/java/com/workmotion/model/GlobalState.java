package com.workmotion.model;

public enum GlobalState {
	
	ADDED ("ADDED") {
		@Override
		public GlobalState nextState() {
			return IN_CHECK;
		}
	},
	IN_CHECK ("IN_CHECK") {
		@Override
		public GlobalState nextState() {
			return APPROVED;
		}
	},
	APPROVED ("APPROVED") {
		@Override
		public GlobalState nextState() {
			return ACTIVE;
		}
	},
	ACTIVE ("ACTIVE") {
		@Override
		public GlobalState nextState() {
			return null;
		}
	},
	
	
	// This state is used to counter concurrency writes 
	PENDING ("PENDING") {
		@Override
		public GlobalState nextState() {
			return null;
		}
	};

	private String name;
	
	private GlobalState(String name) {
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
	
	public abstract GlobalState nextState();
}
