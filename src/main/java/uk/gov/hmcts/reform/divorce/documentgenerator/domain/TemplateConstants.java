package uk.gov.hmcts.reform.divorce.documentgenerator.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateConstants {
    // Generators
    public static final String DOCMOSIS_TYPE = "docmosis";
    public static final String PDF_GENERATOR_TYPE = "pdfgenerator";

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
    public static final String ACCESS_CODE_KEY = "access_code";
    public static final String CASE_ID_KEY = "id";
    public static final String D8_DIVORCE_WHO_KEY = "D8DivorceWho";
    public static final String D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY = "D8ReasonForDivorceDesertionDate";
    public static final String D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY = "D8ReasonForDivorceSeperationDate";
    public static final String D8_MENTAL_SEPARATION_DATE_KEY = "D8MentalSeparationDate";
    public static final String D8_PHYSICAL_SEPARATION_DATE_KEY = "D8PhysicalSeparationDate";
    public static final String PREVIOUS_ISSUE_DATE_KEY = "PreviousIssueDate";
    public static final String LAST_MODIFIED_KEY = "last_modified";
    public static final String DATE_OF_DOCUMENT_PRODUCTION = "dateOfDocumentProduction";
    public static final String WELSH_LAST_MODIFIED_KEY = "welsh_last_modified";
    public static final String WELSH_PREVIOUS_ISSUE_DATE_KEY = "welshPreviousIssueDate";
    public static final String WELSH_D8_DIVORCE_WHO_KEY = "welshD8DivorceWho";
    public static final String WELSH_D8_MARRIAGE_DATE_KEY = "welshD8MarriageDate";
    public static final String WELSH_D8_REASON_FOR_DIVORCE_DESERTION_DATE_KEY = "welshD8ReasonForDivorceDesertionDate";
    public static final String WELSH_D8_MENTAL_SEPARATION_DATE_KEY = "welshD8MentalSeparationDate";
    public static final String WELSH_D8_PHYSICAL_SEPARATION_DATE_KEY = "welshD8PhysicalSeparationDate";
    public static final String WELSH_D8_REASON_FOR_DIVORCE_SEPERATION_DATE_KEY =
            "welshD8ReasonForDivorceSeperationDate";
    public static final String WELSH_COURT_HEARING_DATE_KEY = "welshDateOfHearing";
    public static final String LANGUAGE_PREFERENCE_WELSH_KEY = "LanguagePreferenceWelsh";
    public static final String IS_DRAFT_KEY = "isDraft";
    public static final String FEATURE_TOGGLE_RESP_SOLCIITOR = "featureToggleRespSolicitor";
    public static final String WELSH_CURRENT_DATE_KEY = "welshCurrentDate";
    public static final String WELSH_DATE_OF_DOCUMENT_PRODUCTION = "welshDateOfDocumentProduction";
}
