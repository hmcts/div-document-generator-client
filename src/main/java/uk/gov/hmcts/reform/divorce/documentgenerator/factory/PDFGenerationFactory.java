package uk.gov.hmcts.reform.divorce.documentgenerator.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplatesConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DOCMOSIS_TYPE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;

@Component
public class PDFGenerationFactory {

    private TemplatesConfiguration templatesConfiguration;
    private Map<String, PDFGenerationService> generatorMap;

    @Autowired
    public PDFGenerationFactory(TemplatesConfiguration templatesConfiguration,
                                @Qualifier("pdfGenerator") PDFGenerationService pdfGenerationService,
                                @Qualifier("docmosisPdfGenerator") PDFGenerationService docmosisPdfGenerationService) {//TODO - should these be in a constructor? - do it last, if at all
        this.templatesConfiguration = templatesConfiguration;

        // Setup generator type mapping against expected template map values
        this.generatorMap = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>(PDF_GENERATOR_TYPE, pdfGenerationService),
            new AbstractMap.SimpleImmutableEntry<>(DOCMOSIS_TYPE, docmosisPdfGenerationService)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));//TODO - refactor
    }

    public PDFGenerationService getGeneratorService(String templateId) {
        String generatorServiceName = templatesConfiguration.getGeneratorServiceNameByTemplateName(templateId);
        return generatorMap.get(generatorServiceName);
    }

}
