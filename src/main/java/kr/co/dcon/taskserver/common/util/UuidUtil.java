package kr.co.dcon.taskserver.common.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UuidUtil {
	private static HashMap<String, String> prefixMap = new HashMap<>();
	private static HashMap<String, String> prefixMapRev = new HashMap<>();
	private static boolean prefared = false;

	private static void prefare()
	{
		if (!prefared)
		{
			prefared = true;
			prefixMap.put("bu", "bf");
			prefixMapRev.put("bf", "bu");

		}
	}

	public static String getUUID()
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static byte[] uuidToByte(String id)
	{
		prefare();
		if (id == null) return null;
		String replaced = id.replaceAll("-", "");

		for(Map.Entry<String, String> entry : prefixMap.entrySet())
		{
			if (replaced.startsWith(entry.getKey())) {
				replaced = replaced.replaceFirst(entry.getKey(), entry.getValue());
				break;
			}
		}

		int len = replaced.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(replaced.charAt(i), 16) << 4)
					+ Character.digit(replaced.charAt(i+1), 16));
		}
		return data;
	}

	public static String byteToUuid(byte[] idData)
	{
		if (idData == null) return null;
		StringBuilder sb = new StringBuilder();

		int byteIdx = 0;
		for(byte b : idData){
			sb.append(String.format("%02X", b&0xff));
			byteIdx++;

			if (byteIdx == 4 || byteIdx == 6 || byteIdx == 8 || byteIdx == 10)
			{
				sb.append("-");
			}
		}

		String retVal = sb.toString().toLowerCase();

		for(Map.Entry<String, String> entry : prefixMapRev.entrySet())
		{
			if (retVal.startsWith(entry.getKey())) {
				retVal = retVal.replaceFirst(entry.getKey(), entry.getValue());
				break;
			}
		}
		return retVal;
	}
}
