//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.util.xml.formatter;

import java.util.StringTokenizer;

/**
 * Formatiert einen XML String mit Einrückung.
 *
 * @author roger@micromata.de
 */
public class XmlFormatter
{

  /**
   * The Enum XmlEl.
   */
  public static enum XmlEl
  {

    /**
     * The El open.
     */
    ElOpen,

    /**
     * The El open closed.
     */
    ElOpenClosed,

    /**
     * The El closed.
     */
    ElClosed,

    /**
     * The Xml decl.
     */
    XmlDecl,

    /**
     * The Comment.
     */
    Comment,

    /**
     * The Text.
     */
    Text
  }

  /**
   * The Class XmlTokenizer.
   */
  public static class XmlTokenizer
  {

    /**
     * The input.
     */
    private String input;

    /**
     * The pushback token.
     */
    private String pushbackToken;

    /**
     * The last token.
     */
    private String lastToken;

    /**
     * The stk.
     */
    StringTokenizer stk;

    /**
     * Instantiates a new xml tokenizer.
     *
     * @param input the input
     */
    public XmlTokenizer(String input)
    {
      this.input = input;
      stk = new StringTokenizer(input, "<?!/>--", true);
    }

    /**
     * Parses the text.
     *
     * @return the string
     */
    private String parseText()
    {
      StringBuilder sb = new StringBuilder();
      String tk = null;
      while ((tk = nextToken()) != null) {
        if (tk.equals("<") == true) {
          pushBack(tk);
          return sb.toString();
        }
        sb.append(tk);
      }
      return sb.toString();
    }

    /**
     * Parses the element.
     *
     * @return the string
     */
    private String parseElement()
    {
      StringBuilder sb = new StringBuilder();
      String tk = null;
      while ((tk = nextToken()) != null) {
        if (tk.equals(">") == true) {
          sb.append(tk);
          return sb.toString();
        }
        sb.append(tk);
      }
      return sb.toString();
    }

    /**
     * Parses the comment.
     *
     * @return the string
     */
    private String parseComment()
    {
      StringBuilder sb = new StringBuilder();
      String tk = null;
      while ((tk = nextToken()) != null) {
        if (tk.equals("-") == true) {
          tk = nextToken();
          if (tk.equals("-") == true) {
            tk = nextToken();
            if (tk.equals(">") == true) {
              sb.append("-->");
              return sb.toString();
            } else {
              sb.append("--");
              sb.append(tk);
            }
          } else {
            sb.append("-");
            sb.append(tk);
          }
        } else {
          sb.append(tk);
        }
      }
      return sb.toString();
    }

    /**
     * Next token.
     *
     * @return the string
     */
    private String nextToken()
    {
      if (pushbackToken != null) {
        String ret = pushbackToken;
        pushbackToken = null;
        return ret;
      }
      if (stk.hasMoreTokens() == false) {
        return null;
      }
      return stk.nextToken();
    }

    /**
     * Push back.
     *
     * @param tk the tk
     */
    private void pushBack(String tk)
    {
      if (this.pushbackToken == null) {
        this.pushbackToken = tk;
      } else {
        this.pushbackToken = pushbackToken + tk;
      }
    }

    /**
     * return null, if non.
     *
     * @return the xml el
     */
    public XmlEl nextElem()
    {
      String tk = nextToken();
      if (tk == null) {
        return null;
      }
      if (tk.equals("<") == false) {
        pushBack(tk);
        lastToken = parseText();
        return XmlEl.Text;
      }
      tk = nextToken();
      if (tk.equals("!") == true) {
        pushBack("<!");
        lastToken = parseComment();
        return XmlEl.Comment;
      } else if (tk.equals("?") == true) {
        pushBack("<?");
        lastToken = parseElement();
        return XmlEl.XmlDecl;
      } else {
        pushBack("<");
        pushBack(tk);
        lastToken = parseElement();
        if (lastToken.startsWith("</") == true) {
          return XmlEl.ElClosed;
        } else if (lastToken.endsWith("/>") == true) {
          return XmlEl.ElOpenClosed;
        } else {
          return XmlEl.ElOpen;
        }
      }
    }

    public String getLastToken()
    {
      return lastToken;
    }

    public String getInput()
    {
      return input;
    }
  }

  /**
   * Indent.
   *
   * @param s the s
   * @param count the count
   * @return the string
   */
  public static String indent(String s, int count)
  {
    if (count == 0) {
      return "";
    }
    if (count == 1) {
      return s;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; ++i) {
      sb.append(s);
    }
    return sb.toString();
  }

  /**
   * Format a XML string
   * 
   * if an error occours, returns the original xml string.
   *
   * @param xml the xml
   * @param indentString String to use indent
   * @return the string
   */
  public static String formatXml(String xml, String indentString)
  {
    try {
      if (xml == null) {
        return xml;
      }

      return formatXmlInternal(xml, indentString);
    } catch (RuntimeException ex) { // NOSONAR "Illegal Catch" framework
      // now warn here
      return xml;
    }
  }

  /**
   * Format xml internal.
   *
   * @param xml the xml
   * @param indentString the indent string
   * @return the string
   */
  public static String formatXmlInternal(String xml, String indentString)
  {
    StringBuilder sb = new StringBuilder();
    XmlTokenizer xt = new XmlTokenizer(xml);
    int curIndent = 0;
    XmlEl el;
    XmlEl lastEl = null;
    while ((el = xt.nextElem()) != null) {
      switch (el) {
        case Comment:
        case Text:
          sb.append(xt.getLastToken());
          break;
        case ElOpen:
          sb.append("\n").append(indent(indentString, curIndent)).append(xt.getLastToken());
          ++curIndent;
          break;
        case ElClosed:
          --curIndent;
          if (lastEl == XmlEl.Text || lastEl == XmlEl.Comment) {
            sb.append(xt.getLastToken());
          } else {
            sb.append("\n").append(indent(indentString, curIndent)).append(xt.getLastToken());
          }
          break;
        case XmlDecl:
        case ElOpenClosed:
          sb.append("\n").append(indent(indentString, curIndent)).append(xt.getLastToken());
          break;

      }
      lastEl = el;
    }
    return sb.toString();
  }
}
