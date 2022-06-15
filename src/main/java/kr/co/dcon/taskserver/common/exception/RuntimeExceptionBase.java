package kr.co.dcon.taskserver.common.exception;


import kr.co.dcon.taskserver.common.constants.ResultCode;


public class RuntimeExceptionBase extends RuntimeException {
	private static final long serialVersionUID = 8903837184113759431L;

	private final ResultCode resultCode;

	public RuntimeExceptionBase(ResultCode resultCode) {
		super();
		this.resultCode = resultCode;
	}

	public RuntimeExceptionBase(ResultCode resultCode, String msg) {
		super(msg);
		this.resultCode = resultCode;
	}

	public RuntimeExceptionBase(ResultCode resultCode, String msg, Throwable t) {
		super(msg, t);
		this.resultCode = resultCode;
	}

	public ResultCode getErrorCode() {
		return resultCode;
	}

}
