/*
 * Copyright 2011 Henry Coles and Stefan Penndorf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.pitest.plugins;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.FileUtils;
import org.apache.maven.it.util.ResourceExtractor;
import org.junit.Test;
import org.pitest.testapi.execute.Pitest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Stefan Penndorf <stefan.penndorf@gmail.com>
 */
public class PitPluginMojoIT {

    private final String pitestVersion = getVersion();
    private final String pluginVersion = getPluginVersion();

    private Verifier verifier;

    @Test
    public void shouldWorkWithJMockitAndForceTestNG() throws Exception {
        prepare("/demo-with-jmockit");
        this.verifier.executeGoal("test");
        this.verifier.executeGoal("org.pitest:pitest-maven:mutationCoverage");
    }

    @SuppressWarnings("unchecked")
    private File prepare(final String testPath) throws IOException,
            VerificationException {
        final String tempDirPath = System.getProperty("maven.test.tmpdir",
                System.getProperty("java.io.tmpdir"));
        final File tempDir = new File(tempDirPath, getClass().getSimpleName());
        final File testDir = new File(tempDir, testPath);
        FileUtils.deleteDirectory(testDir);

        final String path = ResourceExtractor.extractResourcePath(getClass(),
                testPath, tempDir, true).getAbsolutePath();

        this.verifier = new Verifier(path);
        this.verifier.setAutoclean(false);
        this.verifier.setDebug(true);
        this.verifier.getCliOptions().add("-Dpit.version=" + this.pitestVersion);
        this.verifier.getCliOptions().add("-Dplugin.version=" + this.pluginVersion);

        return testDir;
    }

    private static String getVersion() {
        String path = "/version.prop";
        InputStream stream = Pitest.class.getResourceAsStream(path);
        Properties props = new Properties();
        try {
            props.load(stream);
            stream.close();
            return (String) props.get("version");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getPluginVersion() {
        String path = "/META-INF/force-testng-plugin.version";
        InputStream stream = PitPluginMojoIT.class.getResourceAsStream(path);
        Properties props = new Properties();
        try {
            props.load(stream);
            stream.close();
            return (String) props.get("version");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
