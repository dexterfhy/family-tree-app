package ict373assignment2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/*
Title: ICT373 Assignment #2
Author: Dexter Fong (33467253)
File Name: FamilyTreeGUI.java

Purpose: 
FamilyTreeGUI.java is a class that stores information about a Graphical User Interface that 
is used to display a FamilyTree object. It consists of two main panels: one, a home panel that 
is loaded when the program is first run to allow a user to load an existing tree or create a new tree,
and two, the main view/edit panel that allows a user to visualize the tree using a JTree object 
and the details of each node in the tree.

It contains:
    - CONSTRUCTOR: A method to initialize the FamilyTree, JTree and home GUI window object
    - CONSTRUCTION OF MAIN GUI WINDOWS: Methods that allow for the home and view/edit panels to be created
    - DISPLAYING INFO WITHIN GUI WINDOWS: Methods that allow specific information within the view/edit panels 
      to be displayed, including the details of each member and the JTree itself
    - SERIALIZING: Methods that allow the FamilyTree object to be read from and written to a file
    - REPORTING SYSTEM STATUS & ACTIVITY: Methods dealing with the display of JOptionPane prompts and 
      status bars that show when an action is completed successfully
    - INNER CLASSES FOR CALLBACK FUNCTIONS: Inner classes used to implement callback methods that allow functions to be
      executed following events (clicking of buttons, selection of nodes in the tree, etc)

Input: 
String input is received in a combination of text fields, text areas and combo box selections and passed into FamilyMember/Address objects

Output:
The GUI is primarily responsible for displaying output from the FamilyTree object in the view/edit panel. This is achieved
using a JTree and simple text labels

Assumptions/Conditions:
- Functions that allow for members to be deleted or set as the root of the tree will not work on members that do not appear in tree 
  but are stored as FamilyMember relational references (father, spouse, etc) within nodes on the JTree
- The GUI will not allow access to children or grand children via the panel that list the details of each family member in the tree. 
  Hyperlink access is only provided for father/mother/spouse.
- The GUI will not allow the gender and relative type of a family member to be changed once added, to maintain integrity of relationships
- Names (or its constituent parts) may be of any length, but only contain letters and spaces, and no digits or special characters.
- Numbers (street and postal code) may not contain spaces, letters or special characters.
- Life Description is a mandatory field.
*/
public class FamilyTreeGUI {
    //Declaration of private instance GUI components
    private JFrame homePanelFrame, viewPanelFrame;
    private JPanel controlPanel, infoPanel;
    private File currentFile;
    private FamilyTree currentFamilyTree;
    private JTree tree;
    private final JLabel statusLabel = new JLabel("Program loaded");


    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    CONSTRUCTORS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Default constructor for FamilyTreeGUI
    //Initializes a copy of the FamilyTree object and a JTree to display the former
    //Calls the showHomePage() method to display the home GUI window
    public FamilyTreeGUI() {
        
        currentFamilyTree = new FamilyTree();
        currentFile = null;
        tree = new JTree();
        showHomePage();
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
 

    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    CONSTRUCTION OF MAIN GUI WINDOWS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //The application consists of the home panel which is displayed when the program is first loaded
    //And the main panel for viewing and editing the family tree
    
    //Displays the initial home panel
    private void showHomePage() {
    	//Sets the properties of the main frame holding all the components of the home panel
        homePanelFrame = new JFrame();
        homePanelFrame.setTitle("Family Trees v2");
        homePanelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homePanelFrame.setSize(416, 280);
        homePanelFrame.setLocationRelativeTo(null);
        JPanel contentPane = new JPanel();
        homePanelFrame.setContentPane(contentPane);
        contentPane.setLayout(null);

        //Program title label
        JLabel familyTreeLabel = new JLabel("Family Trees");
        familyTreeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        familyTreeLabel.setFont(new Font("Segoe UI", Font.BOLD, 45));
        familyTreeLabel.setForeground(Color.BLACK);
        familyTreeLabel.setBounds(6, 6, 390, 70);
        contentPane.add(familyTreeLabel);

        //Brief introduction messages that suggest to the user to select one of two options displayed
        JLabel welcomeTreeLabel = new JLabel("Welcome!");
        welcomeTreeLabel.setForeground(new Color(255, 255, 255));
        welcomeTreeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeTreeLabel.setForeground(Color.BLACK);
        welcomeTreeLabel.setBounds(25, 90, 569, 25);
        contentPane.add(welcomeTreeLabel);

        JLabel selectMenuLabel = new JLabel("I would like to...");
        selectMenuLabel.setForeground(Color.BLACK);
        selectMenuLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        selectMenuLabel.setBounds(25, 115, 205, 16);
        contentPane.add(selectMenuLabel);

        //Left button that allows the user to open a JFileChooser to load an existing FamilyTree file with the *ft extension
        JButton loadTreeBtn = new JButton("Load Existing Tree");
        loadTreeBtn.setForeground(Color.BLACK);
        loadTreeBtn.setBackground(Color.WHITE);
        loadTreeBtn.setBounds(50, 150, 140, 45);
        
        //Callback method that implements an action listener for the load button
        loadTreeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    //Set the types of files accepted
                    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("FamilyTree Files (*.ft)", "ft"));
                    fileChooser.setAcceptAllFileFilterUsed(true);

                    int result = fileChooser.showOpenDialog(viewPanelFrame);
                    //If an option was selected
                    if (result == JFileChooser.APPROVE_OPTION) {
                        try {
                            homePanelFrame.dispose(); //Destroy the initial panel
                            openFile(fileChooser.getSelectedFile()); //Open the file selected by user
                            showViewEditPanel(); //Load the main view/edit panel
                            displayTree(currentFamilyTree); //Display the tree based on the file loaded
                            editStatus("File Opened: " + (fileChooser.getSelectedFile().getAbsolutePath()));
                        } catch (Exception f) {
                            showErrorDialog(f);
                            editStatus("Error: " + f.getMessage());
                        }
                    }
                }
        });
        contentPane.add(loadTreeBtn);

        //Right button that allows the user to start with an empty tree
        JButton createTreeBtn = new JButton("Create New Tree");
        createTreeBtn.setForeground(Color.BLACK);
        createTreeBtn.setBackground(Color.WHITE);
        createTreeBtn.setBounds(210, 150, 140, 45);

        //Callback method that implements an action listener for the create button
        createTreeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    homePanelFrame.dispose(); //Destroy the initial panel
                    currentFamilyTree = new FamilyTree(); //Instantiate an empty FamilyTree object
                    currentFile = null;
                    showViewEditPanel(); //Load the main view/edit panel
                    displayTree(currentFamilyTree); //Display the empty tree
                    editStatus("Blank tree created");
                }
        });
        contentPane.add(createTreeBtn);

        homePanelFrame.setVisible(true);
    }
    
    //Displays the main view/edit panel
    private void showViewEditPanel() {
        //Sets the properties of the main frame holding all the components of the view/edit panel
        viewPanelFrame = new JFrame("Family Trees v2");
        viewPanelFrame.setSize(750, 800);
        viewPanelFrame.setLayout(new BorderLayout());
        viewPanelFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        //Program title label
        JLabel headerLabel = new JLabel("Family Trees");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 45));
        headerLabel.setForeground(Color.BLACK);
        viewPanelFrame.add(headerLabel, BorderLayout.NORTH);
        
        //The following calls are made to sub-methods that display the various components of the view/edit panel
        displayMenuBar(); //Top menu bar where user can create a new tree, open an existing file, or save the current tree
        displayControlPanel(); //Control panel shows the JTree
        displayStatusBar(); //Bottom row of text that shows the status of actions taken after user selects options in the GUI
        displayTree(currentFamilyTree);

        //Callback method that prompts the user for confirmation before closing the window
        viewPanelFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (promptContinue("Continue? All unsaved changes will be lost.")) {
                    System.exit(0);
                }
            }
        });

        viewPanelFrame.setLocationRelativeTo(null);
        viewPanelFrame.setVisible(true);
    }
    
    //Sub function for menu bar where user can create a new tree, open an existing file, or save the current tree
    private void displayMenuBar() {
        //Initialize a JMenuBar and assign it to the main viewPanelFrame
        JMenuBar menuBar;
        menuBar = new JMenuBar();
        viewPanelFrame.setJMenuBar(menuBar);

        //Construct the fileMenu and the options items available inside
        JMenu fileMenu = new JMenu("File Options");
        menuBar.add(fileMenu);
        
        JMenuItem newAction = new JMenuItem("Create New Tree");
        fileMenu.add(newAction);
        newAction.addActionListener(new createTreeAction());
        
        JMenuItem openAction = new JMenuItem("Open FamilyTree File");
        fileMenu.add(openAction);
        openAction.addActionListener(new openTreeAction());
        
        fileMenu.addSeparator();

        JMenuItem saveAction = new JMenuItem("Save");
        fileMenu.add(saveAction);
        saveAction.addActionListener(new saveAction());
        
        JMenuItem saveAsAction = new JMenuItem("Save As");
        fileMenu.add(saveAsAction);
        saveAsAction.addActionListener(new saveAsAction());

        fileMenu.addSeparator();
                
        JMenuItem exitAction = new JMenuItem("Exit Program");
        fileMenu.add(exitAction);

        //Callback method to prompt user for confirmation before closing the window
        exitAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (promptContinue("Continue? All unsaved changes will be lost.")) {
                    System.exit(0);
                }
            }
        });       
    }

    //Sub function for control panel showing the JTree
    private void displayControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        
        viewPanelFrame.add(controlPanel, BorderLayout.CENTER);
    }

    //Sub function for status bar showing the status of actions taken after user selects options in the GUI
    private void displayStatusBar() {
        //Initializes a panel for the status bar and adds it to the main frame
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        viewPanelFrame.add(statusPanel, BorderLayout.SOUTH);
        
        //Arrange the layout of the panel as well as the label for the text that will be printed in the bar
        statusPanel.setPreferredSize(new Dimension(viewPanelFrame.getWidth(), 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
 
    
    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    DISPLAYING INFO WITHIN GUI WINDOWS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Used to display the family tree using a JTree for a given FamilyTree parameter
    //As well as the info panel on the right
    private void displayTree(FamilyTree familyTree) {
        DefaultMutableTreeNode main = new DefaultMutableTreeNode("Main"); //Root node
        TreePath lastSelectedNode = null; //Last selected node by user
        DefaultMutableTreeNode top;
        
        //Check if tree has a root and displays appropriate message
        if (!familyTree.hasRoot()) {
            top = new DefaultMutableTreeNode("EMPTY TREE");
        } else {
            //Adds the top node in the JTree
            top = new DefaultMutableTreeNode(familyTree.getRoot());
            //Recursive call to populate entire tree from the root
            createTree(top, familyTree.getRoot());
            //Set last selected node to user's last selection
            lastSelectedNode = tree.getSelectionPath();
        }
        
        //Create the JTree given the available nodes
        //Set the properties of the tree (such as visible handles, showing the root, etc)
        tree = new JTree(main);
        main.add(top);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setEnabled(true);
        tree.expandPath(new TreePath(main.getPath()));
        tree.getSelectionModel().addTreeSelectionListener(new treeSelectorAction());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setBorder(new EmptyBorder(0, 10, 0, 10));

        //Expand and display all nodes
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        
        //Set color properties of each node
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object nodeInfo = node.getUserObject();
                if (nodeInfo instanceof FamilyMember) {
                    setTextNonSelectionColor(Color.DARK_GRAY);
                    setBackgroundSelectionColor(Color.LIGHT_GRAY);
                    setTextSelectionColor(Color.DARK_GRAY);
                    setBorderSelectionColor(Color.WHITE);
                } else {
                    setTextNonSelectionColor(new Color(255, 140,0));
                    setBackgroundSelectionColor(Color.WHITE);
                    setTextSelectionColor(new Color(255, 140,0));
                    setBorderSelectionColor(Color.WHITE);
                }
                setLeafIcon(null);
                setClosedIcon(null);
                setOpenIcon(null);
                super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
                return this;
            }
        });
        
        //Allow for scrolling of the tree
        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(250, 0));

        //Display the empty info panel on the right
        infoPanel = new JPanel();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        //Display the edit details window if the tree is empty, else prompt the user with a message to select a node
        JLabel promptInfo = null;
        if (!familyTree.hasRoot()) {
            displayAddRelativeInfo(null);
        } else {
            promptInfo = new JLabel("Select member of to view more details.");
            promptInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            infoPanel.add(promptInfo, BorderLayout.NORTH);
            infoPanel.setOpaque(false);
        }
        
        //Clear the panel and reset the control panel
        controlPanel.removeAll();
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(container);

        container.setLayout(new BorderLayout());
        container.add(treeScrollPane, BorderLayout.WEST);
        container.add(infoPanel, BorderLayout.CENTER);
        
        controlPanel.add(container);
        controlPanel.validate();
        controlPanel.repaint();
        
        //Set tree to last selected node
        tree.setSelectionPath(lastSelectedNode);
    }

    //Display the information for a given node on the info panel (right)
    @SuppressWarnings("unchecked")
    private void displayMemberInfo(FamilyMember member) {
        tree.setEnabled(true);
        
        //Clear the info panel and reset its layout
        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        // Create the gridbag layout for the components 
        infoPanel.setLayout(new GridBagLayout());
        
        //Instantiate a GridBagConstraints object 
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel container = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        infoPanel.add(container, gbc);
        
        //Use a grup layyout to display the form
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateGaps(true);

        //Labels and text fields for first section - personal particulars
        JLabel memberInfoLabel = new JLabel("Personal Particulars");
        memberInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        JLabel firstNameLabel = new JLabel("First Name");
        JLabel firstNameTextField = new JLabel(member.getFirstName(), 10);
        JLabel lastNameLabe = new JLabel("Surname");
        JLabel lastNameTextField = new JLabel(member.getLastName(), 10);
        JLabel maidenNameLabel = new JLabel("Maiden Name");
        JLabel maidenNameTextField = new JLabel();
        if (member.has(FamilyMember.Attribute_MAIDENNAME)) {
            maidenNameTextField.setText(member.getMaidenName());
        } else {
            maidenNameTextField.setText("-");
        }
        JLabel genderLabel = new JLabel("Gender");
        JLabel genderComboBox = new JLabel(member.getGender() == FamilyMember.Gender_MALE ? "Male" : "Female");
        
        JLabel lifeDescriptionLabel = new JLabel("Description");
        JTextArea lifeDescriptionTextArea = new JTextArea();
        
        lifeDescriptionTextArea.setText(member.getLifeDescription());
        lifeDescriptionTextArea.setWrapStyleWord(true);
        lifeDescriptionTextArea.setLineWrap(true);
        lifeDescriptionTextArea.setOpaque(false);
        lifeDescriptionTextArea.setEditable(false);
        lifeDescriptionTextArea.setFocusable(false);
        lifeDescriptionTextArea.setBackground(UIManager.getColor("Label.background"));
        lifeDescriptionTextArea.setFont(UIManager.getFont("Label.font"));
        lifeDescriptionTextArea.setBorder(UIManager.getBorder("Label.border"));
        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);
          
        //Set dimensions of text area based on the length of the life description field
        int height = 18;
        int words = member.getLifeDescription().length();
        if (words > 20) {
        	height = ((int)Math.ceil(((double)words / 20)) * height);
        }
    	
        lifeDescriptionTextArea.setMaximumSize(new Dimension(180, height));
        lifeDescriptionScrollPane1.setMaximumSize(new Dimension(180, height));
        lifeDescriptionScrollPane1.setBorder(UIManager.getBorder("Label.border"));

        //Labels and text fields for second section - address
        JLabel addressInfoLabel = new JLabel("<html><br/>Address");
        addressInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel streetNoLabel = new JLabel("Street Number:");
        JLabel streetNoTextField = new JLabel(member.getAddress().getStreetNumber(), 10);
        JLabel streetNameLabel = new JLabel("Street Name:");
        JLabel streetNameTextField = new JLabel(member.getAddress().getStreetName(), 10);
        JLabel suburbLabel = new JLabel("Suburb");
        JLabel suburbTextField = new JLabel(member.getAddress().getSuburb(), 10);
        JLabel postalCodeLabel = new JLabel("Postal Code");
        JLabel postalCodeTextField = new JLabel(member.getAddress().getPostalCode() + "", 10);

        //Labels and text fields for second section - relationships
        JLabel relativeInfoLabel = new JLabel("<html><br/>Relationship(s)");
        relativeInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        //Style the father label as a hyperlink
        JLabel fatherLabel = new JLabel("Father");
        JLabel fatherTextField = new JLabel();
        if (member.has(FamilyMember.Relation_FATHER)) {
            fatherTextField.setText(member.getFather().toString());
            fatherTextField.setForeground(Color.BLUE);
            Font font = fatherTextField.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            fatherTextField.setFont(font.deriveFont(attributes));
        } else {
            fatherTextField.setText("None");
        }
        
        //Add a mouse listener to the father label to call displayMemberInfo of father when clicked
        fatherTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    if(member.has(FamilyMember.Relation_FATHER)) {
                            displayMemberInfo(member.getFather());
                    }
            }
        });
        
        JLabel motherLabel = new JLabel("Mother");
        JLabel motherTextField = new JLabel();
        
        //Style the mother label as a hyperlink
        if (member.has(FamilyMember.Relation_MOTHER)) {
            motherTextField.setText(member.getMother().toString());
            motherTextField.setForeground(Color.BLUE);
            Font font = motherTextField.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            motherTextField.setFont(font.deriveFont(attributes));
        } else {
            motherTextField.setText("None");
        }
        
        //Add a mouse listener to the mother label to call displayMemberInfo of mother when clicked
        motherTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    if(member.has(FamilyMember.Relation_MOTHER)) {
                            displayMemberInfo(member.getMother());
                    }	
            }
        });
        
        JLabel spouseLabel = new JLabel("Spouse");
        JLabel spouseTextField = new JLabel();
        
        //Style the spouse label as a hyperlink
        if (member.has(FamilyMember.Relation_SPOUSE)) {
            spouseTextField.setText(member.getSpouse().toString());
            spouseTextField.setForeground(Color.BLUE);
            Font font = spouseTextField.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            spouseTextField.setFont(font.deriveFont(attributes));
        } else {
            spouseTextField.setText("None");
        }

        //Add a mouse listener to the spouse label to call displayMemberInfo of spouse when clicked
        spouseTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    if(member.has(FamilyMember.Relation_SPOUSE)) {
                            displayMemberInfo(member.getSpouse());
                    }
            }
        });
        
        //Children and grandchildren labels use a for each loop that runs through the children of each member 
        //The latter uses a nested for each loop to get the names of all grandchidlren and display them accordingly
        JLabel childrenLabel = new JLabel("Children");
        String children = "<html>";
        if (member.has(FamilyMember.Relation_CHILDREN)) {
            for (FamilyMember child : member.getChildren()) {
                children += child.toString() + "<br>";
            }
            children += "</html>";
        } else {
            children = "None";
        }
        JLabel childrenTextField = new JLabel(children);

        JLabel grandChildrenLabel = new JLabel("Grand Children");
        String grandChildren = "<html>";
        if (member.has(FamilyMember.Relation_CHILDREN)) {
            for (FamilyMember child : member.getChildren()) {
                if (child.has(FamilyMember.Relation_CHILDREN)) {
                    for (FamilyMember grandChild : child.getChildren()) {
                        grandChildren += grandChild.toString() + "<br>";
                    }
                }
            }
            grandChildren += "</html>";
        } else {
            grandChildren = "None";
        }
        JLabel grandChildrenTextField = new JLabel(grandChildren);
        JLabel dimmyText = new JLabel("");
        //

        //Alignment of all components given previous GroupLayout
        //Horizontal Alignment
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(memberInfoLabel)
                        .addComponent(dimmyText)
                        .addComponent(firstNameLabel)
                        .addComponent(lastNameLabe)
                        .addComponent(maidenNameLabel)
                        .addComponent(genderLabel)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(dimmyText)
                        .addComponent(addressInfoLabel)
                        .addComponent(dimmyText)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNameLabel)
                        .addComponent(suburbLabel)
                        .addComponent(postalCodeLabel)
                        .addComponent(dimmyText)
                        .addComponent(relativeInfoLabel)
                        .addComponent(dimmyText)
                        .addComponent(fatherLabel)
                        .addComponent(motherLabel)
                        .addComponent(spouseLabel)
                        .addComponent(childrenLabel)
                        .addComponent(grandChildrenLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(firstNameTextField)
                        .addComponent(lastNameTextField)
                        .addComponent(maidenNameTextField)
                        .addComponent(lifeDescriptionScrollPane1)
                        .addComponent(genderComboBox)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoTextField)
                        .addComponent(streetNameTextField)
                        .addComponent(suburbTextField)
                        .addComponent(postalCodeTextField)
                        .addComponent(fatherTextField)
                        .addComponent(motherTextField)
                        .addComponent(spouseTextField)
                        .addComponent(childrenTextField)
                        .addComponent(grandChildrenTextField)
                )
        );
 
        //Vertical Alignment
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(memberInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dimmyText))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(firstNameLabel)
                        .addComponent(firstNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastNameLabe)
                        .addComponent(lastNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maidenNameLabel)
                        .addComponent(maidenNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(lifeDescriptionScrollPane1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dimmyText))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dimmyText))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNoTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNameLabel)
                        .addComponent(streetNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suburbLabel)
                        .addComponent(suburbTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(postalCodeLabel)
                        .addComponent(postalCodeTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dimmyText))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(relativeInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dimmyText))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(fatherLabel)
                        .addComponent(fatherTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(motherLabel)
                        .addComponent(motherTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(spouseLabel)
                        .addComponent(spouseTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(childrenLabel)
                        .addComponent(childrenTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(grandChildrenLabel)
                        .addComponent(grandChildrenTextField))
        );

        //ADD button that uses a callback method with the addRelativeAction method
        JButton addRelative = new JButton("Add Relative");
        addRelative.addActionListener(new addRelativeAction(member));
        addRelative.setPreferredSize(new Dimension(120, 30));
        addRelative.setToolTipText("Add a new family member relative to the selected member.");
        
        //EDIT button that uses a callback method with the editMemberAction method
        JButton editMember = new JButton("Edit Details");
        editMember.addActionListener(new editMemberAction(member));
        editMember.setPreferredSize(new Dimension(100, 30));
        editMember.setToolTipText("Edit the details of the selected member.");
        
        //DEL button that uses a callback method with the deleteMemberAction method
        JButton delete = new JButton("Delete Member");
        delete.addActionListener(new deleteMemberAction(member));
        delete.setPreferredSize(new Dimension(120, 30));
        delete.setToolTipText("Deletes the selected member from the tree."); 
        
        //ROOT button that uses a callback method with the makeMemberRootAction method
        JButton makeRoot = new JButton("Set As Root");        
        makeRoot.addActionListener(new makeMemberRootAction(member));
        makeRoot.setPreferredSize(new Dimension(100, 30));
        makeRoot.setToolTipText("Sets the selected member as the root of the family tree.");
        
        //Set the layout of the bottom container
        JPanel btncontainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btncontainer.add(addRelative);
        btncontainer.add(editMember);
        btncontainer.add(delete);
        btncontainer.add(makeRoot);
        
        //Display infoPanel with btncontainer
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(btncontainer, gbc);
        infoPanel.validate();
        infoPanel.repaint();
    }
    
    //Constructs the window that is displayed in infoPanel when the ADD button is clicked while
    //viewing the details of a family member
    private void displayAddRelativeInfo(FamilyMember member) {
        tree.setEnabled(false);
        
        //Resets infoPanel
        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        //Sets title of add panel depending on whether the form is used to add a root or relative member
        JPanel info = new JPanel();
        JLabel memberInfoLabel = new JLabel("Enter Tree Root Information:", SwingConstants.LEFT);
        if (member != null) {
            memberInfoLabel.setText("Enter Relative Information:");
        }
        memberInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        //Setting a grid bag layout with specific constraints for the components laid out using GridBagLayout
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        infoPanel.add(memberInfoLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        infoPanel.add(info, gbc);

        //Apply group layout
        GroupLayout layout = new GroupLayout(info);
        info.setLayout(layout);
        layout.setAutoCreateGaps(true);

        //Set relative type label and input field
        JLabel relativeTypeLabel = new JLabel("Relative Type");
        DefaultComboBoxModel<String> relativeTypeList = new DefaultComboBoxModel<>();
        relativeTypeList.addElement("MOTHER");
        relativeTypeList.addElement("FATHER");
        relativeTypeList.addElement("SPOUSE");
        relativeTypeList.addElement("CHILD");
        JComboBox<String> relativeTypeComboBox = new JComboBox<>(relativeTypeList);
        
        //When setting the root (member is null), make the relative type combobox uneditable
        if (member == null) {
            relativeTypeComboBox.removeAllItems();
            relativeTypeComboBox.setEnabled(false);
        }
        
        //Set name labels and input fields
        JLabel firstNameLabel = new JLabel("Name");
        JTextField firstNameTextField = new JTextField("Meryl", 10);
        JLabel lastNameLabe = new JLabel("Surname");
        JTextField lastNameTextField = new JTextField("Streep", 10);
        JLabel maidenNameLabel = new JLabel("Maiden Name");
        JTextField maidenNameTextField = new JTextField(10);

        //Set gender label and input field
        JLabel genderLabel = new JLabel("Gender");
        DefaultComboBoxModel<String> genderList = new DefaultComboBoxModel<>();
        genderList.addElement("FEMALE");
        genderList.addElement("MALE");
        JComboBox<String> genderComboBox = new JComboBox<>(genderList);
        if (currentFamilyTree.hasRoot()) genderComboBox.setEnabled(false);
        //if adding the root member, allow for gender to be set
        //else default to mother and prevent gender from being changed unless relative type is changed

        //Set life description label and input field
        JLabel lifeDescriptionLabel = new JLabel("Life Description");
        JTextArea lifeDescriptionTextArea = new JTextArea("-NIL-",10, 10);
        lifeDescriptionTextArea.setLineWrap(true);
        lifeDescriptionTextArea.setWrapStyleWord(true);
        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);

        //Set address labels and input fields
        JLabel addressInfoLabel = new JLabel("Address:");
        addressInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        JLabel streetNoLabel = new JLabel("Street Number:");
        JTextField streetNoTextField = new JTextField("52", 10);
        JLabel streetNameLabel = new JLabel("Street Name:");
        JTextField streetNameTextField = new JTextField("Lakeside", 10);
        JLabel suburbLabel = new JLabel("Suburb");
        JTextField suburbTextField = new JTextField("Jurong", 10);
        JLabel postalCodeLabel = new JLabel("Postal Code");
        JTextField postalCodeTextField = new JTextField("643289", 10);

        //ActionListener added to relative type combobox to change the gender value
        //to an appropriate value if the gender-specific items were selected (Father/Mother)
        //Makes them editable as well
        relativeTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch ((String) relativeTypeComboBox.getSelectedItem()) {//check for a match
                    case "FATHER":
                        genderComboBox.setSelectedItem("MALE");
                        maidenNameTextField.setEditable(false);
                        genderComboBox.setEnabled(false);
                        lastNameTextField.setText(member.getLastName());
                        break;
                    case "MOTHER":
                        genderComboBox.setSelectedItem("FEMALE");
                        maidenNameTextField.setEditable(true);
                        genderComboBox.setEnabled(false);
                        lastNameTextField.setText(member.getLastName());
                        break;
                    case "SPOUSE":
                        lastNameTextField.setText(member.getLastName()); 
                        //when spouse is selected in combobox, set gender of new member to opposite of 
                        //selected member
                        if (member.getGender() == FamilyMember.Gender_FEMALE){
                            genderComboBox.setSelectedItem("MALE");
                            maidenNameTextField.setEditable(false);
                        } else {
                            genderComboBox.setSelectedItem("FEMALE");
                            maidenNameTextField.setEditable(true);
                        }                        
                        genderComboBox.setEnabled(false);
                        //setenabled
                        break;
                    case "CHILD":
                        lastNameTextField.setText(member.getLastName());
                        maidenNameTextField.setEditable(true);
                        genderComboBox.setEnabled(true);
                }
            }
        });
        
        //ActionListener added to gender combobox to change whether the maiden name field can be edited
        //Clears maiden name field and makes it uneditable if male combobox selection is made
        genderComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch ((String) genderComboBox.getSelectedItem()) {
                    case "FEMALE":
                    	maidenNameTextField.setEditable(true);
                        break;
                    case "MALE":
                        maidenNameTextField.setText("");
                        maidenNameTextField.setEditable(false);
                }
            }
        });
        
        //Alignment of all components given previous GroupLayout
        //Horizontal Alignment
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(relativeTypeLabel)
                        .addComponent(firstNameLabel)
                        .addComponent(lastNameLabe)
                        .addComponent(maidenNameLabel)
                        .addComponent(genderLabel)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNameLabel)
                        .addComponent(suburbLabel)
                        .addComponent(postalCodeLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(firstNameTextField)
                        .addComponent(relativeTypeComboBox)
                        .addComponent(lastNameTextField)
                        .addComponent(maidenNameTextField)
                        .addComponent(lifeDescriptionScrollPane1)
                        .addComponent(genderComboBox)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoTextField)
                        .addComponent(streetNameTextField)
                        .addComponent(suburbTextField)
                        .addComponent(postalCodeTextField)
                )
        );

        //Vertical Alignment
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(relativeTypeLabel)
                        .addComponent(relativeTypeComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(firstNameLabel)
                        .addComponent(firstNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastNameLabe)
                        .addComponent(lastNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maidenNameLabel)
                        .addComponent(maidenNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(lifeDescriptionScrollPane1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNoTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNameLabel)
                        .addComponent(streetNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suburbLabel)
                        .addComponent(suburbTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(postalCodeLabel)
                        .addComponent(postalCodeTextField))
        );

        //Callback method with ActionListener to validate the fields that user has entered
        //Method is tied to the Add Member button and calls the validateFields method
        JButton saveMember = new JButton("Add Member");
        saveMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Return an error message using the validateFields method
                //String will be blank if nothing is wr ong with input
                String errorMessage = FamilyMember.validateFields(firstNameTextField.getText(),
                        lastNameTextField.getText(), maidenNameTextField.getText(),
                        lifeDescriptionTextArea.getText(), streetNoTextField.getText(),
                        streetNameTextField.getText(), suburbTextField.getText(),
                        postalCodeTextField.getText(),
                        (String) genderComboBox.getSelectedItem(), (String) relativeTypeComboBox.getSelectedItem(),
                        member);
                
                //Display appropriate error message if string is not empty
                if(!errorMessage.equals("")) {
                    errorMessage = "One or more fields were not entered correctly. Please correct the following:\n"
                                    + errorMessage;
                    JOptionPane.showMessageDialog(null, errorMessage);
                } else {
                    //Initialize family member object so that it can be used to add a new member
                    Address newAddress = new Address(streetNoTextField.getText(), streetNameTextField.getText(),
                                            suburbTextField.getText(), postalCodeTextField.getText());
                    FamilyMember newMember = new FamilyMember(firstNameTextField.getText(),
                                    lastNameTextField.getText(), (String) genderComboBox.getSelectedItem(),
                                    newAddress, lifeDescriptionTextArea.getText());
                    newMember.setMaidenName(maidenNameTextField.getText());
                    //Checks if tree is empty, and uses it to either add a root member or relative member
                    if (member == null){
                        currentFamilyTree.setRoot(newMember);
                        editStatus("Root Added");
                    } else {
                        member.addRelative((String) relativeTypeComboBox.getSelectedItem(), newMember);
                        editStatus("New Member Added");
                    }
                    displayTree(currentFamilyTree);
                }
            }
        });
        
        //Callback method with ActionListener that calls cancelEditAction
        //Method is tied to the Cancel button
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new cancelEditAction(member));
        
        //Set layout of container and add buttons
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.setOpaque(false);
        container.add(saveMember);
        container.add(cancel);

        //Display infoPanel with container
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(container, gbc);
        infoPanel.validate();
        infoPanel.repaint();
    }
    
    //Constructs the window that is displayed in infoPanel when the EDIT button is clicked while
    //viewing the details of a family member
    private void displayEditMemberInfo(FamilyMember member) {
        tree.setEnabled(false);
        
        //Resets infoPanel
        infoPanel.removeAll();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        //Setting a grid bag layout with specific constraints for the components laid out using GridBagLayout
        JPanel info = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        infoPanel.add(info, gbc);
        
        //Apply group layout
        GroupLayout layout = new GroupLayout(info);
        info.setLayout(layout);
        layout.setAutoCreateGaps(true);

        //Set name labels and input fields
        JLabel memberInfoLabel = new JLabel("Personal Particulars: ");
        memberInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel firstNameLabel = new JLabel("Name");
        JTextField firstNameTextField = new JTextField(member.getFirstName(), 10);
        JLabel lastNameLabel = new JLabel("Surname");
        JTextField lastNameTextField = new JTextField(member.getLastName(), 10);
        JLabel maidenNameLabel = new JLabel("Maiden Name");
        JTextField maidenNameTextField = new JTextField(member.getMaidenName(), 10);
        //Prevent maiden name from being editable if member is male
        if (member.getGender() != FamilyMember.Gender_FEMALE) {
            maidenNameTextField.setEditable(false);
        }
        
        //Set gender label and input field
        JLabel genderLabel = new JLabel("Gender");
        DefaultComboBoxModel<String> genderList = new DefaultComboBoxModel<>();
        if (member.getGender() == FamilyMember.Gender_FEMALE){
            genderList.addElement("FEMALE");
        } else {
            genderList.addElement("MALE");
        }
        JComboBox<String> genderComboBox = new JComboBox<>(genderList);
        //Prevent gender of member from being changed as this may affect subsequent children, etc
        genderComboBox.setEnabled(false);

        //Set life description label and input field
        JLabel lifeDescriptionLabel = new JLabel("Life Description");
        JTextArea lifeDescriptionTextArea = new JTextArea(member.getLifeDescription(), 10, 10);
        lifeDescriptionTextArea.setLineWrap(true);
        lifeDescriptionTextArea.setWrapStyleWord(true);
        JScrollPane lifeDescriptionScrollPane1 = new JScrollPane(lifeDescriptionTextArea);
        
        //Set address labels and input fields
        JLabel addressInfoLabel = new JLabel("Address: ");
        addressInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel streetNoLabel = new JLabel("Street Number:");
        JTextField streetNoTextField = new JTextField(member.getAddress().getStreetNumber(), 10);
        JLabel streetNameLabel = new JLabel("Street Name:");
        JTextField streetNameTextField = new JTextField(member.getAddress().getStreetName(), 10);
        JLabel suburbLabel = new JLabel("Suburb");
        JTextField suburbTextField = new JTextField(member.getAddress().getSuburb(), 10);
        JLabel postalCodeLabel = new JLabel("Postal Code");
        JTextField postalCodeTextField = new JTextField(member.getAddress().getPostalCode() + "", 10);

        //Alignment of all components given previous GroupLayout
        //Horizontal Alignment
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(memberInfoLabel)
                        .addComponent(firstNameLabel)
                        .addComponent(lastNameLabel)
                        .addComponent(maidenNameLabel)
                        .addComponent(genderLabel)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNameLabel)
                        .addComponent(suburbLabel)
                        .addComponent(postalCodeLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(firstNameTextField)
                        .addComponent(lastNameTextField)
                        .addComponent(maidenNameTextField)
                        .addComponent(lifeDescriptionScrollPane1)
                        .addComponent(genderComboBox)
                        .addComponent(addressInfoLabel)
                        .addComponent(streetNoTextField)
                        .addComponent(streetNameTextField)
                        .addComponent(suburbTextField)
                        .addComponent(postalCodeTextField)
                )
        );

        //Vertical Alignment
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(memberInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(firstNameLabel)
                        .addComponent(firstNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastNameLabel)
                        .addComponent(lastNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(maidenNameLabel)
                        .addComponent(maidenNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(genderLabel)
                        .addComponent(genderComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lifeDescriptionLabel)
                        .addComponent(lifeDescriptionScrollPane1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addressInfoLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNoLabel)
                        .addComponent(streetNoTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(streetNameLabel)
                        .addComponent(streetNameTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(suburbLabel)
                        .addComponent(suburbTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(postalCodeLabel)
                        .addComponent(postalCodeTextField))
        );
        
        //Callback method with ActionListener to validate the fields that user has entered
        //Method is tied to the Save Details button and calls the validateFields method
        JButton saveMember = new JButton("Save Details");
        saveMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Return an error message using the validateFields method
                //String will be blank if nothing is wr ong with input
                String errorMessage = FamilyMember.validateFields(firstNameTextField.getText(),
                        lastNameTextField.getText(), maidenNameTextField.getText(),
                        lifeDescriptionTextArea.getText(), streetNoTextField.getText(),
                        streetNameTextField.getText(), suburbTextField.getText(),
                        postalCodeTextField.getText(),
                        "", "", member); 
                        //empty strings passed in lieu of gender and relativeType as they are not editable in edit view anyway

                //Display appropriate error message if string is not empty
                if(!errorMessage.equals("")) {
                    errorMessage = "One or more fields were not entered correctly. Please correct the following:\n"
                                    + errorMessage;
                    JOptionPane.showMessageDialog(null, errorMessage);
                } else {
                    //Use set methods to set the new values of member once all input has been validated
                    member.setFirstName(firstNameTextField.getText());
                    member.setLastName(lastNameTextField.getText());
                    member.setMaidenName(maidenNameTextField.getText());
                    member.setLifeDescription(lifeDescriptionTextArea.getText());
                    member.setGender((String) genderComboBox.getSelectedItem());

                    member.getAddress().setStreetNumber(streetNoTextField.getText());
                    member.getAddress().setStreetName(streetNameTextField.getText());
                    member.getAddress().setSuburb(suburbTextField.getText());
                    member.getAddress().setPostalCode(postalCodeTextField.getText());
                    
                    displayTree(currentFamilyTree);
                    editStatus("Family Member "+member.toString()+" Edited");
                }
            }
        });
        
        //Callback method with ActionListener that calls cancelEditAction
        //Method is tied to the Cancel button
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new cancelEditAction(member));
        
        //Set layout of container and add buttons
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.setOpaque(false);
        container.add(saveMember);
        container.add(cancel);
        
        //Display infoPanel with container
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        infoPanel.add(container, gbc);
        infoPanel.validate();
        infoPanel.repaint();
    }
    
    //Populates a given JTree object recursively using a given node and root FamilyMember object
    private void createTree(DefaultMutableTreeNode top, FamilyMember root) {
        //Declaration of all relational nodes
        DefaultMutableTreeNode parents = null;
        DefaultMutableTreeNode father = null;
        DefaultMutableTreeNode mother = null;
        DefaultMutableTreeNode spouse = null;
        DefaultMutableTreeNode children = null;
        DefaultMutableTreeNode child = null;
        DefaultMutableTreeNode spouseNode = null;

        //Adds parents to a given node in the JTree
        if (root.has(FamilyMember.Relation_PARENTS) && root == currentFamilyTree.getRoot()) {
            parents = new DefaultMutableTreeNode("Parents");
            top.add(parents);

            if (root.has(FamilyMember.Relation_FATHER)) {
                father = new DefaultMutableTreeNode(root.getFather());
                //add father to parent node
                parents.add(father);
            }

            if (root.has(FamilyMember.Relation_MOTHER)) {
                mother = new DefaultMutableTreeNode(root.getMother());
                //add mother to parent node
                parents.add(mother);
            }
        }
                
        //Adds spouse for a given node in the JTree
        if (root.has(FamilyMember.Relation_SPOUSE)) {
            spouseNode = new DefaultMutableTreeNode("Spouse");
            spouse = new DefaultMutableTreeNode(root.getSpouse());
            //add spouse node
            spouseNode.add(spouse);
            //add the spouse node 
            top.add(spouseNode);
        }

        //Recursively adds all children (which will in turn add parent/spouse) for a given node in the JTree
        if (root.has(FamilyMember.Relation_CHILDREN)) {
            children = new DefaultMutableTreeNode("Children");
            //For each loop runs through all children in the ArrayList
            for (FamilyMember f : root.getChildren()) {
                child = new DefaultMutableTreeNode(f);
                createTree(child, f);
                children.add(child);
            }
            top.add(children);
        }
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
    
 
    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    SERIALIZING
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Uses FileInputStream and ObjectInputStream to read a given file into a FamilyTree object
    private void openFile(File file) {
        FileInputStream fileIS = null;
        ObjectInputStream objectIS = null;
        FamilyTree ft = null;
        
        try {
            //Read the objects and close the streams
            fileIS = new FileInputStream(file);
            objectIS = new ObjectInputStream(fileIS);
            ft = (FamilyTree) objectIS.readObject();
            fileIS.close();
            objectIS.close();

            //Set the program variables to the file variables
            currentFamilyTree.setRoot(ft.getRoot());
            currentFile = file;
            tree = new JTree();
        } catch (Exception f) {
            throw new IllegalArgumentException("File could not be read.");
        }

    }

    //Uses FileOutputStream and ObjectOutputStream to write a given tree to a FamilyTree file
    private void saveToFile(File file) {
        FileOutputStream fileOS = null;
        ObjectOutputStream objectOS = null;
        
        try {
            fileOS = new FileOutputStream(file);
            objectOS = new ObjectOutputStream(fileOS);
            objectOS.writeObject(this.currentFamilyTree);
            fileOS.close();
            objectOS.close();
            currentFile = file;
        } catch (Exception ex) {
            throw new IllegalArgumentException("File could not be saved");
        }
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
    
    
    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    REPORTING SYSTEM STATUS & ACTIVITY
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////        
    //Method that accepts an error message string and displays it to the user in a JOptionPane prompt
    //Returns true if the user selected Yes
    private boolean promptContinue(String msg) {
        if (currentFamilyTree.hasRoot()) {
            int dialogResult = JOptionPane.showConfirmDialog(viewPanelFrame, msg, "Warning!", JOptionPane.YES_NO_OPTION);
            return dialogResult == JOptionPane.YES_OPTION;
        }
        return true;
    }      

    //Uses a JOptionPane to print an error message from a given exception parameter
    private void showErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(viewPanelFrame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    //Method that changes the label of the status bar to report on system activity once an action is taken
    private void editStatus(String status) {
        statusLabel.setText(status);
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
    
    
    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    INNER CLASSES FOR CALLBACK FUNCTIONS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //The following classes all implement the ActionListener interface
    //and when instantiated will execute the actionPerformed method
    
    //Used to call the displayAddRelativeInfo method which constructs the add form for a new member based on a given member
    //BUTTON: "ADD" while VIEWING a family member's details
    private class addRelativeAction implements ActionListener {
        private FamilyMember member;
        //Accepts a FamilyMember object as a parameter which is then modified by actionPerformed
        public addRelativeAction(FamilyMember member) {
            this.member = member;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            displayAddRelativeInfo(member);
        }
    }

    //Used to call the displayEditMemberInfo method which constructs the edit details form for an existing member based on a given member
    //BUTTON: "EDIT" while VIEWING a family member's details
    private class editMemberAction implements ActionListener {
        private FamilyMember member;
        //Accepts a FamilyMember object as a parameter which is then modified by actionPerformed
        public editMemberAction(FamilyMember member) {
            this.member = member;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            displayEditMemberInfo(member);
        }
    }
 
    //Used to call the searchMember method which looks for a given member, and then deletes it from the tree
    //BUTTON: "DEL" while VIEWING a family member's details
    private class deleteMemberAction implements ActionListener{
        private FamilyMember member;
        //Accepts a FamilyMember object as a parameter which is then modified by actionPerformed
        public deleteMemberAction(FamilyMember member) {
            this.member = member;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            FamilyMember root = currentFamilyTree.getRoot();
            if (promptContinue("Deleting a member with relatives or children that are NOT stored as relatives of other members"
                    + " will remove them from the tree as well.\n\nContinue?")){
                if(root.isEqual(member)) {
                    currentFamilyTree.setRoot(null);
                    displayTree(currentFamilyTree);
                    JOptionPane.showMessageDialog(null, "Deletion Successful.");
                }else {
                    //Recursive calls searchMember method until the given member is found 
                    //searchMember method already deletes the given member from the tree
                    if(FamilyTree.searchMember(root, member, true)) {
                        currentFamilyTree.setRoot(root); //refresh tree with root
                        displayTree(currentFamilyTree);
                        infoPanel.removeAll();
                        JOptionPane.showMessageDialog(null, "Deletion Successful.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Unable to delete member as member exists only as a reference, and not in the tree.");
                    }
                }                        
            }
        }
    }
    
    //Used to call the searchMember method which looks for a given member, and uses it to set a new tree with it as the root
    //BUTTON: "ROOT" while VIEWING a family member's details
    private class makeMemberRootAction implements ActionListener{
        private FamilyMember member;
        //Accepts a FamilyMember object as a parameter which is then modified by actionPerformed
        public makeMemberRootAction(FamilyMember member) {
            this.member = member;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {                
            FamilyMember root = currentFamilyTree.getRoot();

            //Prompt user for confirmation
            if (promptContinue("This function will set the selected member as the root of the tree."
                    + " This change is irreversible.\n\nContinue?")){
                //Recursive calls searchMember method until the given member is found 
                if(FamilyTree.searchMember(root, member, false)) {
                    //Since member exists in the tree, set it as the new root of the tree with setRoot method
                    currentFamilyTree.setRoot(member);
                    displayTree(currentFamilyTree);
                    JOptionPane.showMessageDialog(null, "Root Change Successful.");
                } else {
                    JOptionPane.showMessageDialog(null, "Unable to make member root as member exists only as a reference, and not in the tree.");
                }                      
            }           
        }
    }
    
    //Used to call the displayMemberInfo method on a given member, allowing the user to escape the edit details window for a member
    //to the main infoPanel view showing family member details
    //BUTTON: "Cancel"  while EDITING a family member's details
    private class cancelEditAction implements ActionListener {
        FamilyMember member;
        //Accepts a FamilyMember object as a parameter which is then modified by actionPerformed
        public cancelEditAction(FamilyMember member) {
            this.member = member;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //Check that member isn't null
            if (member != null){
                displayMemberInfo(member);
            } else {
                //Prevent the user from escaping the edit details panel (which is shown automatically when an empty tree is created)
                //Until the tree has a root
                JOptionPane.showMessageDialog(null, "Please add a root first.");
                displayTree(currentFamilyTree);
            }
            editStatus("Action Canceled");
        }
    }
    
    //Used to call the displayTree method on an empty tree 
    //BUTTON: "New" in the menu bar
    private class createTreeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (promptContinue("Continue? All unsaved changes will be lost.")) {
                currentFamilyTree = new FamilyTree();
                currentFile = null;
                displayTree(currentFamilyTree);
                editStatus("Blank Tree Created");
            }
        }
    }

    //Used to load a new JFileChooser which allows the user to open an FamilyTree file and display it with the displayTree method
    //BUTTON: "Open" in the menu bar
    private class openTreeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (promptContinue("Continue? All unsaved changes will be lost.")) {
                JFileChooser fileChooser = new JFileChooser();
                //Set the types of files accepted
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("FamilyTree Files (*.ft)", "ft"));
                fileChooser.setAcceptAllFileFilterUsed(true);

                int result = fileChooser.showOpenDialog(viewPanelFrame);
                //If an option was selected
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {                    
                        openFile(fileChooser.getSelectedFile()); //Open the file selected by user
                        displayTree(currentFamilyTree); //Display the tree based on the file loaded
                        editStatus("File Opened: " + (fileChooser.getSelectedFile().getAbsolutePath()));
                    } catch (Exception f) {
                        showErrorDialog(f);
                        editStatus("Error: " + f.getMessage());
                    }
                }
            }
        }
    }
    
    //Used to call the saveToFile method allowing a file to be saved to the chosen directory
    //BUTTON: "Save" in the menu bar
    private class saveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (currentFile != null) {
                    saveToFile(currentFile);
                    editStatus("File Saved: " + currentFile.getPath());
                } else {
                    editStatus("Save Failed");
                    //Call a saveAsAction method instead if the currentFile does not exists
                    ActionListener listner = new saveAsAction();
                    listner.actionPerformed(e);
                }
            } catch (Exception f) {
                showErrorDialog(f);
                editStatus("Error: "+ f.getMessage());
            }
        }
    }

    //Used to call the saveToFile method allowing a file to be saved to the chosen directory
    //BUTTON: "Save As" in the menu bar
    private class saveAsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser() {
                private static final long serialVersionUID = 1L;
                //Override the method called by the UI when the user hits the Approve button to overwrite an existing file
                @Override
                public void approveSelection() {
                    File selectedFile = getSelectedFile();
                    //Check if selected file exists
                    if (selectedFile.exists() && getDialogType() == SAVE_DIALOG) {
                        int result = JOptionPane.showConfirmDialog(this, "Overwrite the existing file?", "Warning", JOptionPane.YES_NO_OPTION);
                        //Prompt user for confirmation, only approve the overwrite if YES was selected by user
                        switch (result) {
                            case JOptionPane.YES_OPTION:
                                super.approveSelection();
                                return;
                            case JOptionPane.NO_OPTION:
                                return;
                            case JOptionPane.CLOSED_OPTION:
                                return;
                        }
                    }
                    super.approveSelection();
                }
            };
            
            //Set the types of files accepted
            fileChooser.setSelectedFile(new File("Family Tree.ft"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("FamilyTree Files (*.ft)", "ft"));
            
            int result = fileChooser.showSaveDialog(viewPanelFrame);
            //Prompt user before saving
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String filename = fileChooser.getSelectedFile().toString();
                    //Ensure that file name ends with appropriate extension
                    if (!filename.endsWith(".ft")) {
                        filename += ".ft";
                    }
                    File file = new File(filename);
                    //Save the file and display the current tree again
                    saveToFile(file);
                    displayTree(currentFamilyTree);
                    editStatus("File Saved: " + (file.getAbsolutePath()));
                } catch (Exception f) {
                    showErrorDialog(f);
                    editStatus("Error: "+ f.getMessage());
                }
            }
        }
    }

    //The following method implements the TreeSelectionListener interface
    
    //Used to call the displayMemberInfo method when the user selects a node in the tree
    //BUTTON: Selectable node from the JTree
    private class treeSelectorAction implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent event) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            //Initialize a node to the last selected component in the tree

            //Check if a selection was at least made
            if (node == null) {
                return;
            }

            //Ensures that the selection was a FamilyMember object before proceding to call displayMemberInfo
            Object nodeInfo = node.getUserObject();
            if (nodeInfo instanceof FamilyMember) {
                displayMemberInfo((FamilyMember) nodeInfo);
                editStatus("Details Displayed: " + ((FamilyMember) nodeInfo));
            }
        }
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
}