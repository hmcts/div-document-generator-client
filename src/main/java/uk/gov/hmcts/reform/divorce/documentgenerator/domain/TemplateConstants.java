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
    public static final String DECREE_NISI_TEMPLATE_ID = "FL-DIV-GNO-ENG-00021.docx";
    public static final String MINI_PETITION_TEMPLATE_ID = "divorceminipetition";
    public static final String DRAFT_MINI_PETITION_TEMPLATE_ID = "divorcedraftminipetition";
    public static final String RESPONDENT_ANSWERS_TEMPLATE_ID = "respondentAnswers";
    public static final String DN_ANSWERS_TEMPLATE_ID = "FL-DIV-GNO-ENG-00022.docx";
    public static final String COSTS_ORDER_DOCUMENT_ID = "FL-DIV-DEC-ENG-00060.docx";
    public static final String CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID = "FL-DIV-GNO-ENG-00059.docx";
    public static final String DECREE_ABSOLUTE_TEMPLATE_ID = "FL-DIV-GOR-ENG-00062.docx";
    public static final String AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID = "FL-DIV-APP-ENG-00080.docx";
    public static final String AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID = "FL-DIV-APP-ENG-00081.docx";
    public static final String AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID = "FL-DIV-APP-ENG-00083.docx";
    public static final String AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID = "FL-DIV-APP-ENG-00084.docx";
    public static final String AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID = "FL-DIV-APP-ENG-00082.docx";
    public static final String AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID = "FL-DIV-LET-ENG-00075.doc";
    public static final String AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID = "FL-DIV-LET-ENG-00076.doc";
    public static final String SOLICITOR_PERSONAL_SERVICE_TEMPLATE_ID = "FL-DIV-GNO-ENG-00073.docx";
    public static final String DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID = "FL-DIV-DEC-ENG-00088.docx";
    public static final String DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID = "FL-DIV-DEC-ENG-00098.docx";

    // Template Names
    public static final String AOS_INVITATION_NAME_FOR_PDF_FILE = "AOSInvitation.pdf";
    public static final String CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE = "CertificateOfEntitlement.pdf";
    public static final String CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "CoRespondentAnswers.pdf";
    public static final String CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE = "CoRespondentInvitation.pdf";
    public static final String DECREE_NISI_TEMPLATE_NAME = "DecreeNisiPronouncement.pdf";
    public static final String DECREE_NISI_ANSWERS_TEMPLATE_NAME = "DecreeNisiAnswers.pdf";
    public static final String MINI_PETITION_NAME_FOR_PDF_FILE = "DivorcePetition.pdf";
    public static final String DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE = "DraftDivorcePetition.pdf";
    public static final String RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "RespondentAnswers.pdf";
    public static final String COSTS_ORDER_NAME_FOR_PDF_FILE = "CostsOrder.pdf";
    public static final String CASE_LIST_FOR_PRONOUNCEMENT_NAME_FOR_PDF_FILE = "CaseListForPronouncement.pdf";
    public static final String DECREE_ABSOLUTE_NAME_FOR_PDF_FILE = "DecreeAbsolute.pdf";
    public static final String AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE =
        "AOSOffline2YearSeparationForm.pdf";
    public static final String AOS_OFFLINE_5_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE =
        "AOSOffline5YearSeparationForm.pdf";
    public static final String AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_FILE =
        "AOSOfflineAdulteryFormRespondent.pdf";
    public static final String AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_FILE =
        "AOSOfflineAdulteryFormCoRespondent.pdf";
    public static final String AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_FILE =
        "AOSOfflineBehaviourDesertionForm.pdf";
    public static final String AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_NAME_FOR_PDF_FILE =
        "AOSOfflineInvitationLetterRespondent.pdf";
    public static final String AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_NAME_FOR_PDF_FILE =
        "AOSOfflineInvitationLetterCoRespondent.pdf";

    public static final String SOLICITOR_PERSONAL_SERVICE_NAME_FOR_PDF_FILE = "SolicitorPersonalService.pdf";
    public static final String DN_REFUSAL_ORDER_CLARIFICATION_NAME_FOR_PDF_FILE = "DecreeNisiClarificationOrder.pdf";
    public static final String DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_FILE = "DecreeNisiRefusalOrder.pdf";

    // Template Data Mapper Constants
    public static final String CASE_DATA = "case_data";
    public static final String CASE_DETAILS = "caseDetails";
    public static final String CCD_DATE_FORMAT = "yyyy-MM-dd";
    public static final String CCD_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String CO_RESPONDENT_WISH_TO_NAME = "D8ReasonForDivorceAdulteryWishToName";
    public static final String COURT_CONTACT_KEY = "CourtContactDetails";
    public static final String COURT_HEARING_DATE_KEY = "DateOfHearing";
    public static final String COURT_HEARING_JSON_KEY = "DateAndTimeOfHearing";
    public static final String COURT_HEARING_TIME_KEY = "TimeOfHearing";
    public static final String D8_MARRIAGE_DATE_KEY = "D8MarriageDate";
    public static final String DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY = "DAEligibleFromDate";
    public static final String DECREE_NISI_GRANTED_DATE_KEY = "DecreeNisiGrantedDate";
    public static final String DECREE_NISI_SUBMITTED_DATE_KEY = "DNApplicationSubmittedDate";
    public static final String DECREE_ABSOLUTE_GRANTED_DATE_KEY = "DecreeAbsoluteGrantedDate";
    public static final String BEHAVIOUR_MOST_RECENT_DATE_DN_KEY = "BehaviourMostRecentIncidentDateDN";
    public static final String ADULTERY_FOUND_OUT_DATE_KEY = "AdulteryDateFoundOut";
    public static final String DN_APPROVAL_DATE_KEY = "DNApprovalDate";
    public static final String ISSUE_DATE_KEY = "IssueDate";
    public static final String LETTER_DATE_FORMAT = "dd MMMM yyyy";
    public static final String SERVICE_COURT_NAME_KEY = "ServiceCourtName";
    public static final String SERVICE_CENTRE_COURT_NAME = "Courts and Tribunals Service Centre";
    public static final String SERVICE_CENTRE_COURT_CONTACT_DETAILS = "c/o HMCTS Digital Divorce"
        + "\nPO Box 12706\nHarlow\nCM20 9QT\n\nEmail: divorcecase@justice.gov.uk\nPhone: 0300 303"
        + " 0642 (from 8.30am to 5pm)";
    public static final String SOLICITOR_IS_NAMED_CO_RESPONDENT = "D8ReasonForDivorceAdulteryIsNamed";
    public static final String SPACE_DELIMITER = " ";
    public static final String CLIAM_COSTS_FROM = "D8DivorceClaimFrom";
    public static final String CLIAM_COSTS_FROM_RESP_CORESP = "ClaimFromRespCoResp";
    public static final String CLIAM_COSTS_FROM_RESP = "ClaimFromResp";
    public static final String CLIAM_COSTS_FROM_CORESP = "ClaimFromCoResp";
    public static final String REFUSAL_CLARIFICATION_REASONS = "RefusalClarificationReason";
    public static final String REFUSAL_REJECTION_REASONS = "RefusalRejectionReason";
    public static final String JURISDICTION_CLARIFICATION_VALUE = "jurisdictionDetails";
    public static final String MARRIAGE_CERT_CLARIFICATION_VALUE = "marriageCertificate";
    public static final String MARRIAGE_CERT_TRANSLATION_CLARIFICATION_VALUE = "marriageCertTranslation";
    public static final String PREVIOUS_PROCEEDINGS_CLARIFICATION_VALUE = "previousProceedingDetails";
    public static final String CASE_DETAILS_STATEMENT_CLARIFICATION_VALUE = "caseDetailsStatement";
    public static final String NO_JURISDICTION_REJECTION_VALUE = "noJurisdiction";
    public static final String NO_CRITERIA_REJECTION_VALUE = "noCriteria";
    public static final String INSUFFICIENT_DETAILS_REJECTION_VALUE = "insufficentDetails";
    public static final String FREE_TEXT_ORDER_VALUE = "other";
    public static final String HAS_JURISDICTION_CLARIFICATION_KEY = "HasJurisdictionClarificationReason";
    public static final String HAS_MARRIAGE_CERT_CLARIFICATION_KEY = "HasMarriageCertClarificationReason";
    public static final String HAS_MARRIAGE_CERT_TRANSLATION_CLARIFICATION_KEY
        = "HasMarriageCertTranslationClarificationReason";
    public static final String HAS_PREVIOUS_PROCEEDINGS_CLARIFICATION_KEY = "HasPreviousProceedingsClarificationReason";
    public static final String HAS_CASE_DETAILS_STATEMENT_CLARIFICATION_KEY
        = "HasCaseDetailsStatementClarificationReason";
    public static final String HAS_NO_JURISDICTION_REJECTION_KEY = "HasNoJurisdiction";
    public static final String HAS_NO_CRITERIA_REJECTION_KEY = "HasApplicationDoesNotFitCriteria";
    public static final String HAS_INSUFFICIENT_DETAILS_REJECTION_KEY = "HasInsufficientDetails";
    public static final String HAS_FREE_TEXT_ORDER_KEY = "HasFreeTextOrder";
    public static final String HAS_ONLY_FREE_TEXT_ORDER_KEY = "HasOnlyFreeTextOrder";
    public static final String YES_VALUE = "yes";
}
