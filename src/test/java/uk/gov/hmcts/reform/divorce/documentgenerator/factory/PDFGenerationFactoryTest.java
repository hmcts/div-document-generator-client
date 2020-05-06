package uk.gov.hmcts.reform.divorce.documentgenerator.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.impl.DocmosisPDFGenerationServiceImpl;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.impl.PDFGenerationServiceImpl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)//TODO - we might be able to move this to the TemplateConfigurationTest
@SpringBootTest
public class PDFGenerationFactoryTest {

    //TODO - possibly some of the logic that should be here will be in DocumentManagementServiceImplUTest :(

    @Autowired
    private PDFGenerationFactory classUnderTest;

    @Test
    public void shouldReturnTheRightGeneratorServiceByTemplateName() {
        //TODO - maybe add all cases in the other test? this will be a configuration test... - I think it can't hurt to over test. Let's do it
        assertThat(classUnderTest.getGeneratorService("aosinvitation"), is(instanceOf(PDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService("FL-DIV-GNO-ENG-00073.docx"), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
    }

    @Test
    public void shouldReturnDefaultGeneratorForNonExistentTemplate() {
        //TODO - think about consequences of making docmosis the default one - if we come to it
        assertThat(classUnderTest.getGeneratorService("non-existent-template"), is(instanceOf(PDFGenerationServiceImpl.class)));
    }

    //TODO - look into unit test warnings - do it last
}
