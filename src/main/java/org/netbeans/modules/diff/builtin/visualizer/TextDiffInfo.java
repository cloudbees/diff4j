package org.netbeans.modules.diff.builtin.visualizer;

import org.netbeans.api.diff.Difference;

import java.io.Reader;

public class TextDiffInfo extends Info {

    private Reader r1;
    private Reader r2;
    private Difference[] diffs;
    private boolean contextMode;
    private int contextNumLines;

    public TextDiffInfo(String name1, String name2, String title1, String title2,
                        Reader r1, Reader r2, Difference[] diffs) {
        super(name1, name2, title1, title2, null, false, false);
        this.r1 = r1;
        this.r2 = r2;
        this.diffs = diffs;
    }

    public String getName() {
        String componentName = getName1();
        String name2 = getName2();
        if (name2 != null && name2.length() > 0)  componentName += " <> "+name2;
        return componentName;
    }

    public String getTitle() {
        return getTitle1() + " <> " + getTitle2();
    }

    public Reader createFirstReader() {
        return r1;
    }

    public Reader createSecondReader() {
        return r2;
    }

    public Difference[] getDifferences() {
        return diffs;
    }

    /** Setter for property contextMode.
     * @param contextMode New value of property contextMode.
     */
    public void setContextMode(boolean contextMode, int contextNumLines) {
        this.contextMode = contextMode;
        this.contextNumLines = contextNumLines;
    }

    /** Getter for property contextMode.
     * @return Value of property contextMode.
     */
    public boolean isContextMode() {
        return contextMode;
    }

    public int getContextNumLines() {
        return contextNumLines;
    }

}
