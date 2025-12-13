package com.example.twofa.fixture;

/**
 * Predefined SOAP response templates for testing.
 */
public class SoapResponseFixtures {

    public static final String SUCCESS_RESPONSE = """
        <?xml version="1.0" encoding="UTF-8"?>
        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <evaluateRiskResponse>
                    <result>SUCCESS</result>
                    <message>Authentication successful</message>
                </evaluateRiskResponse>
            </soap:Body>
        </soap:Envelope>
        """;

    public static final String FAILURE_RESPONSE = """
        <?xml version="1.0" encoding="UTF-8"?>
        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <evaluateRiskResponse>
                    <result>FAILED</result>
                    <message>Authentication failed</message>
                </evaluateRiskResponse>
            </soap:Body>
        </soap:Envelope>
        """;

    public static final String FAULT_RESPONSE = """
        <?xml version="1.0" encoding="UTF-8"?>
        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            <soap:Body>
                <soap:Fault>
                    <faultcode>soap:Server</faultcode>
                    <faultstring>Internal server error</faultstring>
                </soap:Fault>
            </soap:Body>
        </soap:Envelope>
        """;
}

