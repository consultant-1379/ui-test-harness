/*
Jenkins Job DSL API Viewer - https://jenkinsci.github.io/job-dsl-plugin/
Jenkins Job DSL Playground - http://job-dsl.herokuapp.com/
*/

import com.ericsson.cifwk.taf.ui.builders.*
import javaposse.jobdsl.dsl.DslFactory

def mvnUnitTest = "clean install"
def mvnITest = "${mvnUnitTest} -Pitest"
def mvnDeploy = "clean deploy -DskipTests"

def unitTests = 'Unit tests'
def iTests = 'Integration tests'
def snapshots = 'Snapshots deployment'

def aa = new GerritJobBuilder('AA-gerrit-unit-tests', unitTests, mvnUnitTest)
def ab = new GerritJobBuilder('AB-gerrit-integration-tests', iTests, mvnITest)
def ac = new SonarQubeGerritJobBuilder('AC-gerrit-sonar-qube')
def ba = new SimpleJobBuilder('BA-unit-tests', unitTests, mvnUnitTest)
def bb = new SimpleJobBuilder('BB-integration-tests', iTests, mvnITest)
def bc = new SimpleJobBuilder('BC-deploy-snapshots', snapshots, mvnDeploy)
def ca = new DocsBuildJobBuilder('CA-docs-build')
def cb = new DocsPublishJobBuilder('CB-docs-publish', ca.name)

def build = new MasterBuildFlowBuilder('XX-build-flow',
        """\
        parallel(
            { build '${ba.name}' },
            { build '${bb.name}' }
        )
        parallel(
            { build '${bc.name}' },
            { 
                build '${ca.name}'
                build '${cb.name}' 
            }
        )
        """.stripIndent())

def release = new ReleaseJobBuilder('XX-release', build.name)

[aa, ab, ac, ba, bb, bc, ca, cb, build, release]*.build(this as DslFactory)
