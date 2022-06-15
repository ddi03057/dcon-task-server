package kr.co.dcon.taskserver.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtils {

	private NumberUtils(){}

	private static DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###,##0");

	public static String geThousandth(Object number) {
		String numberStr = String.valueOf(number);
		BigDecimal decimal = new BigDecimal(numberStr);
		return decimalFormat.format(decimal);
	}
}
