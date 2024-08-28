package com.ericsson.cifwk.taf.ui.builders

import com.ericsson.cifwk.taf.ui.utils.Gerrit
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

import static com.ericsson.cifwk.taf.ui.Constants.GIT_BRANCH

class MasterBuildFlowBuilder extends BuildFlowBuilder {

    static final String DESCRIPTION = "Build flow upon branch '${GIT_BRANCH}' update"

    MasterBuildFlowBuilder(String name, String buildFlowText) {
        super(name, DESCRIPTION, buildFlowText)
    }

    @Override
    Job build(DslFactory factory) {
        super.build(factory).with {
            triggers {
                Gerrit.refUpdated delegate
            }
        }
    }
}
