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

import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.apache.maven.shared.verifier.Verifier;

import java.io.File;
import java.util.Properties;

import org.junit.jupiter.api.Test;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-3575">MNG-3575</a>.
 */
public class MavenITmng3575HexadecimalOctalPluginParameterConfigTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng3575HexadecimalOctalPluginParameterConfigTest()
    {
        super( "[3.0.3,)" );
    }

    /**
     * Verify that numeric plugin parameters can be configured using hexadecimal/octal notation.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-3575" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/config.properties" );
        assertEquals( "64", props.getProperty( "byteParam" ) );
        assertEquals( "1024", props.getProperty( "shortParam" ) );
        assertEquals( "65536", props.getProperty( "integerParam" ) );
        assertEquals( "68719476735", props.getProperty( "longParam" ) );
    }

}
