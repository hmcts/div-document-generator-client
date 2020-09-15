package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplatesConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentManagementServiceImplTest {
    private static final String IS_DRAFT = "isDraft";
    private static final String MINI_PETITION_NAME_FOR_WELSH_PDF_FILE = "DivorcePetitionWelsh.pdf";
    private static final String DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE = "DraftDivorcePetition.pdf";
    private static final String DRAFT_DIVORCE_PETITION_WELSH_PDF = "DraftDivorcePetitionWelsh.pdf";
    private static final String D8_PETITION_WELSH_TEMPLATE = "FL-DIV-GNO-WEL-00256.docx";

    private static final String DRAFT_MINI_PETITION_TEMPLATE_ID = "divorcedraftminipetition";

    @Mock
    private PDFGenerationFactory pdfGenerationFactory;

    @Mock
    private PDFGenerationService pdfGenerationService;

    @Mock
    private EvidenceManagementService evidenceManagementService;

    @Mock
    private TemplatesConfiguration templatesConfiguration;

    @InjectMocks
    private DocumentManagementServiceImpl classUnderTest;

    @Captor
    ArgumentCaptor<Map<String, Object>> placeHolderCaptor;

    @Test
    public void testGenerateAndStoreDraftDocumentMock() {
        final Map<String, Object> placeholderMap = new HashMap<>();
        final String authToken = "someToken";
        final byte[] data = {126};

        when(pdfGenerationFactory.getGeneratorService(D8_PETITION_WELSH_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(D8_PETITION_WELSH_TEMPLATE, placeholderMap)).thenReturn(data);
        when(templatesConfiguration.getFileNameByTemplateName(D8_PETITION_WELSH_TEMPLATE)).thenReturn(MINI_PETITION_NAME_FOR_WELSH_PDF_FILE);

        classUnderTest.generateAndStoreDraftDocument(D8_PETITION_WELSH_TEMPLATE, placeholderMap, authToken);

        verify(evidenceManagementService).storeDocumentAndGetInfo(data, authToken, DRAFT_DIVORCE_PETITION_WELSH_PDF);
        verify(pdfGenerationService).generate(same(D8_PETITION_WELSH_TEMPLATE), placeHolderCaptor.capture());
        Map<String, Object> value = placeHolderCaptor.getValue();
        assertThat("Draft value set ", value.get(IS_DRAFT), is(true));
    }

    @Test
    public void testGenerateAndStoreDraftDocument_WithDraftPrefixMock() {

        final Map<String, Object> placeholderMap = new HashMap<>();
        final String authToken = "someToken";
        final byte[] data = {126};

        when(pdfGenerationFactory.getGeneratorService(DRAFT_MINI_PETITION_TEMPLATE_ID))
            .thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(DRAFT_MINI_PETITION_TEMPLATE_ID, placeholderMap)).thenReturn(data);
        when(templatesConfiguration.getFileNameByTemplateName(DRAFT_MINI_PETITION_TEMPLATE_ID)).thenReturn(DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE);

        classUnderTest.generateAndStoreDraftDocument(DRAFT_MINI_PETITION_TEMPLATE_ID, placeholderMap, authToken);

        verify(evidenceManagementService).storeDocumentAndGetInfo(data, authToken,
            DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE);

        verify(pdfGenerationService).generate(same(DRAFT_MINI_PETITION_TEMPLATE_ID), placeHolderCaptor.capture());
        Map<String, Object> value = placeHolderCaptor.getValue();
        assertThat("Draft value set ", value.get(IS_DRAFT), is(true));
    }
}
