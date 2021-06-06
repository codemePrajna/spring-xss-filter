package com.kloia.utils.xss;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getRequestURI() {
        String requestURI = super.getRequestURI();
        return XssRemover.stripXSS(requestURI);
    }

    @Override
    public String getQueryString() {
        String queryString = super.getQueryString();
        return XssRemover.stripXSS(queryString);
    }

    @Override
    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        return XssRemover.stripXSS(parameter);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = XssRemover.stripXSS(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        return XssRemover.stripXSS(header);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        Enumeration<String> headers = super.getHeaders(name);
        String[] values = StringUtils.toStringArray(headers);
        List<String> strippedValues = new ArrayList<>();
        for (String value : values) {
            String strippedValue = XssRemover.stripXSS(value);
            strippedValues.add(strippedValue);
        }
        return Collections.enumeration(strippedValues);
    }

}
