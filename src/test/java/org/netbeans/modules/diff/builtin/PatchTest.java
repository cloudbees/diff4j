package org.netbeans.modules.diff.builtin;

import com.cloudbees.diff.ContextualPatch;
import com.cloudbees.diff.ContextualPatch.PatchReport;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class PatchTest extends TestCase {
    public void testPatchOneFile() throws Exception {
        File p = File.createTempFile("test", "diff");
        FileUtils.copyURLToFile(getClass().getResource("singleFilePatch.diff"),p);

        File b = File.createTempFile("test", "base");
        FileUtils.copyURLToFile(getClass().getResource("base.txt"),b);

        ContextualPatch patch = ContextualPatch.create(p, b);
        List<PatchReport> report = patch.patch(false);

        assertEquals(resourceAsString("after.txt"), FileUtils.readFileToString(b));
    }

    public void testMultiFilePatch() throws Exception {
        File d = mktmpdir();
        for (String name : "alpha.txt bravo.txt charlie.txt".split(" ")) {
            FileUtils.copyURLToFile(getClass().getResource("multifile/before/"+name),new File(d,name));
        }

        File p = File.createTempFile("test", "diff");
        FileUtils.copyURLToFile(getClass().getResource("multifile/multiFilePatch.diff"),p);

        ContextualPatch patch = ContextualPatch.create(p,d);
        List<PatchReport> report = patch.patch(false);
        System.out.println(report);

        for (String name : "alpha.txt bravo.txt delta.txt".split(" ")) {
            assertEquals(resourceAsString("multifile/after/"+name), FileUtils.readFileToString(new File(d,name)));
        }
        assertFalse(new File(d,"charlie.txt").exists());
    }
    
    public void testDeleteOneFileWithTimestampsInDiff() throws Exception {
        File p = File.createTempFile("test", "diff");
        FileUtils.copyURLToFile(getClass().getResource("singleFileDeleteWithTimestamp.diff"),p);

        File b = File.createTempFile("test", "base");
        FileUtils.copyURLToFile(getClass().getResource("simple.txt"),b);

        ContextualPatch patch = ContextualPatch.create(p, b);
        List<PatchReport> report = patch.patch(false);

        assertTrue(report.size() == 1);

        for (ContextualPatch.PatchReport r : report) {
            Throwable failure = r.getFailure();
            if (failure != null)
            	fail ("Failed to patch " + r.getFile());
        }

        assertFalse(b.exists());
    }

    public void testAddOneFileWithTimestampsInDiff() throws Exception {
        File d = mktmpdir();

        File p = File.createTempFile("test", "diff");
        FileUtils.copyURLToFile(getClass().getResource("singleFileAddWithTimestamp.diff"),p);

        ContextualPatch patch = ContextualPatch.create(p,d);
        List<PatchReport> report = patch.patch(false);
        System.out.println(report);

        File n = new File(d,"simple.txt");
        assertTrue(n.exists());
        assertEquals(resourceAsString("simple.txt"), FileUtils.readFileToString(n));
    }

    private String resourceAsString(String name) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(name));
    }

    private File mktmpdir() throws IOException {
        File f = File.createTempFile("diff", "dir");
        f.delete();
        f.mkdir();
        return f;
    }
}
