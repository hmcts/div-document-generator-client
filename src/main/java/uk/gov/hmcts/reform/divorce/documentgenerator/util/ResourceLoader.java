package uk.gov.hmcts.reform.divorce.documentgenerator.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.ErrorLoadingTemplateException;

import java.io.InputStream;

@SuppressWarnings("squid:S1118")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceLoader {

    public static byte[] loadResource(String path) {
        NullOrEmptyValidator.requireNonBlank(path);

        try (InputStream inputStream = ResourceLoader.class.getClassLoader().getResourceAsStream(path)) {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            throw new ErrorLoadingTemplateException(String.format("Couldn't load template with the name %s", path), e);
        }
    }
}
