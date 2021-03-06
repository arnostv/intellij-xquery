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

package org.intellij.xquery.runner.state.run;

import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.util.ProgramParametersUtil;

/**
 * User: ligasgr
 * Date: 18/11/13
 * Time: 13:47
 */
public class ModuleValidator {

    public void validate(XQueryRunConfiguration xQueryRunConfiguration) throws RuntimeConfigurationException {
        final RunConfigurationModule configurationModule = xQueryRunConfiguration.getConfigurationModule();
        configurationModule.checkForWarning();
        ProgramParametersUtil.checkWorkingDirectoryExist(xQueryRunConfiguration, xQueryRunConfiguration.getProject(), configurationModule.getModule());
    }
}
