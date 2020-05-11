package uk.gov.hmcts.reform.divorce.documentgenerator.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplatesConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.impl.DocmosisPDFGenerationServiceImpl;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.impl.PDFGenerationServiceImpl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DOCMOSIS_TYPE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;

@RunWith(MockitoJUnitRunner.class)
public class PDFGenerationFactoryTest {

    @Mock
    private TemplatesConfiguration templatesConfiguration;

    @Mock
    private PDFGenerationServiceImpl pdfGenerationService;

    @Mock
    private DocmosisPDFGenerationServiceImpl docmosisPDFGenerationService;

    @InjectMocks
    private PDFGenerationFactory classUnderTest;//TODO - run code analysis IntelliJ tool - next PR

    @Test
    public void shouldReturnTheRightGeneratorServiceByTemplateName() {
        when(templatesConfiguration.getGeneratorServiceNameByTemplateName("templateUsingPdfGenerator")).thenReturn(PDF_GENERATOR_TYPE);
        when(templatesConfiguration.getGeneratorServiceNameByTemplateName("templateUsingDocmosisGenerator")).thenReturn(DOCMOSIS_TYPE);

        assertThat(classUnderTest.getGeneratorService("templateUsingPdfGenerator"), is(pdfGenerationService));
        assertThat(classUnderTest.getGeneratorService("templateUsingDocmosisGenerator"), is(docmosisPDFGenerationService));
    }

}
