package org.netbeans.modules.diff.builtin;

import com.cloudbees.diff.ContextualPatch;
import com.cloudbees.diff.ContextualPatch.PatchReport;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        for (String name : "alpha.txt bravo.txt charlie.txt multi_add_content.txt multi_remove_content.txt".split(" ")) {
            FileUtils.copyURLToFile(getClass().getResource("multifile/before/"+name),new File(d,name));
        }

        File p = File.createTempFile("test", "diff");
        FileUtils.copyURLToFile(getClass().getResource("multifile/multiFilePatch.diff"),p);

        ContextualPatch patch = ContextualPatch.create(p,d);
        List<PatchReport> report = patch.patch(false);
        System.out.println(report);

        for (String name : "alpha.txt bravo.txt delta.txt multi_add_content.txt multi_remove_content.txt multi_add_file.txt".split(" ")) {
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

    public void testUtf8Patch() throws Exception {
        File diff = new File(getClass().getResource("/data/utf8/diff.patch").getPath());

        File tmpFile = File.createTempFile("before", "dat");
        FileUtils.copyURLToFile(getClass().getResource("/data/utf8/before.txt"),tmpFile);

        ContextualPatch patch = ContextualPatch.create(diff,tmpFile, StandardCharsets.UTF_8);
        List<PatchReport> reports = patch.patch(false);
        for (PatchReport r : reports) {
            if (r.getFailure()!=null)
                throw new IOException("Failed to patch " + r.getFile(), r.getFailure());
        }
        assertEquals(resourceAsString("/data/utf8/after.txt"), FileUtils.readFileToString(tmpFile));
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
