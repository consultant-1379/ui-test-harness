package com.ericsson.cifwk.taf.ui.builders

import com.ericsson.cifwk.taf.ui.utils.Gerrit
import com.ericsson.cifwk.taf.ui.utils.Git
import com.ericsson.cifwk.taf.ui.utils.Maven
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class GerritJobBuilder extends FreeStyleJobBuilder {

    private static final String DESCRIPTION_SUFFIX = 'as a part of Gerrit verification process'

    final String mavenGoal

    GerritJobBuilder(String name,
                     String description,
                     String mavenGoal) {
        super(name, "${description} ${DESCRIPTION_SUFFIX}")

        this.mavenGoal = mavenGoal
    }

    @Override
    Job build(DslFactory factory) {
        def job = super.build(factory)
        job.with {
            concurrentBuild()
            scm {
                Git.gerrit delegate
            }
            triggers {
                Gerrit.patchsetCreated delegate
            }
            steps {
                Maven.goal delegate, mavenGoal
            }
        }
        return job
    }
}
