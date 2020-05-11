package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_INVITATION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_5_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_LIST_FOR_PRONOUNCEMENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_ORDER_DOCUMENT_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_ORDER_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_ANSWERS_TEMPLATE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_TEMPLATE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_CLARIFICATION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DOCMOSIS_TYPE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DRAFT_MINI_PETITION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MINI_PETITION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MINI_PETITION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_PERSONAL_SERVICE_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplatesConfigurationTest {

    @Rule
    public ExpectedException expectedException = none();

    @Autowired
    private TemplatesConfiguration classUnderTest;

    @Test
    public void shouldRetrieveFileNameByTemplateName() {
        assertThat(classUnderTest.getFileNameByTemplateName(AOS_INVITATION_TEMPLATE_ID),
            is(AOS_INVITATION_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(CO_RESPONDENT_INVITATION_TEMPLATE_ID),
            is(CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(RESPONDENT_ANSWERS_TEMPLATE_ID),
            is(RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(CO_RESPONDENT_ANSWERS_TEMPLATE_ID),
            is(CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(MINI_PETITION_TEMPLATE_ID),
            is(MINI_PETITION_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID),
            is(CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(COSTS_ORDER_DOCUMENT_ID),
            is(COSTS_ORDER_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(DN_ANSWERS_TEMPLATE_ID),
            is(DECREE_NISI_ANSWERS_TEMPLATE_NAME));
        assertThat(classUnderTest.getFileNameByTemplateName(CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID),
            is(CASE_LIST_FOR_PRONOUNCEMENT_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(DRAFT_MINI_PETITION_TEMPLATE_ID),
            is(DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(DECREE_NISI_TEMPLATE_ID),
            is(DECREE_NISI_TEMPLATE_NAME));
        assertThat(classUnderTest.getFileNameByTemplateName(AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID),
            is(AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID),
            is(AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID),
            is(AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID),
            is(AOS_OFFLINE_5_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID),
            is(AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID),
            is(AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID),
            is(AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID),
            is(SOLICITOR_PERSONAL_SERVICE_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(DECREE_ABSOLUTE_TEMPLATE_ID),
            is(DECREE_ABSOLUTE_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID),
            is(DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_FILE));
        assertThat(classUnderTest.getFileNameByTemplateName(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID),
            is(DN_REFUSAL_ORDER_CLARIFICATION_NAME_FOR_PDF_FILE));
    }

    @Test
    public void shouldThrowExceptionWhenUnknownTemplateIsRequested() {
        String templateName = "unknown-template";
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unknown template: " + templateName);

        classUnderTest.getFileNameByTemplateName(templateName);
    }

    @Test
    public void shouldReturnTheRightPdfGeneratorNameByTemplateId() {
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(AOS_INVITATION_TEMPLATE_ID), is(PDF_GENERATOR_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(CO_RESPONDENT_INVITATION_TEMPLATE_ID), is(PDF_GENERATOR_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(RESPONDENT_ANSWERS_TEMPLATE_ID), is(PDF_GENERATOR_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(CO_RESPONDENT_ANSWERS_TEMPLATE_ID), is(PDF_GENERATOR_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(MINI_PETITION_TEMPLATE_ID), is(PDF_GENERATOR_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(DRAFT_MINI_PETITION_TEMPLATE_ID), is(PDF_GENERATOR_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(COSTS_ORDER_DOCUMENT_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(DN_ANSWERS_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(DECREE_NISI_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(DECREE_ABSOLUTE_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID), is(DOCMOSIS_TYPE));
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID), is(DOCMOSIS_TYPE));
    }

    @Test
    public void shouldReturnDefaultGeneratorForNonExistentTemplate() {
        assertThat(classUnderTest.getGeneratorServiceNameByTemplateName("non-existent-template"), is(PDF_GENERATOR_TYPE));
    }

    @Test
    public void shouldThrowAnExceptionWhenTemplateNameIsDuplicated() {
        TemplatesConfiguration templatesConfiguration = new TemplatesConfiguration();
        templatesConfiguration.setConfigurationList(asList(
            new TemplateConfiguration("thisName", null, null),
            new TemplateConfiguration("thisName", null, null)
        ));

        expectedException.expect(Exception.class);

        templatesConfiguration.init();
    }

}
