package com.ericsson.cifwk.taf.ui.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class MavenJobBuilder extends AbstractJobBuilder {

    MavenJobBuilder(String name, String description) {
        super(name, description)
    }

    @Override
    Job create(DslFactory factory) {
        factory.mavenJob name
    }
}
