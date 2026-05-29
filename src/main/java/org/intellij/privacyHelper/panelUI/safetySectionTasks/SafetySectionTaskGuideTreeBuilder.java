/*
 * Copyright 2000-2022 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.intellij.privacyHelper.panelUI.safetySectionTasks;

import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.util.ui.tree.TreeUtil;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.util.Comparator;

public class SafetySectionTaskGuideTreeBuilder {
    protected final Project myProject;
    private final JTree myTree;
    private final SafetySectionTaskGuideTreeStructure myTreeStructure;
    private final StructureTreeModel<SafetySectionTaskGuideTreeStructure> myStructureTreeModel;

    public SafetySectionTaskGuideTreeBuilder(JTree tree, DefaultTreeModel treeModel, Project project) {
        myProject = project;
        myTree = tree;
        myTreeStructure = new SafetySectionTaskGuideTreeStructure(project);
        myStructureTreeModel = new StructureTreeModel<>(myTreeStructure, MyComparator.ourInstance, project);
    }

    public final void init() {
        myTree.setModel(new AsyncTreeModel(myStructureTreeModel, myProject));
    }

    public void queueUpdate() {
        myStructureTreeModel.invalidateAsync();
    }

    public void collapseAll() {
        int row = myTree.getRowCount() - 1;
        while (row > 0) {
            myTree.collapseRow(row);
            row--;
        }
    }

    public void expandAll(Runnable onDone) {
        TreeUtil.expandAll(myTree, onDone == null ? () -> {} : onDone);
    }

    public Object getRootElement() {
        return myTreeStructure.getRootElement();
    }

    private static final class MyComparator implements Comparator<NodeDescriptor<?>> {
        public static final Comparator<NodeDescriptor<?>> ourInstance = new MyComparator();

        @Override
        public int compare(NodeDescriptor<?> descriptor1, NodeDescriptor<?> descriptor2) {
            int weight1 = descriptor1.getWeight();
            int weight2 = descriptor2.getWeight();
            if (weight1 != weight2) {
                return weight1 - weight2;
            }
            else {
                return descriptor1.getIndex() - descriptor2.getIndex();
            }
        }
    }
}
