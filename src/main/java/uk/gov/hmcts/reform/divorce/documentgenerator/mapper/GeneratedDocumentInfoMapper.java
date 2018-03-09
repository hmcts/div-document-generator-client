package uk.gov.hmcts.reform.divorce.documentgenerator.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;

@SuppressWarnings("squid:S1118")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GeneratedDocumentInfoMapper {

    public static GeneratedDocumentInfo mapToGeneratedDocumentInfo(FileUploadResponse fileUploadResponse) {
        if (fileUploadResponse == null) {
            return null;
        }

        GeneratedDocumentInfo generatedDocumentInfo = new GeneratedDocumentInfo();
        generatedDocumentInfo.setUrl(fileUploadResponse.getFileUrl());
        generatedDocumentInfo.setMimeType(fileUploadResponse.getMimeType());
        generatedDocumentInfo.setCreatedOn(fileUploadResponse.getCreatedOn());

        return generatedDocumentInfo;
    }
}