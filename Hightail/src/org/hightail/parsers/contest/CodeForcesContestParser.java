/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hightail.parsers.contest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.TagFindingVisitor;

/**
 *
 * @author robertrosolek
 */
public class CodeForcesContestParser extends ContestParser {

    final static private String taskUrlRegExp = "/contest/211/problem/(.*)";
    
    @Override
    public ArrayList<String> parse(String URL) throws ParserException {
        
        URL = URL.trim();
        
        Parser parser = new Parser(URL);
        
        // get all <a> tags
        String[] tagsToVisit = {"a"};
        TagFindingVisitor visitor = new TagFindingVisitor(tagsToVisit);
        parser.visitAllNodesWith(visitor);
        Node[] aNodes = visitor.getTags(0);
        
        // filter link tags for those that link to problems
        // (we recognize that on the base of link itself, by using regexp)
        LinkRegexFilter filter = new LinkRegexFilter(taskUrlRegExp);
        ArrayList<String> result = new ArrayList<String>();
        for (Node node: aNodes)
            if (filter.accept(node)) {
                String linkUrl = ((LinkTag) node).extractLink();
                result.add(linkUrl);
            }
        
        // remove link duplicates
        Set<String> s = new HashSet<String>(result);
        result = new ArrayList<String>(s);
        
        return result;        
    }
    
}
