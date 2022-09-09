package com.implementLife.client.UI;

import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeFolderViewNode extends DefaultMutableTreeNode {
    public JTreeFolderViewNode(Object userObject) {
        super(userObject);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}