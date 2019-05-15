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

@Component
public class PDFGenerationFactory {

    private static final String DOCMOSIS_TYPE = "docmosis";
    private static final String PDF_GENERATOR_TYPE = "pdfgenerator";

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
            templateConfiguration.getMap().getOrDefault(templateId, PDF_GENERATOR_TYPE)
        );
    }
}
