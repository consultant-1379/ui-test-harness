import static com.ericsson.cifwk.taf.ui.Constants.*

job("${JOBS_PREFIX}-job-seed") {
    description "Job DSL seed job for auto provisioning the rest of the ${PROJECT_NAME} jobs"
    scm {
        git {
            remote {
                url GIT_URL
                name GIT_REMOTE
            }
            branch GIT_BRANCH
        }
    }
    triggers {
        scm 'H/2 * * * *'
    }
    steps {
        dsl {
            external "${JOBS_PATH}/*.groovy"
            additionalClasspath "${JOBS_MODULE}/src/main/groovy"
            removeAction 'DELETE'
        }
    }
}