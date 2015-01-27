package org.pitest.plugins.testng;

import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.classinfo.ClassName;
import org.pitest.classinfo.Repository;
import org.pitest.testapi.Configuration;
import org.pitest.testapi.TestGroupConfig;
import org.pitest.testapi.TestPluginFactory;
import org.pitest.testng.TestNGConfiguration;
import org.pitest.util.PitError;

/**
 * Plugin that Uses TestNG Configuration only.
 *
 * @author Stefan Pennndorf <stefan@cyphoria.net>
 */
public class TestNGOnlyTestPluginFactory implements TestPluginFactory {

    @Override
    public Configuration createTestFrameworkConfiguration(TestGroupConfig testGroupConfig, ClassByteArraySource classByteArraySource) {
        final Repository classRepository = new Repository(classByteArraySource);

        if (classRepository.fetchClass(ClassName.fromString("org.testng.TestNG")).hasNone()) {
            throw new PitError(
                    "Unable to use TestNG as test framework: TestNG must be on the classpath.");
        }

        return new TestNGConfiguration(testGroupConfig);
    }

    @Override
    public String description() {
        return "Forces usage of TestNG as Test Framework";
    }
}
