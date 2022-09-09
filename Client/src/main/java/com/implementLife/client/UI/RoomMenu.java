package com.implementLife.client.UI;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.icons.FlatTreeCollapsedIcon;
import com.formdev.flatlaf.icons.FlatTreeExpandedIcon;
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
import static com.implementLife.client.net.NetServiceImpl.getNetService;

public class RoomMenu {
    private JPanel root;
    private JTree rooms;

    private JTextField fieldFindRoomByUUID;
    private JTextField fieldRoomName;
    private JTextField fieldRoomId;

    private JButton btnCreateRoom;
    private JButton btnFind;
    private JButton btnCopyRoomIdToClipboard;
    private JButton btnSetMeHost;
    private JButton btnConnect;
    private JButton btnRefresh;

    private DefaultMutableTreeNode resent_rooms;
    private DefaultMutableTreeNode friends_roms;
    private JTreeFolderViewNode public_roms;

    public RoomMenu() {
        initTreeListOfRooms();
        initButtons();
        initFields();
        rooms.addTreeSelectionListener(e -> onSelectRoomElement());
        btnCopyRoomIdToClipboard.addActionListener(e -> onClickBtnCopyRoomIdToClipboard());
    }

    private void initFields() {
        fieldFindRoomByUUID.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        fieldFindRoomByUUID.putClientProperty(PLACEHOLDER_TEXT, "UUID of room");
    }

    private void initButtons() {
        btnFind.addActionListener(e -> onClickBtnFind());
        btnCreateRoom.addActionListener(e -> onClickBtnCreateRoom());
        btnRefresh.addActionListener(e -> onClickBtnRefresh());
        btnRefresh.setIcon(new ImageIcon("icons/IntelliJ/refresh.svg"));
    }

    private void initTreeListOfRooms() {
        resent_rooms = new JTreeFolderViewNode("Resent Rooms");
        friends_roms = new JTreeFolderViewNode("Friends Rooms");
        public_roms = new JTreeFolderViewNode("Public Rooms");

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

        fieldRoomName.setEnabled(false);
        btnCreateRoom.setEnabled(false);
        btnSetMeHost.setEnabled(true);
        btnConnect.setEnabled(true);
    }

    private Map<UUID, Room> allRooms;

    private void onClickBtnRefresh() {
        allRooms = getNetService().getAllRooms().stream().collect(Collectors.toMap(Room::getId, Function.identity()));
        for (Room room : allRooms.values()) {
            public_roms.add(new DefaultMutableTreeNode(room.getId()));
        }

        rooms.updateUI();
        resetTreeView();
    }

    private void onClickBtnCopyRoomIdToClipboard() {
        String idAsStr = fieldRoomId.getText();
        if (!idAsStr.trim().equals("")) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(idAsStr), null);
        }
    }

    private void onClickBtnFind() {
        if (fieldFindRoomByUUID.getText().trim().equals("")) return;
        Room room = getNetService().getRoom(fieldFindRoomByUUID.getText());
        if (room != null) {
            resent_rooms.add(new DefaultMutableTreeNode(room.getId().toString()));
            rooms.updateUI();
            resetTreeView();

            inRoomState(room);
        }
    }

    private void onClickBtnCreateRoom() {
        String name = fieldRoomName.getText();
        Room room = getNetService().createRoom(name);
        resent_rooms.add(new DefaultMutableTreeNode(room.getId()));
    }

    private void onSelectRoomElement() {
        DefaultMutableTreeNode selectedComponent = (DefaultMutableTreeNode) rooms.getLastSelectedPathComponent();
        if (selectedComponent instanceof JTreeFolderViewNode) return;

        UUID roomUuidAsString = (UUID) selectedComponent.getUserObject();
        Room room = getNetService().getRoom(roomUuidAsString.toString());
        if (room != null) {
            inRoomState(room);
        }
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
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Last Rooms");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldFindRoomByUUID = new JTextField();
        panel2.add(fieldFindRoomByUUID, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        panel2.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        rooms = new JTree();
        Font roomsFont = this.$$$getFont$$$(null, -1, -1, rooms.getFont());
        if (roomsFont != null) rooms.setFont(roomsFont);
        scrollPane1.setViewportView(rooms);
        btnFind = new JButton();
        btnFind.setText("Find");
        panel2.add(btnFind, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRefresh = new JButton();
        btnRefresh.setText("Refresh");
        panel2.add(btnRefresh, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(panel4);
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
        label3.setText("Room UUID:");
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
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JTabbedPane tabbedPane1 = new JTabbedPane();
        tabbedPane1.setTabLayoutPolicy(0);
        tabbedPane1.setTabPlacement(1);
        panel7.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Players", panel8);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Settings", panel9);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Chat", panel10);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel11, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnCreateRoom = new JButton();
        btnCreateRoom.setText("Create Room");
        panel11.add(btnCreateRoom, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel11.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        btnSetMeHost = new JButton();
        btnSetMeHost.setText("Set Me Host");
        panel11.add(btnSetMeHost, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnConnect = new JButton();
        btnConnect.setText("Connect");
        panel11.add(btnConnect, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
