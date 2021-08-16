import org.junit.Test;

import java.io.File;
import java.io.InputStream;

public class TestPath {
    @Test
    public void testPath() {
        System.out.println(getClass().getResource("/").getPath());
    }
}
