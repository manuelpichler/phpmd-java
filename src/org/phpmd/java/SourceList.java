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
import java.util.HashSet;
import java.util.Set;
import org.phpmd.java.util.Argument;
import org.phpmd.java.util.Executable;

/**
 * This class represents a list of source files and/or directories.
 *
 * @author    Manuel Pichler <mapi@phpmd.org>
 * @copyright 2010 Manuel Pichler. All rights reserved.
 * @license   http://www.opensource.org/licenses/bsd-license.php BSD License
 * @version   SVN: $Id$
 * @link      http://phpmd.org
 */
public class SourceList implements Argument {

    /**
     * All input sources.
     */
    private Set<Source> inputs = new HashSet<Source>();

    /**
     * Adds a input file and/or directory.
     *
     * @param source A phpmd input source.
     */
    public void add(Source source) {
        this.inputs.add(source);
    }

    /**
     * Appends all source files to the given executable.
     *
     * @param executable The context executable.
     *
     * @return A prepared executable.
     */
    public Executable toArgument(Executable executable) {
        this.validate();
        
        String list = "";
        for (Source input : this.inputs) {
            list += "," + input.getFileOrDirectory();
        }
        return executable.addArgument(list.substring(1));
    }

    /**
     * Validates all configured sources.
     *
     * @throws ValidationException When one of the source files does not not
     *         exist or no input was specified.
     */
    protected void validate() {
        if (this.inputs.size() == 0) {
            throw new ValidationException("No input files or directories specified.");
        }
        for (Source input : this.inputs) {
            input.validate();
        }
    }
}
