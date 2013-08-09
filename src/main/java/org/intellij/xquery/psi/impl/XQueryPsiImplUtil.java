/*
 * Copyright 2013 Grzegorz Ligas <ligasgr@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.intellij.xquery.psi.impl;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.ResolveScopeManager;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.xquery.psi.*;
import org.intellij.xquery.reference.*;
import org.jetbrains.annotations.NotNull;

/**
 * User: ligasgr
 * Date: 10/02/13
 * Time: 18:59
 */
public class XQueryPsiImplUtil {

    private static final int DOLLAR_CHAR_LENGTH = 1;
    private static final int SEPARATOR_LENGTH = 1;

    public static String getName(XQueryNamespaceName element) {
        return element.getNameIdentifier().getText();
    }

    public static PsiElement setName(XQueryNamespaceName element, String newName) {
        XQueryNamespaceName name = element;
        if (name != null) {
            name.replace(XQueryElementFactory.createModuleDeclarationName(element.getProject(), newName));
        }
        return element;
    }

    public static PsiElement getNameIdentifier(XQueryNamespaceName element) {
        return element;
    }


    public static PsiReference getReference(XQueryVarRef element) {
        int localNameOffset = DOLLAR_CHAR_LENGTH;
        if (element.getVarName() != null) {
            if (element.getVarName().getVarNamespace() != null) {
                localNameOffset += element.getVarName().getVarNamespace().getTextLength() + SEPARATOR_LENGTH;
            }
            return new XQueryVariableReference(element, new TextRange(localNameOffset, element.getTextLength()));
        }
        return null;
    }

    public static String getName(XQueryVarName element) {
        if (element.getNameIdentifier() != null) {
            return element.getNameIdentifier().getText();
        } else {
            return null;
        }
    }

    public static PsiElement setName(XQueryVarName element, String newName) {
        XQueryVarName name = element;
        if (name != null) {
            XQueryVarLocalName localName = name.getVarLocalName();
            if (localName != null) {
                XQueryVarName newNameElement = XQueryElementFactory.createVariableReference(element.getProject(),
                        newName);
                localName.replace(newNameElement.getVarLocalName());
            }
        }
        return element;
    }

    public static PsiElement getNameIdentifier(XQueryVarName element) {
        if (element == null) return null;
        return element.getVarLocalName();
    }

    public static int getTextOffset(XQueryVarName element) {
        if (element == null || element.getVarLocalName() == null) return 0;
        return getNameIdentifier(element).getTextOffset();
    }

    public static boolean processDeclarations(XQueryProlog module, @NotNull PsiScopeProcessor processor,
                                              @NotNull ResolveState state, PsiElement lastParent,
                                              @NotNull PsiElement place) {
        return processor.execute(module, state);
    }

    public static PsiReference getReference(XQueryModuleImportPath element) {
        if (element.getURILiteral() != null) {
            String filename = stripApostrophes(element.getURILiteral().getText());
            if (!StringUtil.isEmptyOrSpaces(filename)) {
                return new XQueryModuleReference(element, filename, new TextRange(1,
                        element.getURILiteral().getTextLength() - 1));
            }
        }
        return null;
    }

    private static String stripApostrophes(String text) {
        return text.replaceAll("\"", "").replaceAll("'", "");
    }

    public static PsiReference getReference(XQueryVarNamespace element) {
        return new XQueryVariableNamespaceNameReference(element, new TextRange(0, element.getTextLength()));
    }

    public static PsiReference getReference(XQueryFunctionNamespace element) {
        return new XQueryFunctionNamespaceNameReference(element, new TextRange(0, element.getTextLength()));
    }

    public static PsiReference getReference(XQueryFunctionCall element) {
        int localNameOffset = 0;
        if (element.getFunctionName().getFunctionNamespace() != null) {
            localNameOffset += element.getFunctionName().getFunctionNamespace().getTextLength() + SEPARATOR_LENGTH;
        }
        return new XQueryFunctionReference(element, new TextRange(localNameOffset,
                element.getFunctionName().getTextLength()));
    }

    public static String getName(XQueryFunctionName element) {
        if (element.getNameIdentifier() != null) {
            return element.getNameIdentifier().getText();
        } else {
            return null;
        }
    }

    public static PsiElement setName(XQueryFunctionName element, String newName) {
        XQueryFunctionName name = element;
        if (name != null) {
            XQueryFunctionLocalName localName = name.getFunctionLocalName();
            if (localName != null) {
                XQueryFunctionName newNameElement = XQueryElementFactory.createFunctionReference(element.getProject()
                        , "dummy", newName);
                localName.replace(newNameElement.getFunctionLocalName());
            }
        }
        return element;
    }

    public static PsiElement getNameIdentifier(XQueryFunctionName element) {
        if (element == null) return null;
        return element.getFunctionLocalName();
    }

    public static int getTextOffset(XQueryFunctionName element) {
        if (element == null || element.getFunctionLocalName() == null) return 0;
        return getNameIdentifier(element).getTextOffset();
    }

    public static SearchScope getUseScope(XQueryVarName element) {
        XQueryFunctionDecl function = PsiTreeUtil.getParentOfType(element, XQueryFunctionDecl.class, true);
        if (function != null) {
            return new LocalSearchScope(function);
        }
        XQueryQueryBody queryBody = PsiTreeUtil.getParentOfType(element, XQueryQueryBody.class, true);
        if (queryBody != null) {
            return new LocalSearchScope(queryBody);
        }
        return ResolveScopeManager.getElementUseScope(element);
    }
}