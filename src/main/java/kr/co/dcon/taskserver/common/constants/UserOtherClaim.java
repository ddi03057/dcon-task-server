package kr.co.dcon.taskserver.common.constants;

public final class UserOtherClaim {
	private UserOtherClaim() {}
	public static final String RECEIVE_SMS = "RECEIVE_SMS";
	public static final String USER_REGIST_COMPANY_NAME = "USER_REGIST_COMPANY_NAME";
	public static final String GROUPS = "groups";
	public static final String USER_NAME = "USER_NM";
	public static final String USER_EMAIL = "USER_EMAIL";
	public static final String USER_TEL_NO = "USER_TEL_NO";
	public static final String USER_MBL_TEL_CNTR_CD = "USER_MBL_TEL_CNTR_CD";
	public static final String USE_YN = "USE_YN";
	public static final String LAST_LOGIN_DT = "LAST_LOGIN_DT";
	public static final String LAST_LOGIN_DEVICE = "LAST_LOGIN_DEVICE";
	public static final String LAST_LOGIN_IPADDR = "LAST_LOGIN_IPADDR";
	public static final String DISABLE_MULTI_LOGIN = "DISABLE_MULTI_LOGIN";
	public static final String PWD_INIT_YN = "PWD_INIT_YN";
	public static final String PWD_CHG_DT = "PWD_CHG_DT";
	public static final String SKIP_PWD_CHG_DT = "SKIP_PWD_CHG_DT";
	public static final String COMPANY_CODE = "CMPN_CD"; // '1000'
	public static final String CUSTOMER_CODE = "CUS_CD"; // 베스핀 내부 직원은 다 'X10000' 의미로 정한 코드 사용
	public static final String CUS_DEPT_CD = "CUS_DEPT_CD"; // 베스핀 내부 직원은 자신의 tocp.employee_m.COST_CEN_CD 기존에 어떤 분이 이렇게 해놔서 어쩔수 없음.
	public static final String CREATE_ID = "CREA_ID";
	public static final String UPDATED = "UPDT_DT";
	public static final String UPDATED_ID = "UPDT_ID";
	public static final String ERROR_CNT = "ERROR_CNT";
	public static final String USE_OTP = "USE_OTP";
	public static final String LOCALE = "locale";
	public static final String CUR_CMPN_ID = "CUR_CMPN_ID";
	public static final String CUR_GRP_ID = "CUR_GRP_ID";

}
