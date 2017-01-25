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
package openlr.mapviewer.utils;

import java.io.Writer;
import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.rewrite.RewriteAppender;
import org.apache.log4j.rewrite.RewritePolicy;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * The Class LogTracker.
 */
public class LogTracker {

	/**
     * A filter for suppressing all log output
     */
    private static final SuppressFilter SUPPRESS_FILTER = new SuppressFilter();

    /** The Constant LAYOUT. */
	private static final Layout LAYOUT = new PatternLayout(
			"%d [%t] %-5p %c - %m%n");

	/** The Constant REWRITE_APPENDER_NAME. */
	private static final String REWRITE_APPENDER_NAME = "RewriteAppender";

	/** The writer. */
	private Writer writer;

	/** The old level. */
	private Level oldLevel;

	/** The additional appender. */
	private Appender additionalAppender;

	/** The old appenders. */
	private RewriteAppender oldAppender;
	
    /**
     * Creates a new log tracker. The log output will be passed to the given
     * {@code logReceiver}.
     * 
     * @param logReceiver
     *            The writer receiving the log, must not be {@code null}
     */
    public LogTracker(final Writer logReceiver) {
        writer = logReceiver;
    }

	/**
	 * Prepare log tracking.
	 */
	@SuppressWarnings("unchecked")
	public final void prepareLogTracking() {
		Logger rootLog = LogManager.getRootLogger();
		// store old level
		oldLevel = rootLog.getLevel(); 
		additionalAppender = new WriterAppender(LAYOUT, writer);
		// prevent existing appenders from logging more than expected with the
		// old level
		oldAppender = new RewriteAppender();
		oldAppender.setName(REWRITE_APPENDER_NAME);
		oldAppender.setRewritePolicy(new RepressAdditionalLoggingPolicy(
				oldLevel));
		Enumeration<Appender> oldApps = rootLog.getAllAppenders();
		while (oldApps.hasMoreElements()) {
			Appender app = oldApps.nextElement();
			app.addFilter(SUPPRESS_FILTER);
		}

		// add old and new appenders
		rootLog.addAppender(oldAppender);
		rootLog.addAppender(additionalAppender);
		// set new level
		rootLog.setLevel(Level.DEBUG);
	}

	/**
	 * Reset log tracking.
	 */
	@SuppressWarnings("unchecked")
	public final void resetLogTracking() {
		Logger rootLog = LogManager.getRootLogger();
		// restore level
		if (oldLevel != null) {
			rootLog.setLevel(oldLevel);
		}
		// remove additional appender
		if (additionalAppender != null) {
			rootLog.removeAppender(additionalAppender);
		}
		// restore old appenders
		RewriteAppender old = (RewriteAppender) rootLog
				.getAppender(REWRITE_APPENDER_NAME);
		if (old != null) {
			rootLog.removeAppender(old);
		}
		
        Enumeration<Appender> oldApps = rootLog.getAllAppenders();
        while (oldApps.hasMoreElements()) {
            Appender app = oldApps.nextElement();
            app.clearFilters();
        }
	}

	/** 
     * A filer that suppresses all log output
     * <p>
     * OpenLR is a trade mark of TomTom International B.V.
     * <p>
     * email: software@openlr.org
     * 
     * @author TomTom International B.V.
     */
    private static final class SuppressFilter extends Filter {
        
        @Override
        public int decide(final LoggingEvent event) {
            return Filter.DENY;
        }
    }

    /**
	 * The Class RepressAdditionalLoggingPolicy. This class is used remove all log events
	 * from being logged with the old appenders if the log level is lower than the 
	 * original level. Old appenders should log only these events they would have logged without
	 * setting a new level for the map viewer.
	 */
	private static final class RepressAdditionalLoggingPolicy implements
			RewritePolicy {

		/** The original level. */
		private final Level originalLevel;

		/**
		 * Instantiates a new repress additional logging policy.
		 * 
		 * @param orig
		 *            the orig
		 */
		private RepressAdditionalLoggingPolicy(final Level orig) {
			originalLevel = orig;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public LoggingEvent rewrite(final LoggingEvent event) {
			if (event.getLevel().isGreaterOrEqual(originalLevel)) {
				return event;
			}
			return null;
		}

	}
}
