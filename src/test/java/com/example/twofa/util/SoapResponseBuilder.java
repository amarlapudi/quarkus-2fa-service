package com.example.twofa.util;

/**
 * Builder for constructing SOAP responses for testing.
 */
public class SoapResponseBuilder {

    private static final String SOAP_ENVELOPE_START = """
        <?xml version="1.0" encoding="UTF-8"?>
        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
        """;

    private static final String SOAP_ENVELOPE_END = """
            </soap:Body>
        </soap:Envelope>
        """;

    /**
     * Builds a successful SOAP response.
     */
    public static String buildSuccessResponse(String message) {
        return SOAP_ENVELOPE_START +
            "<evaluateRiskResponse>" +
            "<result>SUCCESS</result>" +
            "<message>" + message + "</message>" +
            "</evaluateRiskResponse>" +
            SOAP_ENVELOPE_END;
    }

    /**
     * Builds a failed SOAP response.
     */
    public static String buildFailureResponse(String message) {
        return SOAP_ENVELOPE_START +
            "<evaluateRiskResponse>" +
            "<result>FAILED</result>" +
            "<message>" + message + "</message>" +
            "</evaluateRiskResponse>" +
            SOAP_ENVELOPE_END;
    }

    /**
     * Builds a SOAP fault response.
     */
    public static String buildFaultResponse(String faultCode, String faultString) {
        return SOAP_ENVELOPE_START +
            "<soap:Fault>" +
            "<faultcode>" + faultCode + "</faultcode>" +
            "<faultstring>" + faultString + "</faultstring>" +
            "</soap:Fault>" +
            SOAP_ENVELOPE_END;
    }

    /**
     * Builds a malformed SOAP response for error testing.
     */
    public static String buildMalformedResponse() {
        return "<invalid>xml</invalid>";
    }
}

