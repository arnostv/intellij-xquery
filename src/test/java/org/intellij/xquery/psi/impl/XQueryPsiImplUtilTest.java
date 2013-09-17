package org.intellij.xquery.psi.impl;

import com.intellij.navigation.ItemPresentation;
import org.intellij.xquery.psi.XQueryFunctionDecl;
import org.intellij.xquery.psi.XQueryFunctionName;
import org.intellij.xquery.psi.XQueryParamList;
import org.intellij.xquery.psi.XQuerySequenceType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class XQueryPsiImplUtilTest {
    @Mock XQueryFunctionDecl functionDecl;
    @Mock XQueryFunctionName functionName;
    @Mock XQueryParamList paramList;
    @Mock XQuerySequenceType sequenceType;

    @Before
    public void setUp() throws Exception {
        when(functionDecl.getFunctionName()).thenReturn(functionName);
        when(functionDecl.getParamList()).thenReturn(paramList);
        when(functionDecl.getSequenceType()).thenReturn(sequenceType);
    }

    @Test
    public void shouldBuildFullFunctionPresentation() throws Exception {
        when(functionName.getText()).thenReturn("hello");
        when(paramList.getText()).thenReturn("($a, $b)");
        when(sequenceType.getText()).thenReturn("xs:boolean");

        ItemPresentation presentation = XQueryPsiImplUtil.getPresentation(functionDecl);

        assertEquals("hello($a, $b) as xs:boolean", presentation.getPresentableText());
    }

    @Test
    public void shouldBuildFunctionPresentationWhenSequenceTypeIsNotDefined() throws Exception {
        when(functionName.getText()).thenReturn("hello");
        when(paramList.getText()).thenReturn("($a, $b)");
        when(functionDecl.getSequenceType()).thenReturn(null);

        ItemPresentation presentation = XQueryPsiImplUtil.getPresentation(functionDecl);

        assertEquals("hello($a, $b)", presentation.getPresentableText());
    }
}
