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

import org.phpmd.java.util.ValidationException;
import org.phpmd.java.util.Argument;
import org.phpmd.java.util.Executable;

/**
 * This class represents a report format that should be used for the generated
 * PHPMD report file.
 *
 * @author    Manuel Pichler <mapi@phpmd.org>
 * @copyright 2010 Manuel Pichler. All rights reserved.
 * @license   http://www.opensource.org/licenses/bsd-license.php BSD License
 * @version   SVN: $Id$
 * @link      http://phpmd.org
 */
public class ReportFormat implements Argument {

    /**
     * Build-in report formats.
     */
    public static final String FORMAT_XML = "xml",
                               FORMAT_HTML = "html",
                               FORMAT_TEXT = "text";

    /**
     * The default report format.
     */
    public static final String DEFAULT_FORMAT = FORMAT_XML;

    /**
     * The report output format.
     */
    private String format;

    /**
     * Default ctor for this class.
     */
    public ReportFormat() {
        this(DEFAULT_FORMAT);
    }

    /**
     * Constructs a new report format instance.
     *
     * @param format The report output format.
     */
    public ReportFormat(String format) {
        this.format = format;
    }

    /**
     * Returns the used report output format.
     *
     * @return String
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * Appends the required cli-command string tokens to the given executable
     * instance.
     *
     * @param executable Context executable.
     *
     * @return The prepared command list.
     */
    public Executable toArgument(Executable executable) {
        this.validate();
        return executable.addArgument(this.format);
    }

    /**
     * Validates that the value of this object can safely be used in the phpmd
     * command line string. This method will throw a {@link ValidationException}
     * exception when the specified report format is <b>null</b> or an empty
     * string.
     *
     * @throws ValidationException When the spefieid report format is an empty
     *         string or <b>null</b>.
     */
    protected void validate() {
        if (this.format == null || this.format.trim().equals("")) {
            throw new ValidationException("The report format cannot be null or empty.");
        }
    }
}
