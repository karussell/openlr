/**
* Licensed to the TomTom International B.V. under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  TomTom International B.V.
* licenses this file to you under the Apache License, 
* Version 2.0 (the "License"); you may not use this file except 
* in compliance with the License.  You may obtain a copy of the License at
* 
* http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

/**
 *  Copyright (C) 2009-2012 TomTom International B.V.
 *
 *   TomTom (Legal Department)
 *   Email: legal@tomtom.com
 *
 *   TomTom (Technical contact)
 *   Email: openlr@tomtom.com
 *
 *   Address: TomTom International B.V., Oosterdoksstraat 114, 1011DK Amsterdam,
 *   the Netherlands
 */
package openlr.mapviewer.utils.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a type safe manager for observer lists.
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 * @param <O>
 *            The type of observers that can be managed by this instance.
 * @param <S>
 *            The type of object that is delivered to the observers in the
 *            notification message.
 */
public class ObserverManager<O extends Observer<S>, S> {

    /**
     * Stores the currently active listeners
     */
    private final List<O> observers = new ArrayList<O>();

    /**
     * The observable
     */
    private final S theObservable;

    /**
     * Creates a new observer manager
     * 
     * @param observable
     *            The object reference that is delivered to the observers in
     *            case of a change
     */
    public ObserverManager(final S observable) {
        theObservable = observable;
    }

    /**
     * Adds an observer to the list. This will lead to a hard reference from
     * this object to the listener. For memory purposes clients should therefore
     * take care to remove the observer properly if they are intended to shut
     * down!
     * 
     * @param observer
     *            The observer to add
     */
    public final void addObserver(final O observer) {
        observers.add(observer);
    }

    /**
     * Removes a registered observers.
     * 
     * @param observer
     *            The observer instance to remove
     */
    public final void removeObserver(final O observer) {
        observers.remove(observer);
    }

    /**
     * Fires the event that informs all observers about the changed data
     */
    public final void notifyObservers() {
        for (O listener : observers) {
            listener.update(theObservable);
        }
    }
}
