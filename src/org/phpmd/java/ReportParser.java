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

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 *
 *
 * @author    Manuel Pichler <mapi@phpmd.org>
 * @copyright 2010 Manuel Pichler. All rights reserved.
 * @license   http://www.opensource.org/licenses/bsd-license.php BSD License
 * @version   SVN: $Id$
 * @link      http://phpmd.org
 */
public class ReportParser {

    private Report report = new Report();

    public Report parse(InputStream stream) {
        Document doc = this.createDocument(stream);
        if (doc == null) {
            return new Report();
        }
        return this.parse(doc);
    }

    private Document createDocument(InputStream stream) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException exception) {
                }
                public void error(SAXParseException exception) {
                }
                public void fatalError(SAXParseException exception) {
                }
            });

            return db.parse(stream);
        } catch (Exception ex) {
        }
        return null;
    }

    private Report parse(Document doc) {
        this.report = new Report();

        NodeList nodes = doc.getElementsByTagName("file");
        for (int i = 0; i < nodes.getLength(); i++) {
            this.parse((Element) nodes.item(i));
        }
        return this.report;
    }

    private void parse(Element file) {
        NodeList nodes = file.getElementsByTagName("violation");
        for (int i = 0; i < nodes.getLength(); i++) {
            this.parse((Element) nodes.item(i), file.getAttribute("name"));
        }
    }

    private void parse(Element element, String fileName) {
        RuleViolation rv = new RuleViolation();
        rv.setFileName(fileName);
        rv.setClassName(element.getAttribute("class"));
        rv.setFunctionName(element.getAttribute("function"));
        rv.setMethodName(element.getAttribute("method"));
        rv.setPackageName(element.getAttribute("package"));
        rv.setBeginLine(Integer.parseInt(element.getAttribute("beginline")));
        rv.setEndLine(Integer.parseInt(element.getAttribute("endline")));
        rv.setRule(element.getAttribute("rule"));
        rv.setDescription(element.getTextContent().trim());

        this.report.addRuleViolation(rv);
    }
}
