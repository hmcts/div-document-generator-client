package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import com.microsoft.applicationinsights.boot.dependencies.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static java.lang.String.format;
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
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COE_CORESPONDENT_SOLICITOR_CL_TEMPLATE_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COE_COVER_LETTER_FOR_RESPONDENT_FILE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COE_COVER_LETTER_FOR_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_ORDER_DOCUMENT_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COSTS_ORDER_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COST_ORDER_COVER_LETTER_FOR_CO_RESPONDENT_FILE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.COST_ORDER_COVER_LETTER_FOR_CO_RESPONDENT_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CO_RESPONDENT_INVITATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.CoE_CO_RESPONDENT_SOLICITOR_CL;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DA_GRANTED_COVER_LETTER_FOR_RESPONDENT_SOLICITOR_FILE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DA_GRANTED_COVER_LETTER_FOR_RESPONDENT_SOLICITOR_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DA_GRANTED_LETTER_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DA_GRANTED_LETTER_TEMPLATE_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_NAME_FOR_PDF_FILE;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_ANSWERS_TEMPLATE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_TEMPLATE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_GRANTED_AND_COST_ORDER_FOR_RESPONDENT_FILE_NAME;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_GRANTED_AND_COST_ORDER_FOR_RESPONDENT_TEMPLATE_ID;
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
@Slf4j
public class TemplatesConfigurationTest {

    @Rule
    public ExpectedException expectedException = none();

    @Autowired
    private TemplatesConfiguration classUnderTest;

    private static Set<Triple<String, String, String>> expectedTemplateConfigs;

    @BeforeClass
    public static void setUp() {
        expectedTemplateConfigs = Sets.newHashSet(
            new ImmutableTriple<>(AOS_INVITATION_TEMPLATE_ID,
                AOS_INVITATION_NAME_FOR_PDF_FILE, PDF_GENERATOR_TYPE),
            new ImmutableTriple<>(CO_RESPONDENT_INVITATION_TEMPLATE_ID,
                CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE, PDF_GENERATOR_TYPE),
            new ImmutableTriple<>(RESPONDENT_ANSWERS_TEMPLATE_ID,
                RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE, PDF_GENERATOR_TYPE),
            new ImmutableTriple<>(CO_RESPONDENT_ANSWERS_TEMPLATE_ID,
                CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE, PDF_GENERATOR_TYPE),
            new ImmutableTriple<>(MINI_PETITION_TEMPLATE_ID,
                MINI_PETITION_NAME_FOR_PDF_FILE, PDF_GENERATOR_TYPE),
            new ImmutableTriple<>(CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID,
                CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(COSTS_ORDER_DOCUMENT_ID,
                COSTS_ORDER_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(DN_ANSWERS_TEMPLATE_ID,
                DECREE_NISI_ANSWERS_TEMPLATE_NAME, DOCMOSIS_TYPE),
            new ImmutableTriple<>(CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID,
                CASE_LIST_FOR_PRONOUNCEMENT_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(DRAFT_MINI_PETITION_TEMPLATE_ID,
                DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE, PDF_GENERATOR_TYPE),
            new ImmutableTriple<>(DECREE_NISI_TEMPLATE_ID,
                DECREE_NISI_TEMPLATE_NAME, DOCMOSIS_TYPE),
            new ImmutableTriple<>(AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID,
                AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID,
                AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID,
                AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID,
                AOS_OFFLINE_5_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID,
                AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID,
                AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID,
                AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID,
                SOLICITOR_PERSONAL_SERVICE_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(DECREE_ABSOLUTE_TEMPLATE_ID,
                DECREE_ABSOLUTE_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID,
                DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID,
                DN_REFUSAL_ORDER_CLARIFICATION_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(DN_GRANTED_AND_COST_ORDER_FOR_RESPONDENT_TEMPLATE_ID,
                DN_GRANTED_AND_COST_ORDER_FOR_RESPONDENT_FILE_NAME, DOCMOSIS_TYPE),
            new ImmutableTriple<>(DA_GRANTED_LETTER_TEMPLATE_ID,
                DA_GRANTED_LETTER_TEMPLATE_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE),
            new ImmutableTriple<>(DA_GRANTED_COVER_LETTER_FOR_RESPONDENT_SOLICITOR_TEMPLATE_ID,
                DA_GRANTED_COVER_LETTER_FOR_RESPONDENT_SOLICITOR_FILE_NAME, DOCMOSIS_TYPE),
            new ImmutableTriple<>(COE_COVER_LETTER_FOR_RESPONDENT_TEMPLATE_ID,
                COE_COVER_LETTER_FOR_RESPONDENT_FILE_NAME, DOCMOSIS_TYPE),
            new ImmutableTriple<>(COST_ORDER_COVER_LETTER_FOR_CO_RESPONDENT_TEMPLATE_ID,
                COST_ORDER_COVER_LETTER_FOR_CO_RESPONDENT_FILE_NAME, DOCMOSIS_TYPE),
            new ImmutableTriple<>(CoE_CO_RESPONDENT_SOLICITOR_CL,
                COE_CORESPONDENT_SOLICITOR_CL_TEMPLATE_NAME_FOR_PDF_FILE, DOCMOSIS_TYPE)
        );
    }

    @Test
    public void shouldRetrieveFileName_AndPdfGeneratorName_ByTemplateName() {
        expectedTemplateConfigs.forEach(
            expectedTemplateConfig -> {
                String templateName = expectedTemplateConfig.getLeft();
                String fileName = expectedTemplateConfig.getMiddle();
                String fileGenerator = expectedTemplateConfig.getRight();

                System.out.printf("Testing template %s has file name %s%n", templateName, fileName);
                assertThat(format("Template %s should have file name \"%s\"", templateName, fileName),
                    classUnderTest.getFileNameByTemplateName(templateName), is(fileName));

                System.out.printf("Testing template %s has file generator %s%n", templateName, fileGenerator);
                assertThat(format("Template %s should have file generator \"%s\"", templateName, fileGenerator),
                    classUnderTest.getGeneratorServiceNameByTemplateName(templateName), is(fileGenerator));
            }
        );
    }

    @Test
    public void shouldThrowExceptionWhenUnknownTemplateIsRequested() {
        String templateName = "unknown-template";
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unknown template: " + templateName);

        classUnderTest.getFileNameByTemplateName(templateName);
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
