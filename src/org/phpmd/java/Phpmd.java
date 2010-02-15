/**
 * This file is part of PHPMD Java binding project.
 *
 * Copyright (c) 2010, Manuel Pichler <mapi@phpmd.org>.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   * Neither the name of Manuel Pichler nor the names of his
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @author    Manuel Pichler <mapi@phpmd.org>
 * @copyright 2010 Manuel Pichler. All rights reserved.
 * @license   http://www.opensource.org/licenses/bsd-license.php BSD License
 * @version   SVN: $Id$
 * @link      http://phpmd.org
 */

package org.phpmd.java;

import org.phpmd.java.util.DefaultExecutable;
import org.phpmd.java.util.NonBlockingExecutable;
import org.phpmd.java.util.Executable;
import java.io.File;
import org.phpmd.java.util.ExecutableUtil;

/**
 *
 *
 * @author    Manuel Pichler <mapi@phpmd.org>
 * @copyright 2010 Manuel Pichler. All rights reserved.
 * @license   http://www.opensource.org/licenses/bsd-license.php BSD License
 * @version   SVN: $Id$
 * @link      http://phpmd.org
 */
public class Phpmd {

    public static final String SCRIPT_NAME = "phpmd";

    private Executable executable = null;

    private ReportFormat format = new ReportFormat();

    private Priority priority = new Priority();

    private RuleSets ruleSets = new RuleSets();

    private SourceList sourceList = new SourceList();

    private boolean nonBlocking = false;

    public Phpmd() {
        this(ExecutableUtil.findExecutableOnUsersPath(SCRIPT_NAME));
    }
    
    public Phpmd(Executable executable) {
        this.executable = executable;
    }

    public Report run() {

        this.setReportFormat(new ReportFormat(ReportFormat.FORMAT_XML));
        this.setBlocking();

        ReportExecutable script = new ReportExecutable(this.executable);
        this.prepareExecutable(script).exec();

        return script.getReport();
    }

    public void run(File file) {
        this.run(new ReportFile(file));
    }

    public void run(ReportFile output) {
        Executable script = new DefaultExecutable(this.executable);
        script = this.prepare(this.prepareExecutable(script, output));
        script.exec();
    }

    private Executable prepare(Executable executable) {
        if (this.nonBlocking) {
            return new NonBlockingExecutable(executable);
        }
        return executable;
    }
    
    public void addSource(String fileOrDirectory) {
        this.addSource(new File(fileOrDirectory));
    }

    public void addSource(File fileOrDirectory) {
        this.addSource(new Source(fileOrDirectory));
    }

    public void addSource(Source inputSource) {
        this.sourceList.add(inputSource);
    }

    public void addRuleSet(String ruleSet) {
        this.addRuleSet(new RuleSet(ruleSet));
    }

    public void addRuleSet(RuleSet ruleSet) {
        this.ruleSets.add(ruleSet);
    }

    public void setReportFormat(String reportFormat) {
        this.setReportFormat(new ReportFormat(reportFormat));
    }

    public void setReportFormat(ReportFormat reportFormat) {
        this.format = reportFormat;
    }

    public void setMinimumPriority(int priority) {
        this.setMinimumPriority(new Priority(priority));
    }

    public void setMinimumPriority(Priority priority) {
        this.priority = priority;
    }

    public void setBlocking() {
        this.nonBlocking = false;
    }

    public void setNonBlocking() {
        this.nonBlocking = true;
    }
    
    private Executable prepareExecutable(Executable script, ReportFile report) {
        return this.prepareExecutable(script).addArgument(report);
    }

    private Executable prepareExecutable(Executable script) {
        return script.addArgument(this.sourceList)
                .addArgument(this.format)
                .addArgument(this.ruleSets)
                .addArgument(this.priority);
    }
}
