package kr.co.dcon.taskserver.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


// @Configuration
@Slf4j
public class SecureCheckValidationFilter extends HttpServletRequestWrapper {


  private static final String SQL_INJECTION_REGULAR_WORDS =
      "[^\\p{Alnum}]|select|delete|update|insert|create|alter|drop";
  private static final String XSS_REGULAR_WORDS_1 =
      "[^\\p{Alnum}]|javascript|script|iframe|document|vbscript|applet|embed|object";
  private static final String XSS_REGULAR_WORDS_2 =
      "|frame|grameset|layer|bgsound|alert|onblur|onchange|onclick|ondblclick|enerror|onfocus|onload|onmouse|onscroll|onsubmit|onunload";
  private static final String XSS_REGULAR_WORDS = XSS_REGULAR_WORDS_1 + XSS_REGULAR_WORDS_2;

  /* 예외 파라미터 명 */
  private static final String[] EXCEPTION_WORDS = {"id", "password", "emplyrId", "passwordInf"};

  private static final String[] SQL_INJECTION_WORDS =
      {"delete", "update", "insert", "create", "alter", "drop"
      // , "select"
      };
  /* XSS Words */
  private static final String[] TRANSFER_XSS_WORDS = {"javascript", "script", "iframe", "document",
      "vbscript", "applet", "embed", "object", "frame", "grameset", "layer", "bgsound", "alert",
      "onblur", "onchange", "onclick", "ondblclick", "enerror", "onfocus", "onload", "onmouse",
      "onscroll", "onsubmit", "onunload"};


  public SecureCheckValidationFilter(HttpServletRequest request) {
    super(request);
    initialize();
  }

  public void initialize() {
    Pattern.compile(SQL_INJECTION_REGULAR_WORDS, Pattern.CASE_INSENSITIVE);
    Pattern.compile(XSS_REGULAR_WORDS, Pattern.CASE_INSENSITIVE);

  }


  /**
   * @Method Name : getParameter
   * @Method Desc : getParameter 호출 시 Html Tag 치환, SQL Injection 방지, Cross Site Script 방지
   *
   * @작성일 : Jun 7, 2019
   * @작성자 : zest
   * @변경이력 : 이름 : 일자 : 근거자료 : 변경내용
   *       ------------------------------------------------------------------- zest : Jun 7, 2019 :
   *       : 변경 개발 . html tag 방지를 Spring lib로 변경
   *
   * @param value
   * @return
   */

  @Override
  @Bean
  public String getParameter(String parameter) {

    if (parameter == null) {
      return parameter;
    }
    // Get Map Data
    String escapedParameterValue = "";
    String parameterValue = super.getParameter(parameter);

    // 예외 처리 문자
    List<String> listExceptWords = Arrays.asList(EXCEPTION_WORDS);

    if (listExceptWords.contains(parameter)) {
      return parameterValue;
    }

    if (parameterValue != null) {
      // Html tag 치환
      escapedParameterValue = org.springframework.web.util.HtmlUtils.htmlEscape(parameterValue);


      // 특수문자 치환
      escapedParameterValue = cleanSpecialChar(escapedParameterValue);

      // SQL injection 방지
      escapedParameterValue = cleanSQLInjection(escapedParameterValue);

      // Cross Site Scripting 방지
      escapedParameterValue = cleanXSS(escapedParameterValue);

    } else {
      return parameterValue;

    }
    return escapedParameterValue;
  }

  /**
   * @Method Name : getParameterMap
   * @Method Desc : getParameterMap 호출 시 Html Tag 치환, SQL Injection 방지, Cross Site Script 방지
   *
   * @작성일 : Jun 7, 2019
   * @작성자 : zest
   * @변경이력 : 이름 : 일자 : 근거자료 : 변경내용
   *       ------------------------------------------------------------------- zest : Jun 7, 2019 :
   *       : 변경 개발 . html tag 방지를 Spring lib로 변경
   *
   * @param value
   * @return
   */

  @Override
  public Map<String, String[]> getParameterMap() {


    Map<String, String[]> escapedParametersValuesMap = new HashMap<>();

    Map<String, String[]> valueMap = super.getParameterMap();

    for (String key : valueMap.keySet()) {
      escapedParametersValuesMap.put(key, this.getParameterValues(key));
    }


    return escapedParametersValuesMap;
  }

  /**
   * @Method Name : getParameterValues
   * @Method Desc : getParameterValues 호출 시 Html Tag 치환, SQL Injection 방지, Cross Site Script 방지
   *
   * @작성일 : Jun 7, 2019
   * @작성자 : zest
   * @변경이력 : 이름 : 일자 : 근거자료 : 변경내용
   *       ------------------------------------------------------------------- zest : Jun 7, 2019 :
   *       : 변경 개발 . html tag 방지를 Spring lib로 변경
   *
   * @param value
   * @return
   */

  @Override
  public String[] getParameterValues(String parameter) {

    String[] parametersValues = super.getParameterValues(parameter);



    if (parametersValues != null) {

      String[] escapedParameterValues = new String[parametersValues.length];


      for (int i = 0; i < parametersValues.length; i++) {
        String parameterValue = parametersValues[i];

        String escapedParameterValue = "";

        /* 암호화된 데이터는 PASS */
        if ("id".equals(parameter) || "password".equals(parameter)) {
          return parametersValues;
        }
        // Html tag 치환
        escapedParameterValue = parameterValue;


        // Cross Site Scripting 방지
        escapedParameterValue = cleanXSS(escapedParameterValue);


        escapedParameterValues[i] = escapedParameterValue;
      }


      return escapedParameterValues;
    }


    return new String[0];
  }

  /**
   * @Method Name : cleanSQLInjection
   * @Method Desc :
   * @작성일 : Jun 11, 2019
   * @작성자 : zest
   * @변경이력 : 이름 : 일자 : 근거자료 : 변경내용
   *       ------------------------------------------------------------------- zest : Jun 11, 2019 :
   *       : 신규 개발.
   *
   * @param value
   * @return
   */
  private String cleanSQLInjection(String value) {
    if (value == null) {
      return value;
    }
    String valueLowerCase = value.toLowerCase();
    for (String sqlInjectionWord : SQL_INJECTION_WORDS) {
      if (valueLowerCase.contains(sqlInjectionWord)) {
        String xSqlInjectionWord = "x-" + sqlInjectionWord;
        if (valueLowerCase.contains(xSqlInjectionWord)) {
          continue;
        }
        value = value.replaceAll("(?i)" + sqlInjectionWord, xSqlInjectionWord);
      }
    }
    return value;
  }


  /**
   * @Method Name : cleanXSS
   * @Method Desc : Cross Site Sript 방지
   * @작성일 : Jun 7, 2019
   * @작성자 : zest
   * @변경이력 : 이름 : 일자 : 근거자료 : 변경내용
   *       ------------------------------------------------------------------- zest : Jun 7, 2019 :
   *       : 변경 개발 . html tag 방지를 Spring lib로 변경
   *
   * @param value
   * @return
   */
  private String cleanXSS(String value) {
    if (value == null) {
      return null;
    }

    String valueLowerCase = value.toLowerCase();

    for (String transferXssWord : TRANSFER_XSS_WORDS) {
      if (valueLowerCase.contains(transferXssWord)) {
        String xTransferXssWord = "x-" + transferXssWord;
        if (valueLowerCase.contains(xTransferXssWord)) {
          continue;
        }
        value = value.replaceAll("(?i)" + transferXssWord, xTransferXssWord);
      }
    }

    return value;
  }


  /**
   * @Method Name : cleanSpecialChar
   * @Method Desc : 특수 문자 공백 처리
   * @작성일 : Jun 11, 2019
   * @작성자 : zest
   * @변경이력 : 이름 : 일자 : 근거자료 : 변경내용
   *       ------------------------------------------------------------------- zest : Jun 11, 2019 :
   *       : 신규 개발.
   *
   * @param value
   * @return
   */
  private String cleanSpecialChar(String value) {
    if (value == null)
      return value;

    final Pattern specialChars = Pattern.compile("['\"\\#()@;*+]");
    return specialChars.matcher(value).replaceAll("");
  }


}
