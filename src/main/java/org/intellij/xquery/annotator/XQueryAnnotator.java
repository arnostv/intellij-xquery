/*
 * Copyright 2013-2014 Grzegorz Ligas <ligasgr@gmail.com> and other contributors
 * (see the CONTRIBUTORS file).
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

package org.intellij.xquery.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import org.intellij.xquery.annotator.duplicateFunction.ErrorAnnotationCreator;
import org.intellij.xquery.annotator.variable.UnresolvedVariableChecker;
import org.intellij.xquery.annotator.xqdoc.XQDocHighlighter;
import org.intellij.xquery.psi.XQueryFile;
import org.intellij.xquery.psi.XQueryFunctionName;
import org.intellij.xquery.psi.XQueryVarRef;
import org.jetbrains.annotations.NotNull;

public class XQueryAnnotator implements Annotator, DumbAware {

    private ErrorAnnotationCreator duplicateFunctionErrorCreator = new ErrorAnnotationCreator();
    private XQDocHighlighter xQDocHighlighter = new XQDocHighlighter();
    private UnresolvedVariableChecker unresolvedVariableChecker = new UnresolvedVariableChecker();

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof XQueryFunctionName) {
            duplicateFunctionErrorCreator.createDuplicateFunctionDeclarationError(holder, (XQueryFunctionName) element, (XQueryFile) element.getContainingFile());
        }
        if (element instanceof PsiComment) {
            xQDocHighlighter.highlightXQDocTags((PsiComment) element, holder);
        }
        if (element instanceof XQueryVarRef) {
            unresolvedVariableChecker.check((XQueryVarRef) element, holder);
        }
    }
}
