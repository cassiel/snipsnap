/*                                  n
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
package snipsnap.api.snip;

import org.apache.lucene.search.Hits;
import org.snipsnap.snip.Blog;

import java.sql.Timestamp;
import java.util.List;

/**
 * SnipSpace interface handles all the operations with snips like
 * loading, storing, searching et.c
 *
 * @author Stephan J. Schmidt
 * @version $Id: SnipSpace.java 1697 2004-07-02 09:22:05Z stephan $
 */

public interface SnipSpace  {
  public String getETag();

  // A snip is changed by the user (created, stored)
  public void changed(snipsnap.api.snip.Snip snip);

  public void init();

  public void setETag();

  public int getSnipCount();

  public List getChanged();

  public List getChanged(int count);

  public List getAll();

  public List getSince(Timestamp date);

  public List getByDate(String nameSpace, String start, String end);

  /**
   * A list of Snips, ordered by "hotness", currently
   * viewcount.
   *
   * @param count number of snips in the result
   * @return List of snips, ordered by hotness
   */
  public List getHot(int count);

  public List getComments(snipsnap.api.snip.Snip snip);

  public List getByUser(String login);

  public List getChildren(snipsnap.api.snip.Snip snip);

  public List getChildrenDateOrder(snipsnap.api.snip.Snip snip, int count);

  public List getChildrenModifiedOrder(snipsnap.api.snip.Snip snip, int count);

  public void reIndex();

  public Hits search(String queryString);

  public Blog getBlog();

  public Blog getBlog(String name);

  public boolean exists(String name);

  public snipsnap.api.snip.Snip[] match(String pattern);

  public snipsnap.api.snip.Snip[] match(String start, String end);

  public snipsnap.api.snip.Snip load(String name);

  public void systemStore(List snips);

  public void store(snipsnap.api.snip.Snip snip);

  /**
   * Method with with wich the system can store snips.
   * This methode does not change the mTime, the mUser,
   * reindex the snip or add the snip to the modified list
   *
   * @param snip The snip to store
   */
  public void systemStore(snipsnap.api.snip.Snip snip);

  /**
   * Delays the storage of a snip for some time. Some information
   * in a snip are changeg every view. To not store a snip every
   * time it is viewed, delay the store and wait until some changes
   * are cummulated. Should only be used, when the loss of the
   * changes is tolerable.
   *
   * @param snip Snip to delay for storage
   */
  public void delayedStore(snipsnap.api.snip.Snip snip);

  /**
   * Create a new snip with the specified content.
   * @param name the name of the new snip
   * @param content initial content
   * @return the new copied snip
   */
  public snipsnap.api.snip.Snip create(String name, String content);

  public void remove(snipsnap.api.snip.Snip snip);
}