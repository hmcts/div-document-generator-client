package uk.gov.hmcts.reform.divorce.documentgenerator.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateConstants {
    // Generators
    public static final String DOCMOSIS_TYPE = "docmosis";
    public static final String PDF_GENERATOR_TYPE = "pdfgenerator";

    // Template Ids
    public static final String DN_ANSWERS_TEMPLATE_ID = "FL-DIV-GNO-ENG-00022.docx";
    public static final String AOS_OFFLINE_5_YEAR_SEP_FORM_WELSH_TEMPLATE_ID = "FL-DIV-APP-WEL-00247.docx";
    public static final String AOS_OFFLINE_2_YEAR_SEPARATION_FORM_WELSH_TEMPLATE_ID = "FL-DIV-APP-WEL-00246.docx";
    public static final String DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID = "FL-DIV-DEC-ENG-00088.docx";
    public static final String DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID = "FL-DIV-DEC-ENG-00098.docx";
    public static final String AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_WELSH_TEMPLATE_ID = "FL-DIV-APP-WEL-00248.docx";
    public static final String AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_WELSH_TEMPLATE_ID = "FL-DIV-APP-WEL-00249.docx";
    public static final String AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_WELSH_TEMPLATE_ID = "FL-DIV-APP-WEL-00250.docx";
    public static final String COSTS_ORDER_WELSH_DOCUMENT_ID = "FL-DIV-DEC-WEL-00240.docx";
    public static final String DN_REFUSAL_ORDER_CLARIFICATION_WELSH_TEMPLATE_ID = "FL-DIV-DEC-WEL-00251.docx";

    // Template Names
    public static final String DECREE_NISI_ANSWERS_TEMPLATE_NAME = "DecreeNisiAnswers.pdf";
    public static final String AOS_OFFLINE_5_YEAR_SEP_FORM_NAME_FOR_PDF_WELSH_FILE =
        "AOSOffline5YearSeparationFormWelsh.pdf";
    public static final String AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_WELSH_FILE =
        "AOSOffline2YearSeparationFormWelsh.pdf";
    public static final String DN_REFUSAL_ORDER_CLARIFICATION_NAME_FOR_PDF_FILE = "DecreeNisiClarificationOrder.pdf";
    public static final String DN_REFUSAL_ORDER_CLARIFICATION_NAME_FOR_PDF_WELSH_FILE =
        "DecreeNisiClarificationOrderWelsh.pdf";
    public static final String DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_FILE = "DecreeNisiRefusalOrder.pdf";
    public static final String AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_WELSH_FILE =
        "AOSOfflineBehaviourDesertionFormWelsh.pdf";
    public static final String AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_WELSH_FILE =
        "AOSOfflineAdulteryFormRespondentWelsh.pdf";
    public static final String AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_WELSH_FILE =
        "AOSOfflineAdulteryFormCoRespondentWelsh.pdf";
    public static final String COSTS_ORDER_NAME_FOR_PDF_WELSH_FILE = "CostsOrderWelsh.pdf";

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
        + "\nPO Box 12706\nHarlow\nCM20 9QT\n\nEmail: divorcecase@justice.gov.uk\n"
        + "Phone: 0300 303 0642 (Monday to Friday 8am to 8pm, Saturday 8am to 2pm)";
    public static final String SOLICITOR_IS_NAMED_CO_RESPONDENT = "D8ReasonForDivorceAdulteryIsNamed";
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
