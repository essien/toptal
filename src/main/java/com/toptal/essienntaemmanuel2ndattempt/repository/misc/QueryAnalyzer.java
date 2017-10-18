package com.toptal.essienntaemmanuel2ndattempt.repository.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bodmas
 */
public class QueryAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(QueryAnalyzer.class);

    // Leave out OR, AND since they're already valid HQL operators.
    private static final String[][] operators = {{"eq", "="}, {"ne", "!="}, {"gt", ">"}, {"lt", "<"}, {"ge", ">="}, {"le", "<="}};
    private static final Pattern pattern = Pattern.compile("(?<=\\w+ )(eq|ne|gt|lt|ge|le) ");

    public static String clean(String query) {
        Matcher matcher = pattern.matcher(query);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            final String group = matcher.group(1);
            log.debug("group = " + group);
            matcher.appendReplacement(sb, map(group));
        }
        matcher.appendTail(sb);
        final String toString = sb.toString();
        log.debug("Cleaned to " + toString);
        return toString;
    }

    private static String map(String arg) {
        for (String[] item : operators)
            if (item[0].equals(arg))
                return item[1];
        throw new IllegalArgumentException(arg);
    }

    public static void main(String[] args) {
        clean("(date eq '2016-05-01') AND ((number_of_calories gt 20) OR (calories_less_than_expected eq true))");
        // and bode eq 'game eq thrones'
    }
}
