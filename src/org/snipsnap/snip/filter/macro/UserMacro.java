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
/*
 * Macro that displays all Snips by user
 *
 * @author stephan
 * @version $Id$
 */

package org.snipsnap.snip.filter.macro;

import org.snipsnap.snip.Snip;
import org.snipsnap.user.UserManager;

import java.io.IOException;
import java.io.Writer;

public class UserMacro extends ListOutputMacro {
  public String getName() {
    return "list-of-users";
  }

  public String getDescription() {
    return "Displays a list of all users.";
  }

  public void execute(Writer writer, MacroParameter params)
    throws IllegalArgumentException, IOException {
    String type = null;
    boolean showSize = true;
    if (params != null) {
      if (params.getLength() > 0) {
        type = params.get("0");
      }
      showSize = !(params.getLength() > 1 && "nosize".equals(params.get("1")));
    }
    if (params == null || params.getLength() <= 2) {
      output(writer, "All Users:", UserManager.getInstance().getAll(), "no users. not very popular ;-)", type, showSize);
    } else {
      throw new IllegalArgumentException("Number of arguments does not match");
    }
  }
}
