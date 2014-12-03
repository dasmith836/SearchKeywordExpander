package com.rei.search.keywordexpander;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * read a list of permutation specifications from a file and output the expanded permutations.
 * an example permutation specification:
 *  (patagonia, patigonia)(down, insulated,_)(men's, women's,_)(xl, l, m, s, large, medium, small,_)(hiking, camping, winter, cold weather, casual,_)(jacket, jackets)
 *
 * the "_" represents the optional node.
 */
public class App {
    public static void main(String[] args) {
        // first arg is a filename
        if (args.length != 1) {
            System.err.println("USAGE: applicationName filename");
            return;
        }
        // open the file
        String specificationFileName = args[0];
        File file = new File(specificationFileName);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null ) {
                writeExpandedPermutations(line, System.out);
            }
        } catch (FileNotFoundException e){
            System.err.println("file not found: " + specificationFileName);
        } catch (IOException e) {
            System.err.println("error reading file: " + specificationFileName);
        }
    }

    private static void writeExpandedPermutations(String specification, PrintStream out) {
        String[] split = specification.split("\\(");
        List<List<String>> termNodes = new ArrayList<>();
        for (String s : split) {
            if (isNotEmpty(s)) {
                s = s.replaceAll("\\)", "");
                String[] terms = s.split(",\\b*");
                List<String> termList = new ArrayList<>();
                termNodes.add(termList);
                for (String term : terms) {
                    term = term.trim();
                    if ("_".equals(term)) {
                        term = "";
                    }
                    termList.add(term);
                }
            }
        }
        List<StringBuilder> stringBuilders = buildPermutations(termNodes, 0, null);
        for (StringBuilder stringBuilder : stringBuilders) {
            out.println(stringBuilder.toString());
        }
    }

    private static List<StringBuilder> buildPermutations(List<List<String>> termNodes, int level, StringBuilder prefix) {
        // get the terms on this level
        List<StringBuilder> stringBuilders = new ArrayList<>();
        if (level < termNodes.size()) {
            List<String> terms = termNodes.get(level);
            for (String term : terms) {
                StringBuilder newPrefix = new StringBuilder(prefix == null ? "" : prefix).append(term).append(isNotEmpty(term) ? " " : "");
                stringBuilders.addAll(buildPermutations(termNodes, level + 1, newPrefix));
            }
        } else {
            stringBuilders.add(new StringBuilder(prefix == null ? "" : prefix));
        }
        return stringBuilders;
    }

    private static boolean isNotEmpty(String string) {
        return string != null && string.length() > 0;
    }

}
