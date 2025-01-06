package edu.hsog.db;

import edu.hsog.db.DTOs.GadgetDTO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;

public class GUI {
    private JPanel masterPanel;
    private JButton exitJBtn;
    private JButton connectionJBtn;
    private JButton countJBtn;
    private JLabel jConnectionLabel;
    private JLabel countJLbl;
    private JLabel imageLabel;
    private JTextField userLabel;
    private JTextField passwordLabel;
    private JButton loginButton;
    private JButton registerButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton clearButton;
    private JButton firstButton;
    private JButton lastButton;
    private JButton searchButton;
    private JTextField gadgetURL;
    private JTextField keywordLabel;
    private JTextArea descriptionTextArea;
    private JButton loadImgBtn;
    private JButton deleteItemBtn;
    private JButton saveItemBtn;
    private JTextField commentTextField;
    private JButton saveRatingWithCommentButton;
    private JSlider slider1;
    private JTextArea commentTextArea;
    private JLabel loggedUserEmail;
    private JLabel ratingLabel;

    public static String gadgetEmail;


    public GUI() {

        slider1.setMinimum(1);
        slider1.setMaximum(5);
        slider1.setMajorTickSpacing(1);
        slider1.setPaintLabels(true);
        slider1.setPaintTicks(true);
        slider1.setValue(3);

        exitJBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        connectionJBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Globals.initConnectionPool();
                jConnectionLabel.setText("verbunden");
//                setupUI();
                //TODO kto bon crash 1 test
            }
        });
        countJBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int c = DBQueries.count();
                countJLbl.setText("Count: " + c);

            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean userRegistered = DBQueries.registerUser(userLabel.getText(), passwordLabel.getText());
                if (userRegistered){
                    loginUser();
                    setupUI();
                }
            }
        });

        loadImgBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            // From: https://www.codejava.net/java-se/swing/show-simple-open-file-dialog-usingjfilechooser
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setName("imageJFch");
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    //Hier erscheint das Bild im jLabel2
                    Icon icon = Converter.loadIconFromFile(selectedFile.getAbsolutePath());
                    imageLabel.setIcon(icon);
                    imageLabel.setText("");
                }
            }
            });


        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggedUserEmail.setText("");
                gadgetURL.setText("");
                keywordLabel.setText("");
                commentTextArea.setText("");
                descriptionTextArea.setText("");
                commentTextField.setText("");
                imageLabel.setIcon(null);
                imageLabel.setText("");
                ratingLabel.setText("");
            }
        });

        // next, prev...

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!DBQueries.isFirst()) {

                    GadgetDTO gadgets = DBQueries.previousGadget();

                    gadgetURL.setText(gadgets.getUrl());
                    keywordLabel.setText(gadgets.getKeywords());
                    descriptionTextArea.setText(gadgets.getDescription());
                    gadgetEmail = gadgets.getEmail();

                    loadComments(gadgets.getUrl());
                    loadRating(gadgets.getUrl());

                    if(gadgets.getImage() != null){
                        ByteArrayInputStream bis = new ByteArrayInputStream(gadgets.getImage());
                        Icon icon = null;
                        try {
                            icon = new ImageIcon(ImageIO.read(bis));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    }else {
                        imageLabel.setIcon(null);
                        imageLabel.setText("No Image Available");
                    }

                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!DBQueries.isLast()) {

                    GadgetDTO gadgets = DBQueries.nextGadget();

                    gadgetURL.setText(gadgets.getUrl());
                    keywordLabel.setText(gadgets.getKeywords());
                    loadComments(gadgets.getUrl());
                    descriptionTextArea.setText(gadgets.getDescription());
                    gadgetEmail = gadgets.getEmail();

                    loadRating(gadgets.getUrl());

                    if(gadgets.getImage() != null){
                        ByteArrayInputStream bis = new ByteArrayInputStream(gadgets.getImage());
                        Icon icon = null;
                        try {
                            icon = new ImageIcon(ImageIO.read(bis));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    }else {
                        imageLabel.setIcon(null);
                        imageLabel.setText("No Image Available");
                    }

                }
            }
        });
        firstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GadgetDTO gadgets = DBQueries.moveFirst();

                gadgetURL.setText(gadgets.getUrl());
                keywordLabel.setText(gadgets.getKeywords());
                loadComments(gadgets.getUrl());
                descriptionTextArea.setText(gadgets.getDescription());
                gadgetEmail = gadgets.getEmail();
                loadRating(gadgets.getUrl());

                if(gadgets.getImage() != null){
                    ByteArrayInputStream bis = new ByteArrayInputStream(gadgets.getImage());
                    Icon icon = null;
                    try {
                        icon = new ImageIcon(ImageIO.read(bis));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    imageLabel.setIcon(icon);
                    imageLabel.setText("");
                }else {
                    imageLabel.setIcon(null);
                    imageLabel.setText("No Image Available");
                }


            }
        });
        lastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GadgetDTO gadgets = DBQueries.moveLast();

                gadgetURL.setText(gadgets.getUrl());
                keywordLabel.setText(gadgets.getKeywords());
                loadComments(gadgets.getUrl());
                descriptionTextArea.setText(gadgets.getDescription());
                gadgetEmail = gadgets.getEmail();
                loadRating(gadgets.getUrl());

                if(gadgets.getImage() != null){
                    ByteArrayInputStream bis = new ByteArrayInputStream(gadgets.getImage());
                    Icon icon = null;
                    try {
                        icon = new ImageIcon(ImageIO.read(bis));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    imageLabel.setIcon(icon);
                    imageLabel.setText("");
                }else {
                    imageLabel.setIcon(null);
                    imageLabel.setText("No Image Available");
                }

            }
        });
        deleteItemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Globals.getLoggedUser().equals(gadgetEmail)){
                    if(DBQueries.deleteGadget(gadgetURL.getText()) > 0) {
                        System.out.println("mrena");
                        setupUI();
                        //TODO: testit i bon problem
                    }
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mostRatedGadgetUrl = DBQueries.mostRatedGadget();

                GadgetDTO mostRatedGadget = DBQueries.loadGadgetByUrl(mostRatedGadgetUrl);


                gadgetURL.setText(mostRatedGadget.getUrl());
                keywordLabel.setText(mostRatedGadget.getKeywords());
                loadComments(mostRatedGadget.getUrl());
                descriptionTextArea.setText(mostRatedGadget.getDescription());
                gadgetEmail = mostRatedGadget.getEmail();
                loadRating(mostRatedGadget.getUrl());

                if(mostRatedGadget.getImage() != null){
                    ByteArrayInputStream bis = new ByteArrayInputStream(mostRatedGadget.getImage());
                    Icon icon = null;
                    try {
                        icon = new ImageIcon(ImageIO.read(bis));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    imageLabel.setIcon(icon);
                    imageLabel.setText("");
                }else {
                    imageLabel.setIcon(null);
                    imageLabel.setText("No Image Available");
                }
            }
        });
        saveRatingWithCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Globals.getLoggedUser() != null){
                    if(DBQueries.checkRating(Globals.getLoggedUser(), gadgetURL.getText())){
                        DBQueries.editRating(Globals.getLoggedUser(), gadgetURL.getText(), slider1.getValue(), commentTextField.getText());
                        loadComments(gadgetURL.getText());
                        loadRating(gadgetURL.getText());
                    }else{
                        if(DBQueries.rateGadget(Globals.getLoggedUser(), gadgetURL.getText(), slider1.getValue(), commentTextField.getText()) > 0){
                            loadComments(gadgetURL.getText());
                            loadRating(gadgetURL.getText());
                        }
                    }

                }
            }
        });
        saveItemBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Icon icon = imageLabel.getIcon();

                if (Globals.getLoggedUser() != null) {
                    DBQueries.insertGadget(gadgetURL.getText(), Globals.getLoggedUser(), keywordLabel.getText(), descriptionTextArea.getText(), icon);
                    setupUI();
                }

            }
        });
    }

    public void loginUser() {

        boolean userExists = DBQueries.userLoginData(userLabel.getText(), passwordLabel.getText());

        if (userExists) {
            loggedUserEmail.setText(userLabel.getText());
            Globals.setLoggedUser(userLabel.getText());
            jConnectionLabel.setText("logged in");
        } else {
            Globals.setLoggedUser(null);
        }

        if (Globals.getLoggedUser() != null){
            loggedUserEmail.setText(Globals.getLoggedUser());
            setupUI();
        }
        else
            jConnectionLabel.setText("not logged in");


    }

    public void loadComments(String url){
        commentTextArea.setText("");
            ArrayList<String> comments = DBQueries.getComments(url);
            for (String comment : comments) {
                if(comment != null)
                    commentTextArea.append("- " + comment + "\n");
            }
    }

    public void loadRating(String url){
        ratingLabel.setText("");
        double rating = DBQueries.getRating(url);

        ratingLabel.setText(String.format("%.1f", rating));
    }


    public void setupUI(){
        DBQueries.loadGadgets();
        GadgetDTO gadgets = DBQueries.loadRecord();

        gadgetURL.setText(gadgets.getUrl());
        keywordLabel.setText(gadgets.getKeywords());
        loadComments(gadgets.getUrl());
        descriptionTextArea.setText(gadgets.getDescription());
        gadgetEmail = gadgets.getEmail();
        loadRating(gadgets.getUrl());

        if(gadgets.getImage() != null){
            ByteArrayInputStream bis = new ByteArrayInputStream(gadgets.getImage());
            Icon icon = null;
            try {
                icon = new ImageIcon(ImageIO.read(bis));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            imageLabel.setIcon(icon);
            imageLabel.setText("");
        }else {
            imageLabel.setIcon(null);
            imageLabel.setText("No Image Available");
        }
    }

    public JPanel getMasterPanel() {
        return masterPanel;
    }
}

//TODO:
// The owner should be able to edit the keywords, description and the image anytime by clicking SaveItem
// Sort the emails in right order
// Close the rs

/*Problems
    TODO:
     When clicking 4 times on login
     setupUI
     shej a duet ta bosh order by url ke emailet


 */