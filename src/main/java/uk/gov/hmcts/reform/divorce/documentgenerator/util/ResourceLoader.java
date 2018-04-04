package uk.gov.hmcts.reform.divorce.documentgenerator.util;

import org.apache.commons.io.IOUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.ErrorLoadingTemplateException;

@SuppressWarnings("squid:S1118")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceLoader {

    public static byte[] loadResource(String path) {
        NullOrEmptyValidator.requireNonBlank(path);

        try {
            return IOUtils.toByteArray(ResourceLoader.class.getClassLoader().getResourceAsStream(path));
        } catch (Exception e) {
            throw new ErrorLoadingTemplateException(String.format("Couldn't load template with the name %s", path), e);
        }
    }
}
