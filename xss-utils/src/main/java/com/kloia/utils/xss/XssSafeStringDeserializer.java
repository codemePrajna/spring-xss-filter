package com.kloia.utils.xss;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;

public class XssSafeStringDeserializer extends StdScalarDeserializer<String> {

    private static final long serialVersionUID = 1L;

    private static final int FEATURES_ACCEPT_ARRAYS =
            DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getMask()
                    | DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT.getMask();

    public XssSafeStringDeserializer() {
        super(String.class);
    }

    public boolean isCachable() {
        return true;
    }

    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value;
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            value = p.getText();
        } else {
            JsonToken t = p.getCurrentToken();
            if (t == JsonToken.START_ARRAY) {
                return this._deserializeFromArray(p, ctxt);
            } else if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = p.getEmbeddedObject();
                if (ob == null) {
                    value = null;
                } else if (ob instanceof byte[]) {
                    value = ctxt.getBase64Variant().encode((byte[]) ob, false);
                } else {
                    value = ob.toString();
                }
            } else {
                String text = p.getValueAsString();
                value = text != null ? text : (String) ctxt.handleUnexpectedToken(this._valueClass, p);
            }
        }
        return XssRemover.stripXSS(value);
    }

    public String deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return this.deserialize(p, ctxt);
    }

    protected String deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken t;
        if (ctxt.hasSomeOfFeatures(FEATURES_ACCEPT_ARRAYS)) {
            t = p.nextToken();
            if (t == JsonToken.END_ARRAY) {
                if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
                    return getNullValue(ctxt);
                }
            }
            if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                final String parsed = _parseString(p, ctxt);
                if (p.nextToken() != JsonToken.END_ARRAY) {
                    handleMissingEndArrayForSingle(p, ctxt);
                }
                return XssRemover.stripXSS(parsed);
            }
        } else {
            t = p.getCurrentToken();
        }
        return (String) ctxt.handleUnexpectedToken(_valueClass, t, p, null);
    }
}
