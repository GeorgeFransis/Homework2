package javaswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserView extends JFrame {

    private JTextField userIdTextField;
    private JButton followButton;
    private JList<String> followingList;
    private JTextField tweetMessageTextField;
    private JButton postTweetButton;
    private JList<String> newsFeedList;
    private List<Tweet> tweetMessages;
    private String selectedUserId;

    public UserView(List<Tweet> tweetMessages, String selectedUserId) {
        this.tweetMessages = tweetMessages;
        this.selectedUserId = selectedUserId;
        // Set up the frame
        setTitle("User View - " + selectedUserId);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        // First row: User ID text field and Follow button
        JPanel firstRowPanel = new JPanel(new FlowLayout());
        userIdTextField = new JTextField("User ID", 20);
        followButton = new JButton("Follow User");
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                followUser();
            }
        });
        firstRowPanel.add(userIdTextField);
        firstRowPanel.add(followButton);
        add(firstRowPanel);

        // Second row: Current Following label and Following list
        JPanel secondRowPanel = new JPanel(new BorderLayout());
        JLabel followingLabel = new JLabel("Current Following");
        followingList = new JList<>();
        secondRowPanel.add(followingLabel, BorderLayout.NORTH);
        secondRowPanel.add(new JScrollPane(followingList), BorderLayout.CENTER);
        add(secondRowPanel);

        // Third row: Tweet Message text field and Post Tweet button
        JPanel thirdRowPanel = new JPanel(new FlowLayout());
        tweetMessageTextField = new JTextField("Tweet Message", 20);
        postTweetButton = new JButton("Post Tweet");
        postTweetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postTweet();
            }
        });

        thirdRowPanel.add(tweetMessageTextField);
        thirdRowPanel.add(postTweetButton);
        add(thirdRowPanel);

        // Fourth row: News Feed label and News Feed list
        JPanel fourthRowPanel = new JPanel(new BorderLayout());
        JLabel newsFeedLabel = new JLabel("News Feed");
        newsFeedList = new JList<>();
        fourthRowPanel.add(newsFeedLabel, BorderLayout.NORTH);
        fourthRowPanel.add(new JScrollPane(newsFeedList), BorderLayout.CENTER);
        add(fourthRowPanel);

        // Set the frame to be visible
        setVisible(true);

    }

    private void followUser() {
        String userId = userIdTextField.getText().trim();
        DefaultListModel<String> followingListModel = new DefaultListModel<>();

        // Coping existing elements to the new model
        for (int i = 0; i < followingList.getModel().getSize(); i++) {
            followingListModel.addElement(followingList.getModel().getElementAt(i));
        }

        // Checking if the user is already in the following list
        if (!followingListModel.contains(userId)) {
            // Add the user to the following list
            followingListModel.addElement(userId);

            // Setting the new model for followingList
            followingList.setModel(followingListModel);
        } else {
            JOptionPane.showMessageDialog(this, "You are already following this user.", "Duplicate User", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void postTweet() {
        String tweetMessage = tweetMessageTextField.getText().trim();
        String tweetDisplay = "User "+selectedUserId + " tweeted: " + tweetMessage;

        tweetMessages.add(new Tweet(selectedUserId, "Admin", tweetMessage));

        DefaultListModel<String> newsFeedListModel = new DefaultListModel<>();

        // Copy existing elements to the new model
        for (int i = 0; i < newsFeedList.getModel().getSize(); i++) {
            newsFeedListModel.addElement(newsFeedList.getModel().getElementAt(i));
        }

        // Add the tweet display to the news feed list
        newsFeedListModel.addElement(tweetDisplay);

        // Setting the new model for newsFeedList
        newsFeedList.setModel(newsFeedListModel);
    }

    public static void run(List<Tweet> tweetMessages, String selectedUserId) {
        new UserView(tweetMessages, selectedUserId);
    }

}
