package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.stub;

public interface DocumentDownloadService {
    byte[] getDocument(String fileName);
}
