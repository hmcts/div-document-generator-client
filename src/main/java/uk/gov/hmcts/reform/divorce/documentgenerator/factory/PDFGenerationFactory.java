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
                                @Qualifier("docmosisPdfGenerator") PDFGenerationService docmosisPdfGenerationService) {//TODO - should these be in a constructor? - do it last, if at all
        //TODO - second change
        this.templateConfiguration = templateConfiguration;

        // Setup generator type mapping against expected template map values
        this.generatorMap = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>(PDF_GENERATOR_TYPE, pdfGenerationService),
            new AbstractMap.SimpleImmutableEntry<>(DOCMOSIS_TYPE, docmosisPdfGenerationService)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public PDFGenerationService getGeneratorService(String templateId) {
        //TODO - we could even remove all of the docmosis template
        return generatorMap.get(
            getGeneratorType(templateId)
        );
    }

    private String getGeneratorType(String templateId) {
        //TODO - preparation for refactoring - what happens if I pass an unexisting docmosis template as a parameter - do we want to protect against this?
        return templateConfiguration.getMap().getOrDefault(templateId, PDF_GENERATOR_TYPE);//TODO - THIS SHOULD BE THE VERY FIRST THING - careful changing this. we might have templates that are not listed here but still being passed by clients
    }//TODO - we probably got to a point where Docmosis is the default

}
