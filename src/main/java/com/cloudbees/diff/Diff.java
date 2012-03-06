package com.cloudbees.diff;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a diff between two files. Entry point of the library.
 *
 * @author Kohsuke Kawaguchi
 */
public class Diff extends ArrayList<Difference> implements Serializable {
    /**
     * Computes a diff between two files.
     */
    public static Diff diff(File f1, File f2, boolean ignoreWhitespace) throws IOException {
        Reader r1=new FileReader(f1);
        try {
            Reader r2=new FileReader(f2);
            try {
                return diff(r1, r2, ignoreWhitespace);
            } finally {
                IOUtils.closeQuietly(r2);
            }
        } finally {
            IOUtils.closeQuietly(r1);
        }
    }

    /**
     * Create the differences of the content two streams.
     * @param r1 the first source
     * @param r2 the second source to be compared with the first one.
     */
    public static Diff diff(Reader r1, Reader r2, boolean ignoreWhitespace) throws IOException {
        return diff(getLines(r1), getLines(r2), ignoreWhitespace);
    }

    private static List<String> getLines(Reader r) throws IOException {
        BufferedReader br = new BufferedReader(r);
        String line;
        List<String> lines = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    /**
     * Compares two "files" that are passed in as a list of lines.
     */
    public static Diff diff(List<String> lines1, List<String> lines2, boolean ignoreWhitespace) {
        return HuntDiff.diff(lines1,lines2,ignoreWhitespace);
    }

    /**
     * Prints the difference in the unified diff format.
     * 
     * @param numContextLines
     *      Number of context lines to generate around the diff.
     */
    public String toUnifiedDiff(String name1, String name2, Reader r1, Reader r2, int numContextLines) throws IOException {
        return new TextDiffInfo(name1,name2,r1,r2,this).toUnifiedDiffText(numContextLines);
    }

    /**
     * Apply the difference to the specified source.
     *
     * @param source The source stream
     * @return The patched stream
     * @throws IOException When reading from the source stream fails
     * @throws ParseException When the source does not match the patch to be applied
     */
    public Reader apply(Reader source) {
        return new Patch(this, source);
    }

    private static final long serialVersionUID = 1L;
}
