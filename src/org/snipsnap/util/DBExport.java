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
package org.snipsnap.util;

import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.DOMException;
import org.snipsnap.user.User;
import org.snipsnap.user.UserManager;
import org.snipsnap.config.AppConfiguration;
import org.snipsnap.config.Configuration;
import org.snipsnap.app.Application;
import org.snipsnap.snip.Snip;
import org.snipsnap.snip.SnipSpace;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DBExport {
  public static void main(String args[]) {
    if(args.length < 3) {
      System.out.println("usage: DBExport <application>");
      System.exit(-1);
    }

    Configuration serverConfig = null;
    try {
      serverConfig = new Configuration("./conf/server.conf");
    } catch (IOException e) {
      System.out.println("Unable to load server config: "+e);
      System.exit(-1);
    }
    Application app = Application.get();
    AppConfiguration config = null;
    try {
      config = new AppConfiguration(
           new File(serverConfig.getProperty(Configuration.SERVER_WEBAPP_ROOT)+args[0]+"/application.conf"));
    } catch (IOException e) {
      System.out.println("Unable to load application config: "+e);
      System.exit(-1);
    }
    app.setConfiguration(config);

    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document doc = documentBuilder.newDocument();

      Element root = (Element)doc.createElement("snipspace");
      doc.appendChild(root);
      Iterator it = SnipSpace.getInstance().getAll().iterator();
      while(it.hasNext()) {
        Snip snip = (Snip)it.next();
        Element snipElement = getXMLInstance(snip, doc);
        if(snipElement != null) {
          root.appendChild(snipElement);
        }
      }
    } catch (Exception e) {
      System.err.println("error accessing xml parser");
      System.exit(-1);
    }

    System.exit(0);
  }

  private static Object[] args = new Object[]{};


  private static Element getXMLInstance(Snip snip, Document doc) {
    try {
      PropertyDescriptor[] properties = Introspector.getBeanInfo(Snip.class).getPropertyDescriptors();
      Element root = doc.createElement("snip");
      for (int i = properties.length - 1; i >= 0; i--) {
        String name = properties[i].getName();
        Method method = properties[i].getReadMethod();
        Class classType = properties[i].getPropertyType();
        System.out.println("snip: "+snip+", property: " + name + ", classType=" + classType);
        Object value = null;

        if (method != null) {
          try {
            value = method.invoke(snip, args);
          } catch (IllegalAccessException e) {
            System.err.println("could not access property " + name+": "+e);
          } catch (IllegalArgumentException e) {
            System.err.println("illegal argument for reading property " + name+", "+e);
          } catch (InvocationTargetException e) {
            System.err.println("invocation target error for property " + name+", "+e);
          }
        }
        System.err.println("new element: " + name + "='" + value + "'");
        Element prop = doc.createElement(name);
        if (value != null) {
          System.err.println("adding property " + name + " with value '" + value.toString() + "'");
          prop.appendChild(doc.createTextNode(value.toString()));
          root.appendChild(prop);
        }
      }
      return root;
    } catch (IntrospectionException e) {
      return null;
    }
  }
}
