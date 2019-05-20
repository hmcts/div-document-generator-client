package uk.gov.hmcts.reform.divorce.documentgenerator.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateConstants {
    // Template Ids
    public static final String AOS_INVITATION_TEMPLATE_ID = "aosinvitation";
    public static final String CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID = "FL-DIV-GNO-ENG-00020.docx";
    public static final String CO_RESPONDENT_ANSWERS_TEMPLATE_ID = "co-respondent-answers";
    public static final String CO_RESPONDENT_INVITATION_TEMPLATE_ID = "co-respondentinvitation";
    public static final String DECREE_NISI_TEMPLATE_ID = "FL-DIV-GNO-ENG-00021.docx";
    public static final String MINI_PETITION_TEMPLATE_ID = "divorceminipetition";
    public static final String RESPONDENT_ANSWERS_TEMPLATE_ID = "respondentAnswers";

    // Template Names
    public static final String AOS_INVITATION_NAME_FOR_PDF_FILE = "AOSInvitation.pdf";
    public static final String CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE = "CertificateOfEntitlement.pdf";
    public static final String CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "CoRespondentAnswers.pdf";
    public static final String CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE = "CoRespondentInvitation.pdf";
    public static final String DECREE_NISI_NAME_FOR_PDF_FILE = "DecreeNisi.pdf";
    public static final String MINI_PETITION_NAME_FOR_PDF_FILE = "DivorcePetition.pdf";
    public static final String RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "RespondentAnswers.pdf";
}
