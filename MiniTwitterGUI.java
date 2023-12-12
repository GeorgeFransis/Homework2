package javaswing;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.List;
import java.util.ArrayList;

public class MiniTwitterGUI extends JFrame {

    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode usersNode;
    private DefaultMutableTreeNode groupsNode;
    private JTree tree;
    private JTextArea userIdTextArea;
    private JTextArea groupIdTextArea;
    private List<Tweet> tweetMessages = new ArrayList<>();

    public MiniTwitterGUI() {
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Mini Twitter - Admin Control Panel");
        setSize(800, 600);

        // Left panel for tree
        root = new DefaultMutableTreeNode("Root");
        usersNode = new DefaultMutableTreeNode("Users");
        groupsNode = new DefaultMutableTreeNode("Groups");
        root.add(usersNode);
        root.add(groupsNode);

        tree = new JTree(root);

        JScrollPane treeScrollPane = new JScrollPane(tree);

        // Right panel for text areas and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2, 5, 5));

        userIdTextArea = createTextAreaWithPlaceholder("Enter User ID");
        groupIdTextArea = createTextAreaWithPlaceholder("Enter Group ID");

        addRow(inputPanel, userIdTextArea, "Add User");
        addRow(inputPanel, groupIdTextArea, "Add Group");

        JButton openUserViewButton = new JButton("Open User View");
        JButton showUserTotalButton = new JButton("Show User Total");
        JButton showGroupTotalButton = new JButton("Show Group Total");
        JButton showMessagesTotalButton = new JButton("Show Messages Total");
        JButton showPositivePercentageButton = new JButton("Show Positive Percentage");
        JButton uniqueIDButton = new JButton("User/Group ID verification");

        inputPanel.add(new JPanel());
        inputPanel.add(new JPanel());

        inputPanel.add(openUserViewButton);
        inputPanel.add(new JPanel());

        inputPanel.add(new JPanel());
        inputPanel.add(new JPanel());

        inputPanel.add(showUserTotalButton);
        inputPanel.add(showGroupTotalButton);
        inputPanel.add(showMessagesTotalButton);
        inputPanel.add(showPositivePercentageButton);

        openUserViewButton.addActionListener((ActionEvent e) -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (selectedNode != null && selectedNode.getParent() == usersNode) {
                UserView.run(tweetMessages, selectedNode.getUserObject().toString());
            } else {
                JOptionPane.showMessageDialog(MiniTwitterGUI.this, "Please select a user to open User View.", "User Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        showUserTotalButton.addActionListener((ActionEvent e) -> {
            showUserTotal();
        });

        showGroupTotalButton.addActionListener((ActionEvent e) -> {
            showGroupTotal();
        });

        showMessagesTotalButton.addActionListener((ActionEvent e) -> {
            showTweetMessages();
        });

        showPositivePercentageButton.addActionListener((ActionEvent e) -> {
            int totalTweets = tweetMessages.size();
            int positiveTweets = countPositiveTweets(tweetMessages);

            double positivePercentage = (double) positiveTweets / totalTweets * 100;

            String message = String.format("Positive Percentage: %.2f%%", positivePercentage);
            JOptionPane.showMessageDialog(MiniTwitterGUI.this, message, "Positive Percentage", JOptionPane.INFORMATION_MESSAGE);
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, inputPanel);

        splitPane.setResizeWeight(
                0.5);

        add(splitPane);

        setVisible(
                true);
    }

    private int countPositiveTweets(List<Tweet> tweets) {
        int count = 0;
        for (Tweet tweet : tweets) {
            // logic to determine if a tweet is positive
            // For simplicity, let's assume any tweet containing "good" is positive
            if (tweet.getMessage().toLowerCase().contains("good")) {
                count++;
            }
        }
        return count;
    }

    private void showTweetMessages() {
        StringBuilder tweetMessagesList = new StringBuilder("Tweet Messages:\n");

        for (Tweet tweet : tweetMessages) {
            tweetMessagesList.append(tweet.getSender())
                    .append(" tweeted to ")
                    .append(tweet.getReceiver())
                    .append(": ")
                    .append(tweet.getMessage())
                    .append("\n");
        }

        displayList("Tweet Messages Total", tweetMessagesList.toString());
    }

    private void addRow(JPanel panel, JTextArea textArea, String buttonText) {
        textArea.setBorder(BorderFactory.createCompoundBorder(
                textArea.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        final String placeholder = textArea.getText();
        JButton addButton = new JButton(buttonText);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = textArea.getText();

                if (!inputText.isEmpty() && !inputText.equals(placeholder)) {
                    DefaultMutableTreeNode parentNode;

                    if (textArea == userIdTextArea) {
                        parentNode = usersNode;
                    } else {
                        parentNode = groupsNode;
                    }

                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(inputText);
                    ((DefaultTreeModel) tree.getModel()).insertNodeInto(newNode, parentNode, parentNode.getChildCount());

                    TreePath path = new TreePath(newNode.getPath());
                    tree.scrollPathToVisible(path);
                    tree.setSelectionPath(path);

                }
            }
        });

        panel.add(textArea);
        panel.add(addButton);
    }

    private JTextArea createTextAreaWithPlaceholder(String placeholder) {
        JTextArea textArea = new JTextArea(1, 1);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(placeholder);
        textArea.setForeground(Color.GRAY);

        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText(placeholder);
                    textArea.setForeground(Color.GRAY);
                }
            }
        });

        return textArea;
    }

    private void showUserTotal() {
        Enumeration<DefaultMutableTreeNode> enumeration = usersNode.children();
        StringBuilder userList = new StringBuilder("Users:\n");

        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = enumeration.nextElement();
            userList.append(node.getUserObject()).append("\n");
        }

        displayList("User Total", userList.toString());
    }

    private void showGroupTotal() {
        Enumeration<DefaultMutableTreeNode> enumeration = groupsNode.children();
        StringBuilder groupList = new StringBuilder("Groups:\n");

        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = enumeration.nextElement();
            groupList.append(node.getUserObject()).append("\n");
        }

        displayList("Group Total", groupList.toString());
    }

    private void displayList(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        JFrame frame = new JFrame(title);
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MiniTwitterGUI();
            }
        });
    }
}
