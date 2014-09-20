/*
 * Copyright (C) 2014 Sebastien Diot.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blockwithme.util.client;

import java.util.Map;

import com.blockwithme.util.base.WeakKeyMap;

/** Implements WeakKeyMap<KEY, VALUE> using JavaScript Harmony WeakMap.*/
final class RealWeakKeyMap<KEY, VALUE> implements
        WeakKeyMap<KEY, VALUE> {

    private native void init()
    /*-{
    		this.map = new window.WeakMap();
    		// Ugly hack. It seems GWT does not like methods called "delete" ...
    		this.map.rmv = this.map['delete'];
    }-*/;

    /** Constructor */
    public RealWeakKeyMap() {
        init();
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.WeakKeyMap#clear()
     */
    @Override
    public native void clear()
    /*-{
    		this.map.clear();
    }-*/;

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.WeakKeyMap#containsKey(java.lang.Object)
     */
    @Override
    public native boolean containsKey(final Object key)
    /*-{
    		return this.map.has(key);
    }-*/;

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.WeakKeyMap#remove(java.lang.Object)
     */
    @Override
    public native VALUE remove(final Object key)
    /*-{
    		var result = this.map.get(key);
    		this.map.rmv(key);
    		return result;
    }-*/;

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.WeakKeyMap#get(java.lang.Object)
     */
    @Override
    public native VALUE get(final Object key)
    /*-{
    		return this.map.get(key);
    }-*/;

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.WeakKeyMap#put(java.lang.Object, java.lang.Object)
     */
    private native VALUE put2(final KEY key, final VALUE value)
    /*-{
    		var result = this.map.get(key);
    		this.map.set(key, value);
    		return result;
    }-*/;

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.WeakKeyMap#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public VALUE put(final KEY key, final VALUE value) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (value == null) {
            return remove(key);
        }
        return put2(key, value);
    }

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.WeakKeyMap#put(java.lang.Object, java.lang.Object)
     */
    private native void put3(final KEY key, final VALUE value)
    /*-{
    		if ((value !== undefined) && (value !== null)) {
    			this.map.set(key, value);
    		} else {
    			this.map.rmv(key);
    		}
    }-*/;

    /* (non-Javadoc)
     * @see com.blockwithme.util.base.WeakKeyMap#putAll(java.util.Map)
     */
    @Override
    public void putAll(final Map<? extends KEY, ? extends VALUE> m) {
        for (final java.util.Map.Entry<? extends KEY, ? extends VALUE> e : m
                .entrySet()) {
            final KEY key = e.getKey();
            if (key == null) {
                throw new NullPointerException("key");
            }
            final VALUE value = e.getValue();
            put3(key, value);
        }
    }
}