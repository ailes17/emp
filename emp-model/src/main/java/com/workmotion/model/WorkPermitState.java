package com.workmotion.model;

public enum WorkPermitState {

	WORK_PERMIT_CHECK_STARTED ("WORK_PERMIT_CHECK_STARTED") {
		@Override
		public WorkPermitState nextState() {
			return WORK_PERMIT_CHECK_PENDING_VERIFICATION;
		}
	},
	WORK_PERMIT_CHECK_PENDING_VERIFICATION ("WORK_PERMIT_CHECK_PENDING_VERIFICATION") {
		@Override
		public WorkPermitState nextState() {
			return WORK_PERMIT_CHECK_FINISHED;
		}
	},
	WORK_PERMIT_CHECK_FINISHED ("WORK_PERMIT_CHECK_FINISHED") {
		@Override
		public WorkPermitState nextState() {
			return null;
		}
	};
	
	private String name;
	
	/**
	 * @return the name
	 */
	WorkPermitState(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public abstract WorkPermitState nextState();
}
