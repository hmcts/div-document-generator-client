package uk.gov.hmcts.reform.divorce.documentgenerator.exception;

public class PDFGenerationException extends RuntimeException {

    public PDFGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
