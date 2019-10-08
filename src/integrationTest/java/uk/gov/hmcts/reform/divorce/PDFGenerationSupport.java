package uk.gov.hmcts.reform.divorce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PDFGenerationSupport {
    static List<Object[]> getFormNames(final boolean respSolicitorEnabled) {
        List<Object[]> basicTestData = Arrays.asList(new Object[][]{
            {"mini-petition-resp-confidential-addr"},
            {"mini-petition-draft-resp-confidential-addr"},
            {"mini-petition-draft"},
            {"mini-petition-draft-no-place-of-marriage"},
            {"CC--No_FO--No_CN--A_DR-AD-CRK-NO-PL-NO-DT-NO_LP--NO"},
            {"CC--Res_FO--No_CN--B_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--Corres_FO--No_CN--C_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--CorresRes_FO--No_CN--AC_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--Res_FO--App_CN--D_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--Corres_FO--App_CN--E_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--CoresRes_FO--App_CN--F_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--Res_FO--Child_CN--G_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--Corres_FO--Child_CN--BCDEFG_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--CorresRes_FO--Child_CN--AD_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--CorresRes_FO--ChildApp_CN--ADEF_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--cores_FO--ChildApp_CN--BCDE_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
            {"CC--cores_FO--ChildApp_CN--BCDE_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes-Special-Chars-Confidential"},
            {"CC--Res_FO--ChildApp_CN--CDEF_DR-UB-CRK-No-PL-No-DT-No_LP--No"},
            {"CC--No_FO--App_CN--EF_DR-S2Y-CRK-No-PL-No-DT-No_LP--No"},
            {"CC--No_FO--Child_CN--BCF_DR-S5Y-CRK-No-PL-No-DT-No_LP--No"},
            {"CC--No_FO--ChildApp_CN--ABCDEFG_DR-DES-CRK-No-PL-No-DT-No_LP--No"},
            {"CC--cores_FO--ChildApp_CN--BCDE_DR-UB-CRK-Yes-PL-Yes-DT-Yes_LP--Yes-Long-SoC-Solicitors"},
            {"CC--cores_FO--ChildApp_CN--BCDE_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes-Petitioner-Solicitor"},
            {"5YearSeparationWithMentalSeparationDate"},
            {"AOS_Co-respondent_Online"},
            {"AOS_Co-respondent_Paper"},
            {"Desertion"},
            {"Desertion-Amend-Case"},
            {"Resp_2Year_Defend_Response"},
            {"Resp_2Year_Undefend_Response"},
            {"Resp_5Year_Defend_Response_No_Claim_Costs"},
            {"Resp_5Year_Undefend_Response"},
            {"Resp_Adultery_Defend_Response"},
            {"Resp_Adultery_Undefend_Response"},
            {"Resp_Behaviour_Defend_Response_No_Claim_Costs"},
            {"Resp_Behaviour_Undefend_noAdmit_Response"},
            {"Resp_Behaviour_Undefend_Response"},
            {"Resp_Desertion_Defend_Response"},
            {"Resp_Desertion_Undefend_noAdmit_Response"},
            {"Resp_Desertion_Undefend_Response"},
            {"co-respondent-answers-defended-admit-costs"},
            {"co-respondent-answers-undefended-no-admit-no-costs"},
            {"AOS_Offline-Invitation-Letter-Respondent"},
            {"AOS_Offline-Invitation-Letter-Co-Respondent"},
            {"AOS_Offline-2-Year-Separation-Form"},
            {"AOS_Offline-5-Year-Separation-Form"},
            {"AOS_Offline-Behaviour-Desertion-Form"},
            {"AOS_Offline-Adultery-Form-Respondent"},
            {"AOS_Offline-Adultery-Form-Co-Respondent"},
            {"solicitor-personal-service"},
            {"decree-nisi-refusal-order-clarification"},
            {"decree-nisi-refusal-order-rejection"}
        });

        List testData = new ArrayList(basicTestData);

        if (respSolicitorEnabled) {
            testData.addAll(Arrays.asList(new Object[][] {
                {"AOS_Solicitor"},
                {"AOS_Hus_Res-Addr_DivUnit-SC-Sol-Online-Avl"},
                {"AOS_Same-Sex-Female-Sol-Online-Avl"},
                {"AOS_Same-Sex-Male-Sol-Online-Avl"},
                {"AOS_Amend_Petition-Sol-Online-Avl"},
                {"AOS_Hus_Res-Addr_DivUnit-EM-Sol-Online-Avl"}
            }));
        } else {
            testData.addAll(Arrays.asList(new Object[][] {
                {"AOS_Wife_Sol-Addr_DivUnit-WM"},
                {"AOS_Wife_Sol-Addr_DivUnit-SW_No-Sol-Company"},
                {"AOS_Hus_Res-Addr_DivUnit-SC"},
                {"AOS_Same-Sex-Female"},
                {"AOS_Same-Sex-Male"},
                {"AOS_Amend_Petition"},
                {"AOS_Hus_Res-Addr_DivUnit-EM"}
            }));
        }
        return testData;
    }
}
