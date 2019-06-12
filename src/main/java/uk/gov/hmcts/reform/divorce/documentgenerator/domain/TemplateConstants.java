package uk.gov.hmcts.reform.divorce.documentgenerator.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateConstants {
    // Generators
    public static final String DOCMOSIS_TYPE = "docmosis";
    public static final String PDF_GENERATOR_TYPE = "pdfgenerator";

    // Template Ids
    public static final String AOS_INVITATION_TEMPLATE_ID = "aosinvitation";
    public static final String CERTIFICATE_OF_ENTITLEMENT_TEMPLATE_ID = "FL-DIV-GNO-ENG-00020.docx";
    public static final String CO_RESPONDENT_ANSWERS_TEMPLATE_ID = "co-respondent-answers";
    public static final String CO_RESPONDENT_INVITATION_TEMPLATE_ID = "co-respondentinvitation";
    public static final String MINI_PETITION_TEMPLATE_ID = "divorceminipetition";
    public static final String RESPONDENT_ANSWERS_TEMPLATE_ID = "respondentAnswers";

    // Template Names
    public static final String AOS_INVITATION_NAME_FOR_PDF_FILE = "AOSInvitation.pdf";
    public static final String CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE = "CertificateOfEntitlement.pdf";
    public static final String CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "CoRespondentAnswers.pdf";
    public static final String CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE = "CoRespondentInvitation.pdf";
    public static final String MINI_PETITION_NAME_FOR_PDF_FILE = "DivorcePetition.pdf";
    public static final String RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "RespondentAnswers.pdf";

    // Template Data Mapper Constants
    public static final String CARE_OF_PREFIX = "c\\o";
    public static final String CASE_DATA = "case_data";
    public static final String CASE_DETAILS = "caseDetails";
    public static final String CCD_DATE_FORMAT = "yyyy-MM-dd";
    public static final String CO_RESPONDENT_WISH_TO_NAME = "D8ReasonForDivorceAdulteryWishToName";
    public static final String COURT_CONTACT_KEY = "CourtContactDetails";
    public static final String COURT_HEARING_DATE_KEY = "DateOfHearing";
    public static final String COURT_HEARING_JSON_KEY = "DateAndTimeOfHearing";
    public static final String COURT_HEARING_TIME_KEY = "TimeOfHearing";
    public static final String COURT_NAME_KEY = "CourtName";
    public static final String DN_APPROVAL_DATE_KEY = "DNApprovalDate";
    public static final String LETTER_DATE_FORMAT = "dd MMMM yyyy";
    public static final String NEWLINE_DELIMITER = "\n";
    public static final String SERVICE_CENTRE_COURT_NAME = "Courts and Tribunals Service Centre";
    public static final String SERVICE_CENTRE_COURT_CONTACT_DETAILS = "c\\o East Midlands Regional Divorce"
        + " Centre\nPO Box 10447\nNottingham\nNG2 9QN\nEmail: contactdivorce@justice.gov.uk\nPhone: 0300 303"
        + " 0642 (from 8.30am to 5pm)";
    public static final String SOLICITOR_IS_NAMED_CO_RESPONDENT = "D8ReasonForDivorceAdulteryIsNamed";
    public static final String SPACE_DELIMITER = " ";
}
