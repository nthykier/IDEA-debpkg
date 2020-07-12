package com.github.nthykier.ideadebpkg.services

import com.intellij.openapi.project.Project
import com.github.nthykier.ideadebpkg.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
