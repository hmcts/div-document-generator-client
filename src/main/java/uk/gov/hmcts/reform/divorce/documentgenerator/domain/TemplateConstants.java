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
    public static final String DN_GRANTED_AND_COST_ORDER_FOR_RESPONDENT_SOLICITOR_TEMPLATE_ID = "FL-DIV-GNO-ENG-00356.docx";
    public static final String DN_GRANTED_AND_COST_ORDER_FOR_RESPONDENT_TEMPLATE_ID = "FL-DIV-LET-ENG-00357.docx";
    public static final String DA_GRANTED_LETTER_TEMPLATE_ID = "FL-DIV-GOR-ENG-00355.docx";
    public static final String DA_GRANTED_COVER_LETTER_FOR_RESPONDENT_SOLICITOR_TEMPLATE_ID =
        "FL-DIV-GOR-ENG-00353.docx";
    public static final String COE_COVER_LETTER_FOR_RESPONDENT_TEMPLATE_ID = "FL-DIV-LET-ENG-00360.docx";
    public static final String COE_COVER_LETTER_FOR_CORESPONDENT_TEMPLATE_ID = "FL-DIV-GNO-ENG-00449.docx";
    public static final String COE_COVER_LETTER_FOR_RESPONDENT_SOLICITOR_TEMPLATE_ID = "FL-DIV-GNO-ENG-00370.docx";
    public static final String COST_ORDER_COVER_LETTER_FOR_CO_RESPONDENT_TEMPLATE_ID = "FL-DIV-LET-ENG-00358.docx";
    public static final String CoE_CO_RESPONDENT_SOLICITOR_CL = "FL-DIV-GNO-ENG-00447.docx";
    public static final String CO_LETTER_CO_RESPONDENT_SOLICITOR = "FL-DIV-GNO-ENG-00423.docx";

    // Template Names
    public static final String AOS_INVITATION_NAME_FOR_PDF_FILE = "AOSInvitation.pdf";
    public static final String CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE = "CertificateOfEntitlement.pdf";
    public static final String CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "CoRespondentAnswers.pdf";
    public static final String CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE = "CoRespondentInvitation.pdf";
    public static final String DECREE_NISI_TEMPLATE_NAME = "DecreeNisiPronouncement.pdf";
    public static final String DECREE_NISI_ANSWERS_TEMPLATE_NAME = "DecreeNisiAnswers.pdf";
    public static final String DA_GRANTED_LETTER_TEMPLATE_NAME_FOR_PDF_FILE = "DecreeAbsoluteGrantedLetter.pdf";
    public static final String COE_CORESPONDENT_SOLICITOR_CL_TEMPLATE_NAME_FOR_PDF_FILE = "CoECoRespondentSolicitorCoverLetter.pdf";
    public static final String CO_LETTER_CO_RESPONDENT_SOLICITOR_TEMPLATE_NAME_FOR_PDF_FILE = "CostOrderLetterForCoRespondentSolicitor.pdf";
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
    public static final String DN_GRANTED_AND_COST_ORDER_FOR_RESPONDENT_FILE_NAME = "DecreeNisiGrantedForRespondent.pdf";
    public static final String DN_GRANTED_AND_COST_ORDER_FOR_RESPONDENT_SOLICITOR_FILE_NAME =
        "DecreeNisiGrantedCoverLetterForRespondentSolicitor.pdf";
    public static final String DA_GRANTED_COVER_LETTER_FOR_RESPONDENT_SOLICITOR_FILE_NAME =
        "DecreeAbsoluteGrantedCoverLetterForRespondentSolicitor.pdf";
    public static final String COE_COVER_LETTER_FOR_RESPONDENT_FILE_NAME =
        "CertificateOfEntitlementCoverLetterForRespondent.pdf";
    public static final String COE_COVER_LETTER_FOR_CORESPONDENT_FILE_NAME = "CoECoverLetterForCoRespondent.pdf";
    public static final String COE_COVER_LETTER_FOR_RESPONDENT_SOLICITOR_FILE_NAME =
        "CertificateOfEntitlementCoverLetterForRespondentSolicitor.pdf";
    public static final String COST_ORDER_COVER_LETTER_FOR_CO_RESPONDENT_FILE_NAME =
        "CostOrderCoverLetterForCoRespondent.pdf";

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
    public static final String WELSH_DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY = "welshDAEligibleFromDate";
    public static final String WELSH_DECREE_ABSOLUTE_GRANTED_DATE_KEY = "welshDecreeAbsoluteGrantedDate";
    public static final String WELSH_DECREE_NISI_GRANTED_DATE_KEY = "welshDecreeNisiGrantedDate";
    public static final String WELSH_DN_APPROVAL_DATE_KEY = "welshDNApprovalDate";
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

    //Add Translation
    public static final String ENGLISH_VALUE = "ENGLISH";
    public static final String WELSH_VALUE = "WELSH";

    public static final String D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS = "D8PetitionerNameChangedHowOtherDetails";
    public static final String D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_EN =
        "D8PetitionerNameChangedHowOtherDetailsEN";
    public static final String D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_CY =
        "D8PetitionerNameChangedHowOtherDetailsCY";
    public static final String D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS =
        "D8PetitionerNameChangedHowOtherDetailsTrans";
    public static final String D8_PETITIONER_NAME_CHANGED_HOW_OTHER_DETAILS_TRANS_LANG =
        "D8PetitionerNameChangedHowOtherDetailsTransLang";

    public static final String D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS = "D8ReasonForDivorceBehaviourDetails";
    public static final String D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_EN = "D8ReasonForDivorceBehaviourDetailsEN";
    public static final String D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_CY = "D8ReasonForDivorceBehaviourDetailsCY";
    public static final String D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS =
        "D8ReasonForDivorceBehaviourDetailsTrans";
    public static final String D8_REASON_FOR_DIVORCE_BEHAVIOUR_DETAILS_TRANS_LANG =
        "D8ReasonForDivorceBehaviourDetailsTransLang";

    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS = "D8ReasonForDivorceAdulteryDetails";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_EN = "D8ReasonForDivorceAdulteryDetailsEN";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_CY = "D8ReasonForDivorceAdulteryDetailsCY";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS = "D8ReasonForDivorceAdulteryDetailsTrans";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_DETAILS_TRANS_LANG =
        "D8ReasonForDivorceAdulteryDetailsTransLang";

    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS  = "D8ReasonForDivorceAdulteryWhenDetails";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_EN  =
        "D8ReasonForDivorceAdulteryWhenDetailsEN";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_CY  =
        "D8ReasonForDivorceAdulteryWhenDetailsCY";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS  =
        "D8ReasonForDivorceAdulteryWhenDetailsTrans";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHEN_DETAILS_TRANS_LANG  =
        "D8ReasonForDivorceAdulteryWhenDetailsTransLang";

    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS  = "D8ReasonForDivorceAdulteryWhereDetails";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_EN  =
        "D8ReasonForDivorceAdulteryWhereDetailsEN";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_CY  =
        "D8ReasonForDivorceAdulteryWhereDetailsCY";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS  =
        "D8ReasonForDivorceAdulteryWhereDetailsTrans";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_WHERE_DETAILS_TRANS_LANG  =
        "D8ReasonForDivorceAdulteryWhereDetailsTransLang";

    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS  =
        "D8ReasonForDivorceAdultery2ndHandDetails";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_EN  =
        "D8ReasonForDivorceAdultery2ndHandDetailsEN";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_CY  =
        "D8ReasonForDivorceAdultery2ndHandDetailsCY";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS  =
        "D8ReasonForDivorceAdultery2ndHandDetailsTrans";
    public static final String D8_REASON_FOR_DIVORCE_ADULTERY_2ND_HAND_DETAILS_TRANS_LANG  =
        "D8ReasonForDivorceAdultery2ndHandDetailsTransLang";

    public static final String D8_REASON_FOR_DIVORCE_DESERTION_DETAILS  =
        "D8ReasonForDivorceDesertionDetails";
    public static final String D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_EN  =
        "D8ReasonForDivorceDesertionDetailsEN";
    public static final String D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_CY  =
        "D8ReasonForDivorceDesertionDetailsCY";
    public static final String D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS  =
        "D8ReasonForDivorceDesertionDetailsTrans";
    public static final String D8_REASON_FOR_DIVORCE_DESERTION_DETAILS_TRANS_LANG  =
        "D8ReasonForDivorceDesertionDetailsTransLang";

    public static final String D8_LEGAL_PROCEEDINGS_DETAILS  = "D8LegalProceedingsDetails";
    public static final String D8_LEGAL_PROCEEDINGS_DETAILS_EN  = "D8LegalProceedingsDetails_EN";
    public static final String D8_LEGAL_PROCEEDINGS_DETAILS_CY  = "D8LegalProceedingsDetails_CY";
    public static final String D8_LEGAL_PROCEEDINGS_DETAILS_TRANS  =
        "D8LegalProceedingsDetailsTrans";
    public static final String D8_LEGAL_PROCEEDINGS_DETAILS_TRANS_LANG  =
        "D8LegalProceedingsDetailsTransLang";

    public static final String RESP_JURISDICTION_DISAGREE_REASON  = "RespJurisdictionDisagreeReason";
    public static final String RESP_JURISDICTION_DISAGREE_REASON_EN  = "RespJurisdictionDisagreeReasonEN";
    public static final String RESP_JURISDICTION_DISAGREE_REASON_CY  = "RespJurisdictionDisagreeReasonCY";
    public static final String RESP_JURISDICTION_DISAGREE_REASON_TRANS  =
        "RespJurisdictionDisagreeReasonTrans";
    public static final String RESP_JURISDICTION_DISAGREE_REASON_TRANS_LANG  =
        "RespJurisdictionDisagreeReasonTransLang";

    public static final String RESP_LEGAL_PROCEEDINGS_DESCRIPTION  = "RespLegalProceedingsDescription";
    public static final String RESP_LEGAL_PROCEEDINGS_DESCRIPTION_EN  = "RespLegalProceedingsDescriptionEN";
    public static final String RESP_LEGAL_PROCEEDINGS_DESCRIPTION_CY  = "RespLegalProceedingsDescriptionCY";
    public static final String RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS  =
        "RespLegalProceedingsDescriptionTrans";
    public static final String RESP_LEGAL_PROCEEDINGS_DESCRIPTION_TRANS_LANG  =
        "RespLegalProceedingsDescriptionTransLang";

    public static final String RESP_COSTS_REASON  = "RespCostsReason";
    public static final String RESP_COSTS_REASON_EN  = "RespCostsReasonEN";
    public static final String RESP_COSTS_REASON_CY  = "RespCostsReasonCY";
    public static final String RESP_COSTS_REASON_TRANS  = "RespCostsReasonTrans";
    public static final String RESP_COSTS_REASON_TRANS_LANG  = "RespCostsReasonTransLang";

    public static final String CO_RESP_COSTS_REASON  = "CoRespCostsReason";
    public static final String CO_RESP_COSTS_REASON_EN  = "CoRespCostsReasonEN";
    public static final String CO_RESP_COSTS_REASON_CY  = "CoRespCostsReasonCY";
    public static final String CO_RESP_COSTS_REASON_TRANS  = "CoRespCostsReasonTrans";
    public static final String CO_RESP_COSTS_REASON_TRANS_LANG  = "CoRespCostsReasonTransLang";

    public static final String PETITION_CHANGED_DETAILS_DN  = "PetitionChangedDetailsDN";
    public static final String PETITION_CHANGED_DETAILS_DN_EN  = "PetitionChangedDetailsDNEN";
    public static final String PETITION_CHANGED_DETAILS_DN_CY  = "PetitionChangedDetailsDNCY";
    public static final String PETITION_CHANGED_DETAILS_DN_TRANS  = "PetitionChangedDetailsDNTrans";
    public static final String PETITION_CHANGED_DETAILS_DN_TRANS_LANG  = "PetitionChangedDetailsDNTransLang";

    public static final String ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN  = "AdulteryTimeLivedTogetherDetailsDN";
    public static final String ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_EN  = "AdulteryTimeLivedTogetherDetailsDNEN";
    public static final String ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_CY  = "AdulteryTimeLivedTogetherDetailsDNCY";
    public static final String ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS  = "AdulteryTimeLivedTogetherDetailsDNTrans";
    public static final String ADULTERY_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG  = "AdulteryTimeLivedTogetherDetailsDNTransLang";

    public static final String BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN  = "BehaviourTimeLivedTogetherDetailsDN";
    public static final String BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_EN  = "BehaviourTimeLivedTogetherDetailsDNEN";
    public static final String BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_CY  = "BehaviourTimeLivedTogetherDetailsDNCY";
    public static final String BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS  = "BehaviourTimeLivedTogetherDetailsDNTrans";
    public static final String BEHAVIOUR_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG  = "BehaviourTimeLivedTogetherDetailsDNTransLang";


    public static final String DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN  = "DesertionTimeLivedTogetherDetailsDN";
    public static final String DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_EN  = "DesertionTimeLivedTogetherDetailsDNEN";
    public static final String DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_CY  = "DesertionTimeLivedTogetherDetailsDNCY";
    public static final String DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS  = "DesertionTimeLivedTogetherDetailsDNTrans";
    public static final String DESERTION_TIME_LIVED_TOGETHER_DETAILS_DN_TRANS_LANG  = "DesertionTimeLivedTogetherDetailsDNTransLang";

    public static final String COSTS_DIFFERENT_DETAILS  = "CostsDifferentDetails";
    public static final String COSTS_DIFFERENT_DETAILS_EN  = "CostsDifferentDetailsEN";
    public static final String COSTS_DIFFERENT_DETAILS_CY  = "CostsDifferentDetailsCY";
    public static final String COSTS_DIFFERENT_DETAILS_TRANS  = "CostsDifferentDetailsTrans";
    public static final String COSTS_DIFFERENT_DETAILS_TRANS_LANG  = "CostsDifferentDetailsTransLang";

}
