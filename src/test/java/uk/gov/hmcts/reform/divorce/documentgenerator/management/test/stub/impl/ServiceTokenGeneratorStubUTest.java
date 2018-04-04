package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.stub.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

public class ServiceTokenGeneratorStubUTest {

    private static final String DUMMY_SERVICE_TOKEN = "Dummy Token";

    private final AuthTokenGenerator classUnderTest = new ServiceTokenGeneratorStub();

    @Test
    public void whenGenerate_thenReturnDummyToken() {
        assertEquals(DUMMY_SERVICE_TOKEN, classUnderTest.generate());
    }
}
