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
package org.snipsnap.config;

import org.radeox.util.logging.Logger;
import org.snipsnap.container.Components;
import org.snipsnap.notification.Consumer;
import org.snipsnap.notification.Message;
import org.snipsnap.notification.MessageService;
import org.snipsnap.snip.Snip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Manages configurations of this web application (different contexts)
 *
 * @author Matthias L. Jugel
 * @version $Id$
 */
public class ConfigurationManager implements Consumer {
  private static ConfigurationManager configManager = null;

  private String oid;
  private Configuration config;

  public static ConfigurationManager getInstance() {
    if (null == configManager) {
      configManager = new ConfigurationManager();
    }
    return configManager;
  }

  private ConfigurationManager() {
  }

  public Configuration addConfiguration(String oid, Configuration config) {
    if(config.isInstalled()) {
      MessageService service = (MessageService)Components.getComponent(MessageService.class);
      service.register(this);
    }
    this.oid = oid;
    this.config = config;
    return config;
  }

  public void removeConfiguration(String oid) {
    if(oid != null && oid.equals(this.oid)) {
      this.oid = null;
      this.config = null;
    }
  }

  public Configuration getConfiguration(String oid) {
    return (oid != null && oid.equals(this.oid) ? config : null);
  }

  public String checkForPrefix(String prefix) {
    return (prefix != null && config != null && prefix.equals(config.getPrefix()) ? oid : null);
  }

  public Iterator getOids() {
    return (oid != null ? Arrays.asList(new String[] { oid }) : Arrays.asList(new String[] {})).iterator();
  }

  public void consume(Message messsage) {
    if(Message.SNIP_MODIFIED.equals(messsage.getType())) {
      Snip snip = (Snip)messsage.getValue();
      if("SnipSnap/config".equals(snip.getName())) {
        try {
          Logger.log("reloading config for: "+oid);
          config.load(new ByteArrayInputStream(snip.getContent().getBytes()));
        } catch (IOException e) {
          System.err.println("ConfigurationManager: unable to reload configuration: "+e);
          e.printStackTrace();
        }
      }
    }
  }
}