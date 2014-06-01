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

package org.intellij.xquery.inspection.namespaceprefix;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.util.containers.ContainerUtil;
import org.intellij.xquery.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.intellij.codeInspection.ProblemHighlightType.GENERIC_ERROR_OR_WARNING;

public class NamespaceSameInImportAsInDeclaration extends LocalInspectionTool {

    @Nullable
    @Override
    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        if (!(file instanceof XQueryFile)) {
            return null;
        }

        XQueryFile xQueryFile = (XQueryFile) file;

        List<ImportPair> importPairs = importedNamespaces(xQueryFile);
        List<ImportPair> fixableMismatchedImports = fixableMismatchedImports(importPairs);

        if (fixableMismatchedImports.size() > 0) {
            ProblemDescriptor[] problemDescriptors = new ProblemDescriptor[fixableMismatchedImports.size()];
            for (int i = 0; i < problemDescriptors.length; i++) {
                problemDescriptors[i] = manager.createProblemDescriptor(fixableMismatchedImports.get(i).namespacePrefix, "Namespace prefix in module import should be same as in module declaration", (LocalQuickFix) null,
                        GENERIC_ERROR_OR_WARNING, true);
            }
            return problemDescriptors;
        } else {
            return null;
        }

    }

    public static List<ImportPair> importedNamespaces(final XQueryFile xQueryFile) {
        Collection<XQueryModuleImport> imports = xQueryFile.getModuleImports();
        List<ImportPair> result = new ArrayList<ImportPair>();

        for (XQueryModuleImport moduleImport : imports) {
            String importedPrefix = moduleImport.getNamespacePrefix() != null ? moduleImport.getNamespacePrefix().getName() : null;
            if (importedPrefix != null) {
                Collection<XQueryFile> referencesToExistingFilesInImport = XQueryUtil.getReferencesToExistingFilesInImport(moduleImport);
                for (XQueryFile imported : referencesToExistingFilesInImport) {
                    XQueryModuleDecl moduleDeclaration = imported.getModuleDeclaration();
                    if (moduleDeclaration != null) {
                        XQueryNamespacePrefix namespacePrefix = moduleDeclaration.getNamespacePrefix();
                        if (namespacePrefix != null) {
                            String declaredPrefix = namespacePrefix.getName();
                            result.add(new ImportPair(declaredPrefix, importedPrefix, moduleImport.getNamespacePrefix()));
                        }
                    }
                }
            }
        }
        return result;
    }

    private static List<ImportPair> fixableMismatchedImports(List<ImportPair> importPairs) {
        List<ImportPair> result = new ArrayList<ImportPair>();
        for (final ImportPair importPair : importPairs) {
            if (StringUtil.equals(importPair.declaredPrefix, importPair.importedPrefix)) {
                continue;
            }

            boolean existsOtherImportWhichHasSameImportAsSuggestedFix = ContainerUtil.exists(importPairs, new Condition<ImportPair>() {
                @Override
                public boolean value(ImportPair otherPair) {
                    return StringUtil.equals(importPair.declaredPrefix, otherPair.importedPrefix) && otherPair != importPair;
                }
            });

            if (!existsOtherImportWhichHasSameImportAsSuggestedFix) {
                result.add(importPair);
            }
        }
        return result;
    }

    static class ImportPair {
        ImportPair(String declaredPrefix, String importedPrefix, XQueryNamespacePrefix namespacePrefix) {
            this.declaredPrefix = declaredPrefix;
            this.importedPrefix = importedPrefix;
            this.namespacePrefix = namespacePrefix;
        }

        String declaredPrefix, importedPrefix;
        XQueryNamespacePrefix namespacePrefix;
    }

}
