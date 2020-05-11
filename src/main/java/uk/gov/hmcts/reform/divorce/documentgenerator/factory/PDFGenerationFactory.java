package uk.gov.hmcts.reform.divorce.documentgenerator.factory;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplatesConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.impl.DocmosisPDFGenerationServiceImpl;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.impl.PDFGenerationServiceImpl;

import java.util.Map;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DOCMOSIS_TYPE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;

@Component
public class PDFGenerationFactory {

    private TemplatesConfiguration templatesConfiguration;
    private Map<String, PDFGenerationService> generatorMap;

    @Autowired
    public PDFGenerationFactory(TemplatesConfiguration templatesConfiguration,
                                PDFGenerationServiceImpl pdfGenerationService,
                                DocmosisPDFGenerationServiceImpl docmosisPdfGenerationService) {//TODO - should these be in a constructor? - do it last, if at all - next PR
        this.templatesConfiguration = templatesConfiguration;

        // Setup generator type mapping against expected template map values
        this.generatorMap = ImmutableMap.of(
            PDF_GENERATOR_TYPE, pdfGenerationService,
            DOCMOSIS_TYPE, docmosisPdfGenerationService
        );
    }

    public PDFGenerationService getGeneratorService(String templateId) {
        String generatorServiceName = templatesConfiguration.getGeneratorServiceNameByTemplateName(templateId);
        return generatorMap.get(generatorServiceName);
    }

}
