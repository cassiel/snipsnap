package org.snipsnap.container;

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

import com.tirsen.nanning.config.AspectSystem;
import nanocontainer.StringRegistrationNanoContainer;
import nanocontainer.StringRegistrationNanoContainerImpl;
import nanocontainer.nanning.NanningComponentFactory;
import nanocontainer.reflection.StringToObjectConverter;
import picocontainer.PicoContainer;

public class Components {
  private static PicoContainer container;

  public static synchronized PicoContainer getContainer() {
    if (null == container) {

      AspectSystem as = new AspectSystem();

      NanningComponentFactory fc =  new nanocontainer.nanning.NanningComponentFactory( as );
      PicoContainer pc = new picocontainer.defaults.DefaultPicoContainer(fc);

      StringRegistrationNanoContainer c =
          new StringRegistrationNanoContainerImpl(pc, Components.class.getClassLoader(), new StringToObjectConverter());

      try {
        c.registerComponent("org.snipsnap.snip.storage.JDBCUserStorage");
        c.registerComponent("org.snipsnap.user.UserManager");
        c.registerComponent("org.snipsnap.user.AuthenticationService");
        c.registerComponent("org.snipsnap.user.PasswordService");
        c.registerComponent("org.snipsnap.container.SessionService");
        c.registerComponent("org.radeox.engine.RenderEngine", "org.snipsnap.render.SnipRenderEngine");
        container = c;
        container.instantiateComponents();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return container;
  }

  public static Object getComponent(Class c) {
    return getContainer().getComponent(c);
  }

}