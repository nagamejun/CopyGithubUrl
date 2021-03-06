package com.nagamejun.intellij;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import com.nagamejun.intellij.helper.GithubRepository;
import org.junit.Test;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class GithubRepositoryTest {

    @Mock
    private Project project;
    private VirtualFile vFile;

    @Before
    public void setUp() throws Exception {
        project = mock(Project.class);
        vFile = mock(VirtualFile.class);
    }

    @Test
    public void github_line() throws Exception {
        doReturn(".").when(project).getBasePath();
        doReturn("/src/main/java/com/nagamejun/intellij/helper/GithubRepository.java").when(vFile).getCanonicalPath();

        GithubRepository repository = spy(new GithubRepository(project, "src/test/java/com/nagamejun/intellij/test_gitconfig.txt"));
        doReturn(".").when(repository).findDotGitFolder(any());
        doReturn("1234567").when(repository).buildCommitHash();
        repository.gitConfig();
        assertEquals(
                "https://github.com/nagamejun/CopyGithubUrl/blob/1234567/src/main/java/com/nagamejun/intellij/helper/GithubRepository.java#L22",
                repository.repoUrlFor(vFile, 22)
        );
    }
}
