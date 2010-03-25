package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.Properties;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-4590">MNG-4590</a>.
 * 
 * @author Benjamin Bentmann
 */
public class MavenITmng4590ImportedPomUsesSystemPropertiesTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4590ImportedPomUsesSystemPropertiesTest()
    {
        super( "[2.0.9,3.0-alpha-1),[3.0-alpha-8,)" );
    }

    /**
     * Verify that imported POMs are processed using the same execution properties as the importing POM.
     */
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4590" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng4590" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.setEnvironmentVariable( "MAVEN_OPTS", "-Dtest.file=pom.xml" );
        verifier.setSystemProperty( "test.dir", testDir.getAbsolutePath() );
        verifier.getCliOptions().add( "--settings" );
        verifier.getCliOptions().add( "settings.xml" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/pom.properties" );
        assertEquals( "1", props.getProperty( "project.dependencyManagement.dependencies" ) );
        assertEquals( "dep-a", props.getProperty( "project.dependencyManagement.dependencies.0.artifactId" ) );
        assertEquals( new File( testDir, "pom.xml" ).getAbsoluteFile(), 
            new File( props.getProperty( "project.dependencyManagement.dependencies.0.systemPath" ) ) );
    }

}