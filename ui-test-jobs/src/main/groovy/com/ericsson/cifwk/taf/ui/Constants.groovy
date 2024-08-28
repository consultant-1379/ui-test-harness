package com.ericsson.cifwk.taf.ui

class Constants {

    static final String PROJECT_NAME = 'UI Test Toolkit'

    static final String JOBS_PREFIX = 'ui-test-toolkit'
    static final String JOBS_MODULE = 'ui-test-jobs'
    static final String JOBS_DIRECTORY = 'jobs'
    static final String JOBS_PATH = "${JOBS_MODULE}/${JOBS_DIRECTORY}"

    static final String DOCS_MODULE = 'ui-test-toolkit-docs'
    static final String DOCS_DIRECTORY = 'target/site'
    static final String DOCS_PATH = "${DOCS_MODULE}/${DOCS_DIRECTORY}"
    static final String DOCS_ZIP = "site.zip"

    static final String JDK_1_8 = 'JDK 1.8.0_25'
    static final String SLAVE_TAF_MAIN = 'taf_main_slave'

    static final String GERRIT_SERVER = 'gerrit.ericsson.se'
    static final String GERRIT_CENTRAL = '${GERRIT_CENTRAL}' // resolves to 'ssh://gerrit.ericsson.se:29418'
    static final String GERRIT_MIRROR = '${GERRIT_MIRROR}' // resolves to 'ssh://gerritmirror.lmera.ericsson.se:29418'
    static final String GERRIT_BRANCH = '${GERRIT_BRANCH}'
    static final String GERRIT_REFSPEC = '${GERRIT_REFSPEC}'

    static final String GIT_PROJECT = 'OSS/com.ericsson.de/ui-test-harness'
    static final String GIT_URL = "${GERRIT_CENTRAL}/${GIT_PROJECT}"
    static final String GIT_REMOTE = 'origin'
    static final String GIT_BRANCH = 'master'

}
