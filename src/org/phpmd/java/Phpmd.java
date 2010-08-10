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

import java.io.File;
import org.phpmd.java.util.DefaultExecutable;
import org.phpmd.java.util.NonBlockingExecutable;
import org.phpmd.java.util.Executable;
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

    /**
     * Default name of the phpmd command line executable without an OS specific
     * file extension.
     */
    public static final String SCRIPT_NAME = "phpmd";

    /**
     * Valid exit codes returned by the phpmd command line executable.
     */
    public static final Integer EXIT_CODE_SUCCESS = 0,
                                EXIT_CODE_VIOLATION = 2;

    /**
     * The wrapped executable instance.
     */
    private Executable executable = null;

    /**
     * Default report format/output.
     */
    private ReportFormat format = new ReportFormat();

    /**
     * Specified minimum priority required for rule violations to occure in the
     * generated report.
     */
    private Priority priority = new Priority();

    /**
     * Collection holding the rule sets configured for the phpmd run.
     */
    private RuleSets ruleSets = new RuleSets();

    /**
     * Collection holding the input source files.
     */
    private SourceList sourceList = new SourceList();

    /**
     * Should phpmd be run in a separate thread?
     */
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

    public Integer run(File file) {
        return this.run(new ReportFile(file));
    }

    public Integer run(ReportFile output) {
        Executable script = new DefaultExecutable(this.executable);
        script = this.prepare(this.prepareExecutable(script, output));
        script.exec();

        return script.exitCode();
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

    /**
     * Adds a rule set that will be applied against the checked source code.
     *
     * @param ruleSet A rule set instance.
     */
    public void addRuleSet(RuleSet ruleSet) {
        this.ruleSets.add(ruleSet);
    }

    /**
     * Sets the report format to use.
     *
     * @param reportFormat The report format to use.
     */
    public void setReportFormat(String reportFormat) {
        this.setReportFormat(new ReportFormat(reportFormat));
    }

    /**
     * Sets the report format to use.
     *
     * @param reportFormat A report format instance.
     */
    public void setReportFormat(ReportFormat reportFormat) {
        this.format = reportFormat;
    }

    /**
     * Sets the required minimum priority for rules to occure in the generated
     * violation report. A valid priority value is in an integer in a range
     * between 1 and 5.
     *
     * @param priority The minimum priority value.
     */
    public void setMinimumPriority(int priority) {
        this.setMinimumPriority(new Priority(priority));
    }

    /**
     * Sets the required minimum priority for rules to occure in the generated
     * violation report.
     *
     * @param priority A priority instance.
     */
    public void setMinimumPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Configures this phpmd instance to run blocking. This means phpmd will not
     * run in a separate thread.
     */
    public void setBlocking() {
        this.nonBlocking = false;
    }

    /**
     * Configures this phpmd instance to run in non blocking mode. Non blocking
     * means that phpmd will run in a separate thread.
     */
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
                .addArgument(this.priority)
                .addRegularExitCode(EXIT_CODE_SUCCESS)
                .addRegularExitCode(EXIT_CODE_VIOLATION);
    }
}
