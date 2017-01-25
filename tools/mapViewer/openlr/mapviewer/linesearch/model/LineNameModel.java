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
package openlr.mapviewer.linesearch.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Pattern;

import openlr.map.Line;
import openlr.mapviewer.utils.LineNameResolver;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PatternAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 * The data access object that enables search for line names basing upon 
 * a <a href="http://lucene.apache.org/">Lucene</a> text index. <br>
 * The usage of this class is the following: <br>
 * <br>
 * 1. Provide lines to be indexed:<br>
 * {@link #addToBatch(Line lineA)}<br>
 * {@link #addToBatch(Line lineB)}<br>
 * ...<br>
 * <br>
 * 2. Execute indexing:<br>
 * {@link #index()}<br>
 * <br>
 * 3. The data object is then prepared to perform line name searches on the 
 * index:<br>
 * {@link #getMatchingNames(String searchString)}<br>
 * <br>
 * <p>
 * OpenLR is a trade mark of TomTom International B.V.
 * <p>
 * email: software@openlr.org
 * 
 * @author TomTom International B.V.
 * 
 */
public class LineNameModel {
	
	/** The Constant logger. */
	private static final Logger LOG = Logger.getLogger(LineNameModel.class);
	
	/** The Lucene operator "AND" */
	private static final String AND = "AND";

	/**
	 * The number of maximum hits delivered by a query. Required by Lucene to
	 * create a data buffer for the results.
	 */
	private static final int MAX_HITS = 999999;

	/**
	 * The index field containing the line name.
	 */
	private static final String FIELD_NAME = "name";
	/**
	 * The index field containing a Line ID.
	 */
	private static final String FIELD_LINE_ID = "lineID";	

	/**
	 * A map used for a optimized bulk indexing process.
	 */
	private Map<String, Document> batchFillMap = new TreeMap<String, Document>();
	
	/**
	 * The index location. 
	 */
	private Directory index;
	
	/**
	 * A list of additional line-search-specific word split characters.
	 */
	private static final char[] WORD_SPLIT_CHARS = {'/', '_', '-', '.'};
	
	/** 
	 * The pattern for the Lucene analyzer: split line names at blanks or the 
	 * defined {@link #WORD_SPLIT_CHARS}.
	 */
	private static final String ANALYZER_PATTERN;
	static {
		StringBuilder analyzerPatt = new StringBuilder("[\\s");
		for (char splitChar : WORD_SPLIT_CHARS) {
			analyzerPatt.append('\\').append(splitChar);
		}
		ANALYZER_PATTERN = analyzerPatt.append("]+").toString();
	}
	/**
	 * The strategy the Lucene index splits line names in the index and the 
	 * search strings.
	 */
	private static final Analyzer THE_ANALYZER = new PatternAnalyzer(
			Version.LUCENE_30, Pattern.compile(ANALYZER_PATTERN), true, null);
	
	/**
	 * The query parser.
	 */
	private static final QueryParser QUERY_PARSER = new QueryParser(
			Version.LUCENE_30, FIELD_NAME, THE_ANALYZER);
	
    /**
     * The systems tempory file directory
     */
    private static final String INDEX_BASE_DIR = System
            .getProperty("java.io.tmpdir");
    
    /**
     * The base name of the lucene index directory in the file system
     */
    private static final String LUCENE_INDEX_DIR_BASE = "mapviewer-road-name-index_";
	
	/**
	 * The final path to the lucene index directory
	 */
	private final File indexDirectory;
	
	/**
	 * Creates a new line name model
	 * @param mapIdentifier An identifier of the related map that differs different map during application runtime
	 */
    public LineNameModel(final String mapIdentifier) {
        indexDirectory = new File(INDEX_BASE_DIR, LUCENE_INDEX_DIR_BASE
                + mapIdentifier);
    }

	/**
	 * Adds the line data to the given map as preparation of a later indexing of
	 * the contained {@link Document} objects. For each unique line name an map
	 * entry is created. The attached {@link Document} contains all the line IDs
	 * that refer to this line name.
	 * 
	 * @param line
	 *            The line object to evaluate.
	 */
	public final void addToBatch(final Line line) {
		
		String name = LineNameResolver.resolveLineName(line.getNames());
		if (name.length() > 0) {
			Document doc = batchFillMap.get(name);
			if (doc == null) {
				doc = new Document();
				Field f = new Field(FIELD_NAME, name, Field.Store.YES,
						Field.Index.ANALYZED);
				f.setOmitNorms(true);
				doc.add(f);
			}
			doc.add(new NumericField(FIELD_LINE_ID, Field.Store.YES, false)
					.setLongValue(line.getID()));

			batchFillMap.put(name, doc);			
		}
	}	
	
    /**
     * Cleans up all file system resources that were created in connection with
     * the indexed line name collection.
     */
	public final void cleanup() {
	    
		deleteDir(indexDirectory);
		
        index = null;
	}

	/**
	 * Deletes the given directory
	 * @param dir The directory 
	 */
    private static void deleteDir(final File dir) {
        
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                if (!f.delete()) {
                    LOG.warn("Could not delete temporary index file "
                            + f.getAbsolutePath());
                }
            }
            if (!dir.delete()) {
                LOG.warn("Could not remove temporary index directory "
                        + dir.getAbsolutePath());
            }
        }
    }      

	/**
	 * Initial actions.
	 * 
	 * @throws LineNameModelException
	 *             If an error occurs initializing the index.
	 */
	private void init() throws LineNameModelException {

		// if not yet initialized
		if (index == null) {

			File baseDir = indexDirectory.getParentFile();

			if (baseDir.canWrite()) {
				try {
					if (!indexDirectory.exists()) {
						boolean created = indexDirectory.mkdir();
                        if (!created) {
                            throw new LineNameModelException(
                                    "Could not create directory for street name index: "
                                            + indexDirectory.getAbsolutePath());
                        }
                        // in case of MapViewer crashes
                        indexDirectory.deleteOnExit();
					}
					index = new SimpleFSDirectory(indexDirectory);

					if (LOG.isDebugEnabled()) {
						LOG.debug("Index for street name search will be stored " 
								+ "on file system at " + indexDirectory.getAbsolutePath());
					}

				} catch (IOException e) {
					throw new LineNameModelException(e);
				}
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Got no write access to " + baseDir.getAbsolutePath()
							+ "Index for street name search will be hold in memory!");
				}
				index = new RAMDirectory();
			}
			// allows wildcards at the beginning, this makes the search slower!!
			QUERY_PARSER.setAllowLeadingWildcard(true);
		}
	}

	/**
	 * Indexes all the line data provided before via {@link #addToBatch(Line)}.
	 * 
	 * @throws LineNameModelException
	 *             If an error occurs creating the index.
	 */
	public final void index() throws LineNameModelException {
	    
		try {
			init();

			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, THE_ANALYZER);
			config.setOpenMode(OpenMode.CREATE);
			IndexWriter writer = new IndexWriter(index, config);

			for (Document doc : batchFillMap.values()) {
				writer.addDocument(doc);
			}

			writer.close();

		} catch (Exception e) {
			throw new LineNameModelException(e);
		} finally {
			batchFillMap.clear();
		}
	}

	/**
	 * Delivers line names matching the <code>searchString</code>. First all
	 * hitches are retrieved that match the exact beginning of every word in
	 * the query string. Afterwards all matches with the substring for
	 * searchString at any position of the word are added: <br>
	 * <pre>
	 * input("a br") =>
	 * search("a*" AND "br*") + search("*a*" AND "*br*")
	 * </pre>  <br>
	 * This ranks the hits with exact beginning first, but delivers substring
	 * matches too.
	 * 
	 * @param searchString
	 *            The search string. The search string is expected to be a list
	 *            of strings separated by white-spaces.
	 * @return A collection of matching {@link Item}s.
	 * @throws LineNameModelException
	 *             If an error occurs querying the data source.
	 */
	public final Collection<Item> getMatchingNames(final String searchString)
			throws LineNameModelException {

		Collection<Document> docs;
		final String searchCleaned = removeSpecialChars(searchString);
		
		try {
			if (searchCleaned.length() > 0) {
				// first get all the start matches
				String q1 = buildQueryString(searchCleaned, true);
				docs = searchMatchingDocs(q1);
				// second add all the substring matches
				String q2 = buildQueryString(searchCleaned, false);
				q2 = q2 + " AND NOT (" + q1 + ")";
				docs.addAll(searchMatchingDocs(q2));
			} else {
				docs = searchMatchingDocs("*"); // search all
			}
		} catch (Exception e) {
			throw new LineNameModelException(e);
		}
		Collection<Item> linesFound = new ArrayList<Item>(docs.size());

		List<Long> liDS;
		for (Document document : docs) {

			liDS = new ArrayList<Long>();

			for (Fieldable lID : document.getFieldables(FIELD_LINE_ID)) {
				liDS.add(Long.valueOf(lID.stringValue()));
			}
			linesFound.add(new Item(document.get(FIELD_NAME), liDS));
		}

		return linesFound;
	}

	/**
	 * Performs a search on the index using the given query string.
	 * 
	 * @param queryStr
	 *            A valid Lucene query string.
	 * @return the matching index elements
	 * @throws ParseException
	 *             If an error occurs evaluating the search string.
	 * @throws IOException
	 *             If an IO error occurs reading the index.
	 */
	private Collection<Document> searchMatchingDocs(final String queryStr)
			throws ParseException, IOException {

		Collection<Document> result;

		Query query = QUERY_PARSER.parse(queryStr);

		int hitsPerPage = MAX_HITS; // if the single
		IndexReader reader = IndexReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				hitsPerPage, true);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		if (LOG.isDebugEnabled()) {
			LOG.debug("Query " + queryStr + " Found " + hits.length + " hits.");
		}

		result = new ArrayList<Document>(hits.length);
		for (int i = 0; i < hits.length; ++i) {		
			int docId = hits[i].doc;
			result.add(searcher.doc(docId));
		}

		searcher.close();

		return result;
	}

	/**
	 * Builds up the query string. Performs some general actions. Escapes all
	 * Lucene special characters. Places wildcards around single words in the
	 * input string. single words are determined by splitting at white-spaces.
	 * 
	 * @param input
	 *            The input string.
	 * @param startFixed
	 *            If set to <code>true</code> the wildcards are set that way
	 *            that beginnings of words are searched exactly as given, i.e.
	 *            no wildcard will be prefixed. If this parameter is
	 *            <code>false</code> a wildcard will be places at the beginning
	 *            of each word in the input string ("*street*")
	 * @return The proper Lucene search string.
	 */
	private String buildQueryString(final String input, final boolean startFixed) {

		String inputEscaped = QueryParser.escape(input);	    

		StringTokenizer whiteSpaceTokenizer = new StringTokenizer(inputEscaped);
		StringBuilder sb = new StringBuilder();

		String current;
		// wildcard every single word
		while (whiteSpaceTokenizer.hasMoreElements()) {

			current = whiteSpaceTokenizer.nextToken();

			if (!startFixed) {
				sb.append("*");
			}
			sb.append(current).append("*");
			if (whiteSpaceTokenizer.hasMoreTokens()) {
				sb.append(" ").append(AND).append(" "); // assume that the user
														// wanted to match all
														// the words to be found
			}
		}
		return sb.toString();
	}
	
	/**
	 * Removes special characters from the input string that shouldn't get thru
	 * to the executed search. A special character is replaced with a blank. If
	 * there is nothing left except blanks an empty string is returned.
	 * 
	 * @param input
	 *            The input string.
	 * @return The cleaned string
	 */
	private String removeSpecialChars(final String input) {

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);

			boolean found = false;
			for (char s : WORD_SPLIT_CHARS) {
				if (s == c) {
					found = true;
					result.append(' ');
					break;
				}
			}
			if (!found) {
				result.append(c);
			} 
		}
		return result.toString().trim();
	}
	
	/**
	 * Data class that holds the data of an entry that can be delivered by the 
	 * {@link LineNameModel}.
	 */
	public static class Item {

		/** The line name. */
		private String ln;
		/** The line IDs. */
		private final List<Long> lids;

		/**
		 * Creates a new instance.
		 * @param lineName The line name.
		 * @param lineIDs The line IDs corresponding to the line name.
		 */
		Item(final String lineName, final List<Long> lineIDs) {
			this.ln = lineName;
			this.lids = lineIDs;
		}

		/**
		 * @return The line name.
		 */
		public final String getLineName() {
			return ln;
		}

		/**
		 * @return The line IDs.
		 */		
		public final List<Long> getLineIDs() {
			return lids;
		}

		/**
		 * @return just the line name. That's an pragmatic way to support this
		 * data object in our GUI component displaying object.toString()
		 */
		@Override
		public final String toString() {
			return ln;
		}
	}	
}
