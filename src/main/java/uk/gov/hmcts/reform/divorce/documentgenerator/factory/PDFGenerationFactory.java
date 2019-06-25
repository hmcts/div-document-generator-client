package uk.gov.hmcts.reform.divorce.documentgenerator.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DOCMOSIS_TYPE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;

@Component
public class PDFGenerationFactory {

    private TemplateConfiguration templateConfiguration;
    private Map<String, PDFGenerationService> generatorMap;

    @Autowired
    public PDFGenerationFactory(TemplateConfiguration templateConfiguration,
                                @Qualifier("pdfGenerator") PDFGenerationService pdfGenerationService,
                                @Qualifier("docmosisPdfGenerator") PDFGenerationService docmosisPdfGenerationService) {
        this.templateConfiguration = templateConfiguration;

        // Setup generator type mapping against expected template map values
        this.generatorMap = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>(PDF_GENERATOR_TYPE, pdfGenerationService),
            new AbstractMap.SimpleImmutableEntry<>(DOCMOSIS_TYPE, docmosisPdfGenerationService)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public PDFGenerationService getGeneratorService(String templateId) {
        return generatorMap.get(
            getGeneratorType(templateId)
        );
    }

    public String getGeneratorType(String templateId) {
        return templateConfiguration.getMap().getOrDefault(templateId, PDF_GENERATOR_TYPE);
    }
}
