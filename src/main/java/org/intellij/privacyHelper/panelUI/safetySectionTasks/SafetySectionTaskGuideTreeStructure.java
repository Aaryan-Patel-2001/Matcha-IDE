package org.intellij.privacyHelper.panelUI.safetySectionTasks;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.ide.util.treeView.AbstractTreeStructureBase;
import com.intellij.openapi.project.Project;
import org.intellij.privacyHelper.panelUI.safetySectionTasks.TaskGuide.GuideRootNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SafetySectionTaskGuideTreeStructure extends AbstractTreeStructureBase {
    protected final AbstractTreeNode myRootElement;

    protected SafetySectionTaskGuideTreeStructure(Project project) {
        super(project);
        myRootElement = new GuideRootNode(myProject, new Object());
    }

    @Override
    public @Nullable List<TreeStructureProvider> getProviders() {
        return null;
    }

    @Override
    public @NotNull Object getRootElement() {
        return myRootElement;
    }

    @Override
    public void commit() {

    }

    @Override
    public boolean hasSomethingToCommit() {
        return false;
    }
}
