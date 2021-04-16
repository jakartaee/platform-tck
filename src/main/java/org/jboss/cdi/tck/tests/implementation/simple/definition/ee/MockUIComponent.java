/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.implementation.simple.definition.ee;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.el.ValueBinding;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.FacesListener;
import jakarta.faces.render.Renderer;

public class MockUIComponent extends UIComponent {

    @Override
    protected void addFacesListener(FacesListener arg0) {

    }

    @Override
    public void broadcast(FacesEvent arg0) throws AbortProcessingException {

    }

    @Override
    public void decode(FacesContext arg0) {

    }

    @Override
    public void encodeBegin(FacesContext arg0) throws IOException {

    }

    @Override
    public void encodeChildren(FacesContext arg0) throws IOException {

    }

    @Override
    public void encodeEnd(FacesContext arg0) throws IOException {

    }

    @Override
    public UIComponent findComponent(String arg0) {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public List<UIComponent> getChildren() {
        return null;
    }

    @Override
    public String getClientId(FacesContext arg0) {
        return null;
    }

    @Override
    protected FacesContext getFacesContext() {
        return null;
    }

    @Override
    protected FacesListener[] getFacesListeners(Class arg0) {
        return null;
    }

    @Override
    public UIComponent getFacet(String arg0) {
        return null;
    }

    @Override
    public Map<String, UIComponent> getFacets() {
        return null;
    }

    @Override
    public Iterator<UIComponent> getFacetsAndChildren() {
        return null;
    }

    @Override
    public String getFamily() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public UIComponent getParent() {
        return null;
    }

    @Override
    protected Renderer getRenderer(FacesContext arg0) {
        return null;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    public boolean getRendersChildren() {
        return false;
    }

    @Override
    public ValueBinding getValueBinding(String arg0) {
        return null;
    }

    @Override
    public boolean isRendered() {
        return false;
    }

    @Override
    public void processDecodes(FacesContext arg0) {

    }

    @Override
    public void processRestoreState(FacesContext arg0, Object arg1) {

    }

    @Override
    public Object processSaveState(FacesContext arg0) {
        return null;
    }

    @Override
    public void processUpdates(FacesContext arg0) {

    }

    @Override
    public void processValidators(FacesContext arg0) {

    }

    @Override
    public void queueEvent(FacesEvent arg0) {

    }

    @Override
    protected void removeFacesListener(FacesListener arg0) {

    }

    @Override
    public void setId(String arg0) {

    }

    @Override
    public void setParent(UIComponent arg0) {

    }

    @Override
    public void setRendered(boolean arg0) {

    }

    @Override
    public void setRendererType(String arg0) {

    }

    @Override
    public void setValueBinding(String arg0, ValueBinding arg1) {

    }

    public boolean isTransient() {
        return false;
    }

    public void restoreState(FacesContext arg0, Object arg1) {

    }

    public Object saveState(FacesContext arg0) {
        return null;
    }

    public void setTransient(boolean arg0) {

    }

}
