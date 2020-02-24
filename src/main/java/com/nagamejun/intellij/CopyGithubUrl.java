package com.nagamejun.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.nagamejun.intellij.helper.Action;
import com.nagamejun.intellij.helper.GithubRepository;

public class CopyGithubUrl extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        new Action(new GithubRepository(project.getBasePath())).actionPerformed(event);
    }
}