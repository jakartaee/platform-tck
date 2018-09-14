/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jsf.spec.navigation;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;

import java.util.Locale;
import java.io.IOException;

public class TestViewHandler extends ViewHandler {

  private ViewHandler delegate;

  public TestViewHandler(ViewHandler delegate) {

    this.delegate = delegate;

  } // END TestViewHandler

  /**
   * <p>
   * Returns an appropriate {@link java.util.Locale} to use for this and
   * subsequent requests for the current client.
   * </p>
   *
   * @param context
   *          {@link javax.faces.context.FacesContext} for the current request
   *
   * @throws NullPointerException
   *           if <code>context</code> is <code>null</code>
   */
  public Locale calculateLocale(FacesContext context) {

    return delegate.calculateLocale(context);

  } // END calculateLocale

  /**
   * <p>
   * Return an appropriate <code>renderKitId</code> for this and subsequent
   * requests from the current client.
   * </p>
   * <p/>
   * <p>
   * The default return value is
   * {@link javax.faces.render.RenderKitFactory#HTML_BASIC_RENDER_KIT}.
   * </p>
   *
   * @param context
   *          {@link javax.faces.context.FacesContext} for the current request
   *
   * @throws NullPointerException
   *           if <code>context</code> is <code>null</code>
   */
  public String calculateRenderKitId(FacesContext context) {

    return delegate.calculateRenderKitId(context);

  } // END calculateRenderKitId

  /**
   * <p>
   * Create and return a new {@link javax.faces.component.UIViewRoot} instance
   * initialized with information from the argument <code>FacesContext</code>
   * and <code>viewId</code>.
   * </p>
   * <p/>
   * <p>
   * If there is an existing <code>ViewRoot</code> available on the
   * {@link javax.faces.context.FacesContext}, this method must copy its
   * <code>locale</code> and <code>renderKitId</code> to this new view root. If
   * not, this method must call {@link #calculateLocale} and
   * {@link #calculateRenderKitId}, and store the results as the values of the
   * <code>locale</code> and <code>renderKitId</code>, proeprties, respectively,
   * of the newly created <code>UIViewRoot</code>.
   * </p>
   *
   * @throws NullPointerException
   *           if <code>context</code> is <code>null</code>
   */
  public UIViewRoot createView(FacesContext context, String viewId) {

    return delegate.createView(context, viewId);

  } // END createView

  /**
   * <p>
   * Return a URL suitable for rendering (after optional encoding performed by
   * the <code>encodeActionURL()</code> method of
   * {@link javax.faces.context.ExternalContext}) that selects the specified
   * view identifier.
   * </p>
   *
   * @param context
   *          {@link javax.faces.context.FacesContext} for this request
   * @param viewId
   *          View identifier of the desired view
   *
   * @throws IllegalArgumentException
   *           if <code>viewId</code> is not valid for this
   *           <code>ViewHandler</code>.
   * @throws NullPointerException
   *           if <code>context</code> or <code>viewId</code> is
   *           <code>null</code>.
   */
  public String getActionURL(FacesContext context, String viewId) {

    return delegate.getActionURL(context, viewId);

  } // END getActionURL

  /**
   * <p>
   * Return a URL suitable for rendering (after optional encoding perfomed by
   * the <code>encodeResourceURL()</code> method of
   * {@link javax.faces.context.ExternalContext}) that selects the specifed web
   * application resource. If the specified path starts with a slash, it must be
   * treated as context relative; otherwise, it must be treated as relative to
   * the action URL of the current view.
   * </p>
   *
   * @param context
   *          {@link javax.faces.context.FacesContext} for the current request
   * @param path
   *          Resource path to convert to a URL
   *
   * @throws IllegalArgumentException
   *           if <code>viewId</code> is not valid for this
   *           <code>ViewHandler</code>.
   * @throws NullPointerException
   *           if <code>context</code> or <code>path</code> is
   *           <code>null</code>.
   */
  public String getResourceURL(FacesContext context, String path) {

    return delegate.getResourceURL(context, path);

  } // END getResourceURL

  /**
   * <p>
   * Perform whatever actions are required to render the response view to the
   * response object associated with the current
   * {@link javax.faces.context.FacesContext}.
   * </p>
   *
   * @param context
   *          {@link javax.faces.context.FacesContext} for the current request
   * @param viewToRender
   *          the view to render
   *
   * @throws java.io.IOException
   *           if an input/output error occurs
   * @throws NullPointerException
   *           if <code>context</code> or <code>viewToRender</code> is
   *           <code>null</code>
   * @throws javax.faces.FacesException
   *           if a servlet error occurs
   */
  public void renderView(FacesContext context, UIViewRoot viewToRender)
      throws IOException, FacesException {

    // no-op

  } // END renderView

  /**
   * <p>
   * Perform whatever actions are required to restore the view associated with
   * the specified {@link javax.faces.context.FacesContext} and
   * <code>viewId</code>. It may delegate to the <code>restoreView</code> of the
   * associated {@link javax.faces.application.StateManager} to do the actual
   * work of restoring the view. If there is no available state for the
   * specified <code>viewId</code>, return <code>null</code>.
   * </p>
   *
   * @param context
   *          {@link javax.faces.context.FacesContext} for the current request
   * @param viewId
   *          the view identifier for the current request
   *
   * @throws NullPointerException
   *           if <code>context</code> is <code>null</code>
   * @throws javax.faces.FacesException
   *           if a servlet error occurs
   */
  public UIViewRoot restoreView(FacesContext context, String viewId) {

    return null;

  } // END restoreView

  /**
   * <p>
   * Take any appropriate action to either immediately write out the current
   * state information (by calling
   * {@link javax.faces.application.StateManager#writeState}, or noting where
   * state information should later be written.
   * </p>
   *
   * @param context
   *          {@link javax.faces.context.FacesContext} for the current request
   *
   * @throws java.io.IOException
   *           if an input/output error occurs
   * @throws NullPointerException
   *           if <code>context</code> is <code>null</code>
   */
  public void writeState(FacesContext context) throws IOException {

    delegate.writeState(context);

  } // END writeState

  // ---------------------------------------------------------- Public Methods

  public ViewHandler getDelegate() {

    return delegate;

  } // END getDelegate

  @Override
  public String getWebsocketURL(FacesContext context, String channel) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
