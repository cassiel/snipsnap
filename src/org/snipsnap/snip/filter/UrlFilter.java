/*
 * This file is part of "SnipSnap Wiki/Weblog".
 *
 * Copyright (c) 2002 Stephan J. Schmidt, Matthias L. Jugel
 * All Rights Reserved.
 *
 * Please visit http://snipsnap.org/ for updates and contact.
 *
 * --LICENSE NOTICE--
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * --LICENSE NOTICE--
 */

package org.snipsnap.snip.filter;

import org.snipsnap.snip.filter.regex.RegexTokenFilter;
import org.snipsnap.snip.SnipLink;
import org.snipsnap.snip.Snip;
import org.apache.oro.text.regex.MatchResult;

/*
 * UrlFilter finds http:// style URLs in its input and transforms this
 * to &lt;a href="url"&gt;url&lt;/a&gt;
 *
 * @author stephan
 * @team sonicteam
 * @version $Id$
 */
public class UrlFilter extends RegexTokenFilter {

  public UrlFilter() {
    super("(?:[^\"=]|^)((http|ftp)s?://(%[[:digit:]A-Fa-f][[:digit:]A-Fa-f]|[-_.!~*';/?:@#&=+$,[:alnum:]])+)");
  };

  public void handleMatch(StringBuffer buffer, MatchResult result, Snip snip) {
    buffer.append("<span class=\"nobr\">");
    buffer.append(SnipLink.createImage("external-link", "&gt;&gt;"));
    buffer.append("<a href=\"").append(result.group(1)).append("\">");
    buffer.append(EscapeFilter.escape(result.group(1).charAt(0)));
    buffer.append(result.group(1).substring(1));
    buffer.append("</a></span>");
  }
}
