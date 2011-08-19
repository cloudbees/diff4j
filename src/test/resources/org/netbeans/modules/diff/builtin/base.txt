package org.netbeans.modules.diff.builtin;

import com.infradna.diff.ContextualPatch;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.sound.midi.Patch;
import java.io.File;

/**
 * @author Kohsuke Kawaguchi
 */
public class PatchTest extends TestCase {
    public void testPatchOneFile() throws Exception {
        File p = File.createTempFile("test", "diff");
        FileUtils.copyURLToFile(getClass().getResource("singleFilePatch.diff"),p);
        ContextualPatch p = ContextualPatch.create(p, null);
        p.patch(false);
    }
}
