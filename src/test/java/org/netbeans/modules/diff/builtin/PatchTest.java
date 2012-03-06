package org.netbeans.modules.diff.builtin;

import com.cloudbees.diff.ContextualPatch;
import com.cloudbees.diff.ContextualPatch.PatchReport;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
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

        assertEquals(IOUtils.toString(getClass().getResourceAsStream("after.txt")), FileUtils.readFileToString(b));
    }
}
