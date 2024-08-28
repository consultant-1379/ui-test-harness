package com.ericsson.cifwk.taf.ui.builders

import com.ericsson.cifwk.taf.ui.utils.Git
import com.ericsson.cifwk.taf.ui.utils.Maven
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class SimpleJobBuilder extends FreeStyleJobBuilder {

    final String mavenGoal

    SimpleJobBuilder(String name,
                     String description,
                     String mavenGoal) {
        super(name, description)

        this.mavenGoal = mavenGoal
    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            scm {
                Git.simple delegate
            }
            steps {
                Maven.goal delegate, mavenGoal
            }
        }
    }
}
