package org.netbeans.modules.diff.builtin.visualizer;

import org.netbeans.api.diff.Difference;

import java.io.FileNotFoundException;
import java.io.Reader;

/**
 * This class contains informations about the differences.
 */
public abstract class Info extends Object {

    private String name1;
    private String name2;
    private String title1;
    private String title2;
    private String mimeType;
    private boolean chooseProviders;
    private boolean chooseVisualizers;
        
    public Info(String name1, String name2, String title1, String title2,
                String mimeType, boolean chooseProviders, boolean chooseVisualizers) {
        this.name1 = name1;
        this.name2 = name2;
        this.title1 = title1;
        this.title2 = title2;
        this.mimeType = mimeType;
        this.chooseProviders = chooseProviders;
        this.chooseVisualizers = chooseVisualizers;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getTitle1() {
        return title1;
    }

    public String getTitle2() {
        return title2;
    }

    public String getMimeType() {
        return mimeType;
    }

    public boolean isChooseProviders() {
        return chooseProviders;
    }

    public boolean isChooseVisualizers() {
        return chooseVisualizers;
    }

    public Difference[] getDifferences() {
        return null;
    }

    public Difference[] getInitialDifferences() {
        return null;
    }

    public abstract Reader createFirstReader() throws FileNotFoundException;

    public abstract Reader createSecondReader() throws FileNotFoundException;
}
