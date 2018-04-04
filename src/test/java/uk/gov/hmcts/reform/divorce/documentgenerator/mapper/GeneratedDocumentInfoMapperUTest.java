package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;

public class GeneratedDocumentInfoMapperUTest {

    @Test
    public void testConstructorPrivate() throws Exception {
        Constructor<GeneratedDocumentInfoMapper> constructor =
                GeneratedDocumentInfoMapper.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void givenFileUploadResponseIsNull_whenMapToGeneratedDocumentInfo_thenReturnNull() {
        assertNull(GeneratedDocumentInfoMapper.mapToGeneratedDocumentInfo(null));
    }

    @Test
    public void givenFileUploadResponseIsNotNull_whenMapToGeneratedDocumentInfo_thenReturnProperResponse() {
        final String fileUrl = "url";
        final String mimeType = "mimeType";
        final String createdBy = "createBy";
        final String createdOn = "createdOn";

        final FileUploadResponse fileUploadResponse = new FileUploadResponse(HttpStatus.OK);
        fileUploadResponse.setFileUrl(fileUrl);
        fileUploadResponse.setMimeType(mimeType);
        fileUploadResponse.setCreatedBy(createdBy);
        fileUploadResponse.setCreatedOn(createdOn);

        GeneratedDocumentInfo actual = GeneratedDocumentInfoMapper.mapToGeneratedDocumentInfo(fileUploadResponse);

        assertEquals(fileUrl, actual.getUrl());
        assertEquals(mimeType, actual.getMimeType());
        assertEquals(createdOn, actual.getCreatedOn());
    }
}
