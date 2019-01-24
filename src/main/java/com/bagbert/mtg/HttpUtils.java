package com.bagbert.mtg;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bagbert.commons.football.tools.StringUtils;
import com.google.apphosting.api.ApiProxy;

public class HttpUtils {

  public static boolean hasParam(HttpServletRequest req, String paramName) {
    return req.getParameterMap().containsKey(paramName);
  }

  public static int getIntParam(HttpServletRequest req, String param, int dfault) {
    String intStr = getParam(req, param);
    if (StringUtils.isEmpty(intStr)) {
      return dfault;
    }
    return Integer.parseInt(intStr);
  }

  public static String getParam(HttpServletRequest req, String paramName) {
    return getParam(req, paramName, null);
  }

  public static String getParam(HttpServletRequest req, String paramName, String defaultValue) {
    if (!hasParam(req, paramName)) {
      return defaultValue;
    }
    try {
      return StringUtils.extTrim(URLDecoder.decode(req.getParameter(paramName), "UTF-8"));
    }
    catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getApplicationId() {
    return StringUtils.substringAfterFirstToken(ApiProxy.getCurrentEnvironment().getAppId(), "~");
  }

  public static void writeResponse(Object obj, HttpServletResponse resp) throws IOException {
    if (obj == null) {
      resp.getWriter().write("Empty");
      return;
    }
    resp.getWriter().write(obj.toString());
  }
}
