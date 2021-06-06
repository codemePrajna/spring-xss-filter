package com.kloia.utils.xss;

import lombok.extern.slf4j.Slf4j;
import org.owasp.esapi.ESAPI;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.regex.Pattern;

@Slf4j
public class XssRemover {

    public static final String XSS_REMOVER_CACHE = "XssRemover_Cache_";
    private static final Pattern SCRIPT_CONTENT = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SRC_CONTENT = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern SRC_CONTENT_2 = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern SCRIPT_TAG_CLOSE = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_TAG = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern EVAL_EXPRESSION = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern EXPRESSION_EXPRESSION = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern JAVASCRIPT_EXPRESSION = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
    private static final Pattern VBSCRIPT_EXPRESSION = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
    private static final Pattern ONLOAD_EXPRESSION = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public static String stripXSS(String value) {
        String originalValue = value;

        if (value == null) {
            return null;
        }

        Object cachedValue = getCachedValue(value);
        if (cachedValue instanceof String) {
            return (String) cachedValue;
        } else if (cachedValue instanceof Exception) {
            // Exception is Already thrown
            return originalValue;
        }

        String inputValue;
        try {
            value = ESAPI.encoder().canonicalize(value);

            // Avoid null characters
            value = value.replaceAll("", "");

            inputValue = value;

            // Avoid anything between script tags
            value = SCRIPT_CONTENT.matcher(value).replaceAll("");

            // Avoid anything in a src='...' type of expression
            value = SRC_CONTENT.matcher(value).replaceAll("");
            value = SRC_CONTENT_2.matcher(value).replaceAll("");

            // Remove any lonesome </script> tag
            value = SCRIPT_TAG_CLOSE.matcher(value).replaceAll("");

            // Remove any lonesome <script ...> tag
            value = SCRIPT_TAG.matcher(value).replaceAll("");

            // Avoid eval(...) expressions
            value = EVAL_EXPRESSION.matcher(value).replaceAll("");

            // Avoid expression(...) expressions
            value = EXPRESSION_EXPRESSION.matcher(value).replaceAll("");

            // Avoid javascript:... expressions
            value = JAVASCRIPT_EXPRESSION.matcher(value).replaceAll("");

            // Avoid vbscript:... expressions
            value = VBSCRIPT_EXPRESSION.matcher(value).replaceAll("");

            // Avoid onload= expressions
            value = ONLOAD_EXPRESSION.matcher(value).replaceAll("");

        } catch (Exception e) {
            cacheValue(originalValue, e);
            throw e;
        }

        if (!inputValue.equals(value)) {
            cacheValue(originalValue, value);
            log.warn("Potential XSS attack prevented!! input: {}, modified to: {}", inputValue, value);
            return value;
        } else {
            cacheValue(originalValue, originalValue);
            return originalValue;
        }

    }

    private static void cacheValue(String originalValue, Object result) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            requestAttributes.setAttribute(XSS_REMOVER_CACHE + originalValue, result, RequestAttributes.SCOPE_REQUEST);
        }

    }

    private static Object getCachedValue(String value) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getAttribute(XSS_REMOVER_CACHE + value, RequestAttributes.SCOPE_REQUEST);
    }
}
