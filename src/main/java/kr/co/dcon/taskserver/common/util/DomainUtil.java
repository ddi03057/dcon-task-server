package kr.co.dcon.taskserver.common.util;

import com.google.common.net.InternetDomainName;
import org.springframework.security.authentication.AuthenticationServiceException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class DomainUtil {

	public static String getTopPrivateDomain(String redirectUriAfterLogin) {
		InternetDomainName domainName = InternetDomainName.from(getHost(redirectUriAfterLogin));
		return (domainName.isTopPrivateDomain())?	domainName.toString() : domainName.topPrivateDomain().toString();
	}
	
	public static String getSubDomain(String redirectUriAfterLogin) {
		InternetDomainName domainName = InternetDomainName.from(getHost(redirectUriAfterLogin));
		return domainName.parts().get(0);
	}
	
	public static String getRootUrl(String requestUrl) {
		URL url = null;
		try {
			url = new URL(requestUrl);
		} catch (MalformedURLException e) {
			throw new AuthenticationServiceException(e.getMessage(), e); 
		}
		String rootUrl = String.format("%s://%s", url.getProtocol(), url.getHost());
		if (url.getPort() > 0) {
			rootUrl += ":" + url.getPort();
		}
		return rootUrl;
	}
	
	public static String getHost(String redirectUriAfterLogin) {
		URL url = null;
		try {
			url = new URL(redirectUriAfterLogin);
		} catch (MalformedURLException e) {
			throw new AuthenticationServiceException(e.getMessage(), e); 
		}
		return url.getHost();
	}
	
	public static boolean isValidEmail(String email) {
		
		boolean isValid = true;
		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
		} catch (AddressException e) {
			isValid = false;
		}
		
		return isValid;
	}
}
