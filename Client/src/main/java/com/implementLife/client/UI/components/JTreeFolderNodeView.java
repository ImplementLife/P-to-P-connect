package com.implementLife.client.UI.components;

import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeFolderNodeView extends DefaultMutableTreeNode {
    public JTreeFolderNodeView(Object userObject) {
        super(userObject);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}