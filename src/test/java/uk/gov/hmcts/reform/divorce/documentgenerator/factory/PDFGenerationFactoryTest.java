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
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_ORDER_DOCUMENT_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DRAFT_MINI_PETITION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MINI_PETITION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID;

@RunWith(SpringRunner.class)//TODO - we might be able to move this to the TemplateConfigurationTest
@SpringBootTest
public class PDFGenerationFactoryTest {

    @Autowired
    private PDFGenerationFactory classUnderTest;

    @Test
    public void shouldReturnTheRightGeneratorServiceByTemplateName() {
        assertThat(classUnderTest.getGeneratorService(AOS_INVITATION_TEMPLATE_ID), is(instanceOf(PDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(CO_RESPONDENT_INVITATION_TEMPLATE_ID), is(instanceOf(PDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(RESPONDENT_ANSWERS_TEMPLATE_ID), is(instanceOf(PDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(CO_RESPONDENT_ANSWERS_TEMPLATE_ID), is(instanceOf(PDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(MINI_PETITION_TEMPLATE_ID), is(instanceOf(PDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(DRAFT_MINI_PETITION_TEMPLATE_ID), is(instanceOf(PDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(COSTS_ORDER_DOCUMENT_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(DN_ANSWERS_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(DECREE_NISI_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(DECREE_ABSOLUTE_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
        assertThat(classUnderTest.getGeneratorService(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID), is(instanceOf(DocmosisPDFGenerationServiceImpl.class)));
    }

    @Test
    public void shouldReturnDefaultGeneratorForNonExistentTemplate() {
        //TODO - think about consequences of making docmosis the default one - if we come to it
        assertThat(classUnderTest.getGeneratorService("non-existent-template"), is(instanceOf(PDFGenerationServiceImpl.class)));
    }

}
