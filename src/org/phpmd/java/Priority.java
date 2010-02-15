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
 *
 *
 * @author    Manuel Pichler <mapi@phpmd.org>
 * @copyright 2010 Manuel Pichler. All rights reserved.
 * @license   http://www.opensource.org/licenses/bsd-license.php BSD License
 * @version   SVN: $Id$
 * @link      http://phpmd.org
 */
public class Priority implements Argument {

    /**
     * The used cli option.
     */
    private static final String OPTION_MINIMUM_PRIORITY = "--minimumpriority";

    /**
     * The lowest possible priority.
     */
    public static final int LOWEST_PRIORITY = 5;

    /**
     * The highest possible priority.
     */
    public static final int HIGHEST_PRIORITY = 1;

    /**
     * The default priority.
     */
    public static final int DEFAULT_PRIORITY = 3;

    /**
     * The currently configured priority.
     */
    private int priority;

    /**
     * Default ctor for this class.
     */
    public Priority() {
        this(DEFAULT_PRIORITY);
    }

    /**
     * Constructs a new priority instance for the given value.
     *
     * @param priority The minimum priority to use.
     */
    public Priority(int priority) {
        this.priority = priority;
    }

    /**
     * Appends the required option and value to the given executable.
     *
     * @param executable The entire executable.
     */
    public Executable toArgument(Executable executable) {
        this.validate();
        return executable
                .addArgument(OPTION_MINIMUM_PRIORITY)
                .addArgument(String.valueOf(this.priority));
    }

    /**
     * Validates that the configured priority value is in the range of allowed
     * priority values. This method will throw an exception when the priority
     * value is not valid.
     *
     * @throws ValidationException If the configured priority is not in the
     *         allowed range.
     */
    protected void validate() throws ValidationException {
        if (this.priority < HIGHEST_PRIORITY) {
            throw new ValidationException(
                    String.format(
                            "The maximum priority cannot smaller than %d.",
                            HIGHEST_PRIORITY
                    )
            );
        }
        if (this.priority > LOWEST_PRIORITY) {
            throw new ValidationException(
                    String.format(
                            "The minimum priority cannot be greater than %d.",
                            LOWEST_PRIORITY
                    )
            );
        }
    }
}
