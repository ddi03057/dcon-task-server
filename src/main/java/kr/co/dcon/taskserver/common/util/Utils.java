package kr.co.dcon.taskserver.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Utils {

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);


	private Utils() {
		throw new IllegalStateException("Utils class");
	}

	public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	public static String encryptData(String encryptData) {

		String base = encryptData;

		try {

			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));

			StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}

	}

	public static String getRandomString() {

		char[] pwCollectionAll = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E',
				'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
				'v', 'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')' };
		char[] pwCollectionSpCha = new char[] { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')' };
		char[] pwCollectionNum = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', };
		StringBuilder ranPw = new StringBuilder();
		StringBuilder ranPwNum = new StringBuilder();
		StringBuilder ranPwSpCha = new StringBuilder();
		Random random = new SecureRandom();
		for (int i = 0; i < 1; i++) {
			int selectRandomPw = random.nextInt(pwCollectionNum.length);
			ranPwNum.append(pwCollectionNum[selectRandomPw]);
		}
		for (int i = 0; i < 1; i++) {
			int selectRandomPw = random.nextInt(pwCollectionSpCha.length);
			ranPwSpCha.append(pwCollectionSpCha[selectRandomPw]);
		}
		for (int i = 0; i < 8; i++) {
			int selectRandomPw = random.nextInt(pwCollectionAll.length);
			ranPw.append(pwCollectionAll[selectRandomPw]);
		}
		return ranPwSpCha.toString() + ranPw.toString() + ranPwNum.toString();
	}

	public static String getRealIp(HttpServletRequest request) {
		List<String> checkHeaderNames = new ArrayList<>(
				Arrays.asList("x-forwarded-for", "x-forward-for", "http_x_forwarded_for", "x_forwarded_for",
						"http_client_ip", "wl-proxy-client-ip", "proxy-client-ip", "remote_addr", "x-real-ip"));
		String requestIp = "";

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {

			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);

			if (checkHeaderNames.contains(headerName.toLowerCase())) {
				requestIp = headerValue;
			}
		}

		if (!StringUtils.isEmpty(requestIp) && requestIp.indexOf(',') > -1) {
			requestIp = requestIp.split(",")[0].trim();
		}

		if (StringUtils.isEmpty(requestIp)) {
			requestIp = request.getRemoteAddr();
		}

		return requestIp;
	}

	public static boolean isEmptyText(String str) {
		// null 인 경우 return true
		if (str == null) {
			return true;
		}
		// 공백 제거
		str = str.replace("\\p{Z}", ""); // 띄어쓰기 공백 제거
		str = str.replace("\\p{Z}", ""); // ㄱ + 한자, 1번 공백 제거
		// 공백 제거 후 length 가 1보다 작으면 return true
		return str.length() < 1;
	}

	public static String toNotNull(String value, String defaultValue) {
		String returnValue;
		if (value == null || value.trim().equals("") || value.trim().equals("null")) {
			returnValue = defaultValue;
		} else {
			returnValue = value;
		}
		return returnValue;
	}

	public static boolean isEmpty(Object obj) {

		if (obj == null)
			return true;

		if ((obj instanceof String) && (((String) obj).trim().length() == 0)) {
			return true;
		}

		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}

		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}

		if (obj instanceof List) {
			return ((List<?>) obj).isEmpty();
		}

		if (obj instanceof Object[]) {
			return (((Object[]) obj).length == 0);
		}

		return false;

	}

	public static String getCurrentDate() {
		Date dateNow = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST")).getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		return formatter.format(dateNow);

	}

	public static String getCurrentDateYMD() {
		Date dateNow = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST")).getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
		return formatter.format(dateNow);

	}

	public static String getCurrentDateYMDHypen() {
		Date dateNow = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST")).getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		return formatter.format(dateNow);

	}

	public static String getCurrentMonth() {
		Date dateNow = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST")).getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM", Locale.getDefault());
		return formatter.format(dateNow);

	}

	public static String getMonthAgoMonth(int n) {
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST"));
		cal.add(Calendar.MONTH, n); // 한달전 날짜 가져오기
		Date monthago = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM", Locale.getDefault());
		return formatter.format(monthago);
	}

	public static String getAgoDate(int n) {
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST"));
		cal.add(Calendar.DATE, n); // 한달전 날짜 가져오기
		Date monthago = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		return formatter.format(monthago);
	}

	public static String getCurrentDateYYYMMD() {
		// 2020-05-11 11:16:03
		Date dateNow = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST")).getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		return formatter.format(dateNow);

	}

	public static String getMonthAgoDate(int n) {
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST"));
		cal.add(Calendar.MONTH, n); // 한달전 날짜 가져오기
		Date monthago = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM", Locale.getDefault());
		return formatter.format(monthago);
	}

	public static long getCurrentDateTimeStamp() throws ParseException {
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(0x1ee6280, "KST"));
		cal.add(Calendar.MINUTE, -30); // 한달전 날짜 가져오기
		Date tenMinutsAgoe = cal.getTime();
		logger.info("===tenMinutsAgoe:::{}", tenMinutsAgoe);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		String date =formatter.format(tenMinutsAgoe);
		logger.info("===date:::{}", date);
		Date parsedTimeStamp = formatter.parse(date);
		logger.info("===parsedTimeStamp:::{}", parsedTimeStamp);
		Timestamp timestamp1 = new Timestamp(parsedTimeStamp.getTime());
		long timestamp = timestamp1.getTime();

		logger.info("===timestamp1:::{}", timestamp1);
		logger.info("===timestamp:::{}", timestamp);

		return timestamp;

	}
	public static String replace(String src, String from, String to) {
		if (src == null) {
			return null;
		}
		if (from == null) {
			return src;
		}
		if (to == null) {
			to = "";
		}

		StringBuilder buf = new StringBuilder();

		for (int pos; (pos = src.indexOf(from)) >= 0;) {
			buf.append(src.substring(0, pos));
			buf.append(to);
			src = src.substring(pos + from.length());
		}

		buf.append(src);
		return buf.toString();
	}
	public static String reverseXmlString(String strText) {
		if (strText == null) {
			strText = "";
		} else {
			strText = strText.trim();

			strText = replace(strText, "&amp;", "&");
			strText = replace(strText, "&lt;", "<");
			strText = replace(strText, "&gt;", ">");
			strText = replace(strText, "&apos;", "'");
			strText = replace(strText, "&quot;", "\"");
		}

		return strText;
	}

	public static boolean byteCheck(String txt, int standardByte) {

		// 바이트 체크 (영문 1, 한글 2, 특문 1)
		int en = 0;
		int ko = 0;
		int etc = 0;
		boolean  result= true;
		char[] txtChar = txt.toCharArray();
		for (int j = 0; j < txtChar.length; j++) {
			if (txtChar[j] >= 'A' && txtChar[j] <= 'z') {
				en++;
			} else if (txtChar[j] >= '\uAC00' && txtChar[j] <= '\uD7A3') {
				ko++;
				ko++;
			} else {
				etc++;
			}
		}

		int txtByte = en + ko + etc;
		if (txtByte > standardByte) {
			result=  false;
		} else {
			result= true;
		}
		return result;
	}

	public static String removeComma(String str) {
		if (str.endsWith(",")) {
			return str.substring(0, str.length() - 1);
		}
		return str;
	}

	public static String nvl(String str, String rep) {
        return str==null ? rep : str;
    }

}
