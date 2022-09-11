package com.implementLife.client.UI;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.icons.FlatTreeCollapsedIcon;
import com.formdev.flatlaf.icons.FlatTreeExpandedIcon;
import com.implementLife.client.UI.components.JTreeFolderNodeView;
import com.implementLife.client.UI.components.JTreeRoomView;
import com.implementLife.commonDTO.comServerEntity.Room;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT;
import static com.implementLife.client.AsyncService.getAsyncService;
import static com.implementLife.client.net.NetServiceImpl.getNetService;

public class RoomMenu {
    private JPanel root;
    private JTree rooms;

    private JTextField fieldFindRoomByUUID;
    private JTextField fieldRoomName;
    private JTextField fieldRoomId;
    private JTextField fieldMessage;

    private JButton btnCreateRoom;
    private JButton btnFind;
    private JButton btnCopyRoomIdToClipboard;
    private JButton btnSetMeHost;
    private JButton btnConnect;
    private JButton btnRefresh;
    private JButton btnExit;

    private JTreeFolderNodeView resent_rooms;
    private JTreeFolderNodeView friends_roms;
    private JTreeFolderNodeView public_roms;

    public RoomMenu() {
        initTreeListOfRooms();
        initButtons();
        initFields();
        rooms.addTreeSelectionListener(e -> onSelectRoomElement());
    }

    private void initFields() {
        fieldFindRoomByUUID.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        fieldFindRoomByUUID.putClientProperty(PLACEHOLDER_TEXT, "UUID of room");
    }

    private void initButtons() {
        btnCopyRoomIdToClipboard.addActionListener(e -> onClickBtnCopyRoomIdToClipboard());
        btnFind.addActionListener(e -> onClickBtnFind());
        btnCreateRoom.addActionListener(e -> onClickBtnCreateRoom());
        btnExit.addActionListener(e -> onClickBtnExit());
        btnRefresh.addActionListener(e -> onClickBtnRefresh());
        btnRefresh.setIcon(new ImageIcon("icons/IntelliJ/refresh.svg"));
    }

    private void initTreeListOfRooms() {
        resent_rooms = new JTreeFolderNodeView("Resent Rooms");
        friends_roms = new JTreeFolderNodeView("Friends Rooms");
        public_roms = new JTreeFolderNodeView("Public Rooms");

        DefaultMutableTreeNode rootNode = clearTreeAndGetRootNode(rooms);
        rootNode.add(resent_rooms);
        rootNode.add(friends_roms);
        rootNode.add(public_roms);

        rooms.expandRow(0);
        rooms.setRootVisible(false);

        resetTreeView();
    }

    public void initAfterCreate() {
        onClickBtnRefresh();
    }

    private void resetTreeView() {
        rooms.updateUI();
        DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer) rooms.getCellRenderer();
        cellRenderer.setOpenIcon(new FlatTreeExpandedIcon());
        cellRenderer.setClosedIcon(new FlatTreeCollapsedIcon());
        cellRenderer.setLeafIcon(null);
        cellRenderer.setFont(rooms.getFont());
        rooms.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        rooms.setToggleClickCount(1);
    }

    public JPanel getRoot() {
        return root;
    }

    private DefaultMutableTreeNode clearTreeAndGetRootNode(JTree tree) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.removeAllChildren();
        model.reload();
        return root;
    }

    private void inRoomState(Room room) {
        fieldRoomId.setText(room.getId().toString());
        fieldRoomName.setText(room.getName());
        setEnableStateInRoom(true);
    }

    private void outOfRoomState() {
        fieldRoomId.setText("");
        fieldRoomName.setText("");
        setEnableStateInRoom(false);
    }

    private void setEnableStateInRoom(boolean inRoom) {
        fieldRoomName.setEditable(!inRoom);
        btnCreateRoom.setEnabled(!inRoom);
        btnSetMeHost.setEnabled(inRoom);
        btnConnect.setEnabled(inRoom);
        btnExit.setEnabled(inRoom);
    }

    private Map<UUID, Room> allRooms;

    private void onClickBtnRefresh() {
        getAsyncService().runAsync(() -> {
            btnRefresh.setEnabled(false);
            try {
                allRooms = getNetService().getAllRooms().stream().collect(Collectors.toMap(Room::getId, Function.identity()));
                public_roms.removeAllChildren();
                for (Room room : allRooms.values()) {
                    public_roms.add(new DefaultMutableTreeNode(new JTreeRoomView(room)));
                }
                resetTreeView();
            } finally {
                btnRefresh.setEnabled(true);
            }
        });
    }

    private void onClickBtnCopyRoomIdToClipboard() {
        String idAsStr = fieldRoomId.getText();
        if (!idAsStr.trim().equals("")) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(idAsStr), null);
        }
    }

    private void onClickBtnFind() {
        getAsyncService().runAsync(() -> {
            btnFind.setEnabled(false);
            try {
                if (fieldFindRoomByUUID.getText().trim().equals("")) return;
                Room room = getNetService().getRoom(fieldFindRoomByUUID.getText());
                if (room != null) {
                    resent_rooms.add(new DefaultMutableTreeNode(new JTreeRoomView(room)));
                    resetTreeView();
                    inRoomState(room);
                }
            } finally {
                btnFind.setEnabled(true);
            }
        });
    }

    private void onClickBtnExit() {
        outOfRoomState();
    }

    private void onClickBtnCreateRoom() {
        getAsyncService().runAsync(() -> {
            btnCreateRoom.setEnabled(false);
            Room room;
            try {
                String name = fieldRoomName.getText();
                room = getNetService().createRoom(name);
            } finally {
                btnCreateRoom.setEnabled(true);
            }
            if (room != null) {
                resent_rooms.add(new DefaultMutableTreeNode(new JTreeRoomView(room)));
                resetTreeView();
                inRoomState(room);
            }
        });
    }

    private void onSelectRoomElement() {
        getAsyncService().runAsync(() -> {
            DefaultMutableTreeNode selectedComponent = (DefaultMutableTreeNode) rooms.getLastSelectedPathComponent();
            if (selectedComponent instanceof JTreeFolderNodeView) return;

            UUID roomUuidAsString = ((JTreeRoomView) selectedComponent.getUserObject()).getId();
            Room room = getNetService().getRoom(roomUuidAsString.toString());
            if (room != null) {
                inRoomState(room);
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(4, 4, 4, 4), -1, -1));
        root.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(182);
        panel1.add(splitPane1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Rooms");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRefresh = new JButton();
        btnRefresh.setText("Refresh");
        panel3.add(btnRefresh, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        fieldFindRoomByUUID = new JTextField();
        panel2.add(fieldFindRoomByUUID, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        panel2.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        rooms = new JTree();
        Font roomsFont = this.$$$getFont$$$(null, -1, -1, rooms.getFont());
        if (roomsFont != null) rooms.setFont(roomsFont);
        rooms.setRootVisible(true);
        scrollPane1.setViewportView(rooms);
        btnFind = new JButton();
        btnFind.setText("Find");
        panel2.add(btnFind, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(panel4);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Room info");
        panel5.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Room ID:");
        panel6.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRoomId = new JTextField();
        fieldRoomId.setEditable(false);
        panel6.add(fieldRoomId, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnCopyRoomIdToClipboard = new JButton();
        btnCopyRoomIdToClipboard.setText("Copy");
        panel6.add(btnCopyRoomIdToClipboard, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Name:");
        panel6.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRoomName = new JTextField();
        panel6.add(fieldRoomName, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(10, 0, 10, 0), -1, -1));
        panel4.add(panel7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JTabbedPane tabbedPane1 = new JTabbedPane();
        tabbedPane1.setTabLayoutPolicy(0);
        tabbedPane1.setTabPlacement(1);
        panel7.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Settings", panel8);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Players", panel9);
        final JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setHorizontalScrollBarPolicy(31);
        panel9.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Chat", panel10);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel10.add(scrollPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel10.add(panel11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldMessage = new JTextField();
        panel11.add(fieldMessage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnConnect = new JButton();
        btnConnect.setText("Send");
        panel11.add(btnConnect, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel12, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnCreateRoom = new JButton();
        btnCreateRoom.setText("Create Room");
        panel12.add(btnCreateRoom, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel12.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSetMeHost = new JButton();
        btnSetMeHost.setEnabled(false);
        btnSetMeHost.setText("Set Me Host");
        panel12.add(btnSetMeHost, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnExit = new JButton();
        btnExit.setEnabled(false);
        btnExit.setText("Exit");
        panel12.add(btnExit, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}
