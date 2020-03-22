package com.nagamejun.intellij.helper;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import java.awt.datatransfer.StringSelection;

public class Action {
    private final GithubRepository repo;

    public Action(GithubRepository repo) {
        this.repo = repo;
    }

    public void actionPerformed(AnActionEvent event) {
        final Editor editor = event.getData(PlatformDataKeys.EDITOR);
        final VirtualFile file = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        Integer line = (editor != null) ? editor.visualToLogicalPosition(editor.getSelectionModel().getSelectionStartPosition()).line + 1 : null;
        copyUrl(file, line);
        showStatusBubble(event, file);
    }

    private void copyUrl(VirtualFile file, Integer line) {
        String url = repo.repoUrlFor(file, line);
        CopyPasteManager.getInstance().setContents(new StringSelection(url));
    }

    private void showStatusBubble(AnActionEvent event, VirtualFile file) {
        StatusBar statusBar = WindowManager.getInstance()
                .getStatusBar(DataKeys.PROJECT.getData(event.getDataContext()));

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(
                        "<p>Github URL for '<tt>"
                                + file.getPresentableName()
                                + "</tt> (on master branch) copied to your clipboard.</p>",
                        MessageType.INFO, null)
                .setFadeoutTime(5500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }
}
