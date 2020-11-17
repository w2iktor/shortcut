package pl.symentis.byteman.tutorial;

import org.jboss.byteman.contrib.bmunit.BMScript;
import org.jboss.byteman.contrib.bmunit.BMUnitConfig;
import org.jboss.byteman.contrib.bmunit.WithByteman;
import org.junit.jupiter.api.Test;

@WithByteman
@BMUnitConfig(
        loadDirectory = "src/test/resources/byteman")
public class StaticMethodTest {
    @Test
    @BMScript("mock_static_method.btm")
    void mock_static_method() {
        Main.main(new String[]{});
    }
}
