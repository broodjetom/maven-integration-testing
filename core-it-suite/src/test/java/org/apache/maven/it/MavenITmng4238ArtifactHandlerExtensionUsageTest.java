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

import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.io.IOException;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-3506">MNG-3506</a>.
 * 
 * @author John Casey
 * @version $Id$
 */
public class MavenITmng4238ArtifactHandlerExtensionUsageTest
    extends AbstractMavenIntegrationTestCase
{

    private static final String GID = "org.apache.maven.it.mng4238";
    private static final String AID = "mng-4238";
    private static final String VERSION = "1";
    private static final String TYPE = "jar";
    private static final String BAD_TYPE = "coreit";

    public MavenITmng4238ArtifactHandlerExtensionUsageTest()
    {
        super( "(2.2.0,)" );
    }
    
    public void testProjectPackagingUsage()
        throws IOException, VerificationException
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4238" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        
        // remove the artifact+POM that should be installed here.
        verifier.deleteArtifact( GID, AID, VERSION, "pom" );
        verifier.deleteArtifact( GID, AID, VERSION, TYPE );
        
        // shouldn't exist, but we want to make sure we're testing the current pass properly...
        verifier.deleteArtifact( GID, AID, VERSION, BAD_TYPE );
        
        verifier.executeGoal( "install" );
        
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
        
        // Now, if everything worked, we have a .pom and a .jar in the local repo.
        // IF IT DIDN'T, we have a .pom and a .coreit in the local repo...
        
        String path = verifier.getArtifactPath( GID, AID, VERSION, TYPE );
        assertTrue( path + " should have been installed.", new File( path ).exists() );
        
        path = verifier.getArtifactPath( GID, AID, VERSION, "pom" );
        assertTrue( path + " should have been installed.", new File( path ).exists() );
        
        path = verifier.getArtifactPath( GID, AID, VERSION, BAD_TYPE );
        assertFalse( path + " should NOT have been installed.", new File( path ).exists() );
    }
}