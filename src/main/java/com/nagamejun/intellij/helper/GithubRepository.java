package com.nagamejun.intellij.helper;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitUtil;
import git4idea.repo.GitRepository;
import com.intellij.openapi.project.Project;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubRepository {
    private final Project project;
    private VirtualFile filePath;
    private static Pattern INI_CATEGORY = Pattern.compile("\\[\\s*(\\w+)[\\s'\"]+(\\w+)[\\s'\"]+\\]");
    private static Pattern URL_VALUE = Pattern.compile("\\s*url\\s*=\\s*([^\\s]*)\\.git");
    private final String gitConfig;
    private File gitConfigFile;

    public GithubRepository(Project project) {
        this(project, ".git/config");
    }

    @VisibleForTesting
    public GithubRepository(Project project, String gitconfig) {
        this.project = project;
        this.gitConfig = gitconfig;
    }

    public void gitConfig() {
        String projectRoot = this.project.getBasePath();
        String gitRoot = findDotGitFolder(new File(projectRoot));
        gitConfigFile = new File(gitRoot, this.gitConfig);
    }

    private String buildUrlFor(String sanitizedUrlValue) {
        return "https://" + sanitizedUrlValue + "/blob/" + buildCommitHash();
    }

    public String buildCommitHash() {
        GitRepository repository = GitUtil.getRepositoryManager(project).getRepositoryForFileQuick(this.filePath);
        return repository.getCurrentRevision();
    }

    private String buildLineDomainPrefix() {
        return "#L";
    }

    public String repoUrlFor(VirtualFile relativeFilePath) {
        return repoUrlFor(relativeFilePath, null);
    }

    public String repoUrlFor(VirtualFile filePath, Integer line) {
        this.filePath = filePath;
        String path = filePath.getCanonicalPath().replaceFirst(gitConfigFile.getParentFile().getParent(), "");
        return gitBaseUrl() + path + (line != null ? buildLineDomainPrefix() + line : "");
    }

    public String findDotGitFolder(File absolutePath) {
        if (absolutePath.getParent() == null) {
            throw new RuntimeException(
                    "Could not find parent .git/ folder. Maybe path is not in a git repo? " + absolutePath);
        }
        FileFilter gitFolderFinder = new FileFilter() {
            @Override public boolean accept(File pathname) {
                return pathname.getName().equals(".git");
            }
        };
        if (absolutePath.listFiles(gitFolderFinder).length == 1) {
            return absolutePath.getAbsolutePath();
        }
        return findDotGitFolder(absolutePath.getParentFile());
    }

    private String gitBaseUrl() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(gitConfigFile));
            String line;
            boolean inRemoteOriginSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.matches("\\s*#")) continue;
                Matcher matcher = INI_CATEGORY.matcher(line);
                if (matcher.matches()) {
                    inRemoteOriginSection = "remote".equals(matcher.group(1))
                            && "origin".equals(matcher.group(2));
                    continue;
                }
                matcher = URL_VALUE.matcher(line);
                if (inRemoteOriginSection && matcher.matches()) {
                    return buildUrlFor(matcher.group(1)
                            .replaceAll("ssh://|git://|git@|https://", "")
                            .replaceAll(":", "/"));
                }
            }
            throw new RuntimeException("Did not find [remote \"origin\"] url set in " + gitConfigFile);
        } catch (IOException e) {
            throw new RuntimeException("File " + gitConfigFile + " does not exist.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
