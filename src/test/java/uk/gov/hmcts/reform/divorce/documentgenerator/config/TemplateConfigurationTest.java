package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_ORDER_DOCUMENT_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_ORDER_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_ANSWERS_TEMPLATE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DRAFT_MINI_PETITION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MINI_PETITION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.MINI_PETITION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateConfigurationTest {

    private static final String AOS_INVITATION_NAME_FOR_PDF_FILE = "AOSInvitation.pdf";
    private static final String CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE = "CoRespondentInvitation.pdf";
    private static final String RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "RespondentAnswers.pdf";
    private static final String CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "CoRespondentAnswers.pdf";
    private static final String DECREE_NISI_NAME_FOR_PDF_FILE = "DecreeNisiPronouncement.pdf";
    private static final String CASE_LIST_FOR_PRONOUNCEMENT_PDF_FILE = "CaseListForPronouncement.pdf";
    private static final String DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE = "DraftDivorcePetition.pdf";//TODO - should these be here?

    private static final String DECREE_NISI_TEMPLATE = "FL-DIV-GNO-ENG-00021.docx";
    private static final String SOLICITOR_PERSONAL_SERVICE_FILE_NAME = "SolicitorPersonalService.pdf";

    @Autowired
    private TemplateConfiguration templateConfiguration;

    @Test
    public void shouldRetrieveFileNameByTemplateName() {
        //TODO - what about non-existent ones? replicate the existing behaviour
        assertThat(templateConfiguration.getFileNameByTemplateName(AOS_INVITATION_TEMPLATE_ID), is(AOS_INVITATION_NAME_FOR_PDF_FILE));
        assertThat(templateConfiguration.getFileNameByTemplateName(CO_RESPONDENT_INVITATION_TEMPLATE_ID), is(CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE));
        assertThat(templateConfiguration.getFileNameByTemplateName(RESPONDENT_ANSWERS_TEMPLATE_ID), is(RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE));
        assertThat(templateConfiguration.getFileNameByTemplateName(CO_RESPONDENT_ANSWERS_TEMPLATE_ID), is(CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE));
        assertThat(templateConfiguration.getFileNameByTemplateName(MINI_PETITION_TEMPLATE_ID), is(MINI_PETITION_NAME_FOR_PDF_FILE));
        assertThat(templateConfiguration.getFileNameByTemplateName(CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID), is(CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE));
        assertThat(templateConfiguration.getFileNameByTemplateName(COSTS_ORDER_DOCUMENT_ID), is(COSTS_ORDER_NAME_FOR_PDF_FILE));

//        assertThat(templateConfiguration.getFileNameByTemplateName(DN_ANSWERS_TEMPLATE_ID), is(DECREE_NISI_ANSWERS_TEMPLATE_NAME));
//        assertThat(templateConfiguration.getFileNameByTemplateName(CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID), is(CASE_LIST_FOR_PRONOUNCEMENT_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(DRAFT_MINI_PETITION_TEMPLATE_ID), is(DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(DECREE_NISI_TEMPLATE), is(DECREE_NISI_NAME_FOR_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID), is(AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_NAME_FOR_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID), is(AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_NAME_FOR_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID), is(AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID), is(AOS_OFFLINE_5_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID), is(AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID), is(AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID), is(AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_FILE));
//        assertThat(templateConfiguration.getFileNameByTemplateName(SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID), is(SOLICITOR_PERSONAL_SERVICE_FILE_NAME));
    }

    @Test
    public void shouldReturnTheRightPdfGeneratorNameByTemplateId() {
        //TODO - could this be covered by the factory test?
        //TODO - could this and the factory be the same thing? Probably not, it's not a factory's job to retrieve file names - but it could be a TemplateConfiguration's job to return a PDFGenerator?? Maybe.
    }

}
