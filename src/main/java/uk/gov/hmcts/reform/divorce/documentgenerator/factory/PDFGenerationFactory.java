package uk.gov.hmcts.reform.divorce.documentgenerator.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID;

@Component
public class PDFGenerationFactory {

    private Map<String, PDFGenerationService> generatorMap;
    private PDFGenerationService pdfGenerationService;
    private PDFGenerationService docmosisPdfGenerationService;

    @Autowired
    public PDFGenerationFactory(@Qualifier("pdfGenerator") PDFGenerationService pdfGenerationService,
                                @Qualifier("docmosisPdfGenerator") PDFGenerationService docmosisPdfGenerationService) {
        this.pdfGenerationService = pdfGenerationService;
        this.docmosisPdfGenerationService = docmosisPdfGenerationService;

        this.generatorMap = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>(CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID, docmosisPdfGenerationService)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public PDFGenerationService getGeneratorService(String templateId) {
        return generatorMap.getOrDefault(templateId, pdfGenerationService);
    }
}
