
import interfaces.interfacefile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

public class Main extends javax.swing.JFrame {

 //String defaultPath = "C:\\Users\\SAMSUNG\\Music\\FileManager\\FileManager_Server\\ServerStorage";
    String defaultPath = "";
    int arrowWidth = 8;

    interfacefile getfunctions = (interfacefile) Naming.lookup("rmi://localhost:1099/frameoperations");
    JPanel contentPane = new JPanel();
    JPanel navigationPane = new JPanel();

    private static ArrayList<JPanel> selectedElementBoxes = new ArrayList<>();
    private static ArrayList<JPanel> selectedContentElementBoxes = new ArrayList<>();
    static int idv;
    public Main(int id) throws Exception {
        defaultPath = getfunctions.getpaths();
        System.out.println(defaultPath);
        initComponents();
       getfunctions.init(defaultPath);
        idv = id;
        System.out.println("welcome :"+idv);
        List<String>users = getfunctions.getusers(idv);
        for(int i = 0;i< users.size();i++){
            combo.addItem(users.get(i));
        }
        deleteItem.setEnabled(false);
        renameItem.setEnabled(false);
        open.setEnabled(false);
        download.setEnabled(false);
        this.setLocationRelativeTo(null);
        contentPane.setLayout(new GridLayout(ERROR, 1));
        navigationPane.setLayout(new GridLayout(ERROR, 1));
        addToNavigationPane(defaultPath, "", 0, 0);
        addToContentPane(defaultPath);
    }

    public void onFileSaved(String filePath, String content) throws RemoteException {
        getfunctions.saveFile(filePath, content);
    }

    private void addToNavigationPane(String path, String indexID, int rowIndex, int columnIndex) throws Exception {
        File[] files = getfunctions.showfiles(path,idv);
        int x = 1;
        for (File f : files) {
            if (getfunctions.isDirectory(f.getPath())) {

                JPanel elementBox = new JPanel();
                JButton arrowButton = new JButton();
                JLabel elementIcon = new JLabel();
                JLabel elementName = new JLabel();

                elementBox.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 1));
                elementBox.setBorder(new EmptyBorder(0, 20 * columnIndex, 0, 0));

                arrowButton.setPreferredSize(new Dimension(arrowWidth, arrowWidth));
                arrowButton.setEnabled(false);
                arrowButton.setContentAreaFilled(false);
                arrowButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                ImageIcon image;

                if (getfunctions.checkSubDirectory(f.getPath())) {
                    image = new ImageIcon(System.getProperty("user.dir") + "\\icons\\right-arrow.png");
                    Image convertedImage = image.getImage();
                    Image resizedImage = convertedImage.getScaledInstance(arrowWidth, arrowWidth, Image.SCALE_SMOOTH);

                    arrowButton.setEnabled(true);
                    arrowButton.setIcon(new ImageIcon(resizedImage));
                }
                elementBox.add(arrowButton);

                Image imageIcon = new ImageIcon(System.getProperty("user.dir") + "\\icons\\folder.png").getImage();
                Image resizedImage = imageIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                elementIcon.setIcon(new ImageIcon(resizedImage));
                elementIcon.setPreferredSize(new Dimension(30, 30));

                elementName.setText(f.getName());
                elementName.putClientProperty("path", f.getPath());
                elementBox.add(elementIcon);
                elementBox.add(elementName);

                //Expand & Collapse Button
                arrowButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Object parentPanel = arrowButton.getParent();
                        JPanel panel = (JPanel) parentPanel;
                        String indexID = (String) panel.getClientProperty("indexID");
                        String filePath = String.valueOf(f);
                        int fileIndex = navigationPane.getComponentZOrder(panel);

                        if (arrowButton.getClientProperty("expanded") == null || !(boolean) arrowButton.getClientProperty("expanded")) {
                            try {
                                // Expand
                                addToNavigationPane(filePath, indexID, fileIndex + 1, columnIndex + 1);
                            } catch (Exception ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            arrowButton.putClientProperty("expanded", true);
                        } else {
                            // Collapse
                            for (int i = navigationPane.getComponentCount() - 1; i >= 0; i--) {
                                JPanel panel2 = (JPanel) navigationPane.getComponent(i);
                                String toRemove = (String) panel2.getClientProperty("indexID");
                                if (toRemove.startsWith(indexID)) {
                                    if (!toRemove.equals(indexID)) {
                                        navigationPane.remove(i);
                                    }
                                }
                            }
                            leftScrollPane.setViewportView(navigationPane);
                            arrowButton.putClientProperty("expanded", false);
                        }
                    }
                });
                String a = indexID + x;
                x++;
                elementBox.putClientProperty("indexID", a);
                //Element selection functions
                elementBox.putClientProperty("selected", false);
                elementBox.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            handleSelection(elementBox);
                        } catch (RemoteException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                navigationPane.add(elementBox, rowIndex);
                rowIndex++;
                leftScrollPane.setViewportView(navigationPane);
            }
        }
    }

    private void addToContentPane(String path) throws RemoteException,Exception {
        deleteItem.setEnabled(false);
        renameItem.setEnabled(false);
        open.setEnabled(false);
        download.setEnabled(false);
        contentPane.removeAll();
        contentPane.revalidate();
        contentPane.repaint();
        rightScrollPane.setViewportView(contentPane);
        File[] directoryItems = getfunctions.showfiles(path,idv);
        for (File f : directoryItems) {

            JPanel elementBox = new JPanel();
            JLabel elementIcon = new JLabel();
            JLabel elementName = new JLabel();

            ImageIcon image;
            if (getfunctions.isDirectory(f.getPath())) {
                image = new ImageIcon(System.getProperty("user.dir") + "\\icons\\folder.png");

            } else {
                image = new ImageIcon(System.getProperty("user.dir") + "\\icons\\file.png");
            }

            Image convertedImage = image.getImage();
            Image resizedImage = convertedImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);

            elementIcon.setIcon(new ImageIcon(resizedImage));

            elementIcon.setPreferredSize(new Dimension(30, 30));
            elementName.setText(f.getName());
            elementBox.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));

            elementBox.add(elementIcon, BorderLayout.CENTER);
            elementBox.add(elementName, BorderLayout.SOUTH);

            elementBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        try {
                            openSelectedItem(elementBox);
                        } catch (RemoteException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        handleContentSelection(elementBox);
                    }
                }
            });
            elementName.putClientProperty("path", f.getPath());
            contentPane.add(elementBox);
            rightScrollPane.setViewportView(contentPane);

        }
        pathLabel.setText(path.replace(defaultPath, ""));
    }

    private void handleSelection(JPanel elementBox) throws RemoteException,Exception {
        for (JPanel selectedBox : selectedElementBoxes) {
            selectedBox.setBackground(null);
            selectedBox.putClientProperty("selected", false);
        }
        selectedElementBoxes.clear();

        Boolean isSelected = (Boolean) elementBox.getClientProperty("selected");
        if (isSelected != null && isSelected) {
            elementBox.setBackground(null);
        } else {
            elementBox.setBackground(Color.GRAY);
            selectedElementBoxes.add(elementBox);
        }
        elementBox.putClientProperty("selected", isSelected != null ? !isSelected : true);

        String path = (String) ((JLabel) elementBox.getComponent(2)).getClientProperty("path");

        addToContentPane(path);
    }

    private void handleContentSelection(JPanel elementBox) {
        for (JPanel selectedBox : selectedContentElementBoxes) {
            selectedBox.setBackground(null);
            selectedBox.putClientProperty("selected", false);
        }
        selectedContentElementBoxes.clear();

        Boolean isSelected = (Boolean) elementBox.getClientProperty("selected");
        if (isSelected != null && isSelected) {
            elementBox.setBackground(null);
        } else {
            elementBox.setBackground(Color.GRAY);
            selectedContentElementBoxes.add(elementBox);
        }
        elementBox.putClientProperty("selected", isSelected != null ? !isSelected : true);

        deleteItem.setEnabled(true);
        renameItem.setEnabled(true);
        open.setEnabled(true);
        download.setEnabled(true);
    }

    private void openSelectedItem(JPanel elementBox) throws RemoteException {
        String path = (String) ((JLabel) elementBox.getComponent(1)).getClientProperty("path");
        try {
            if (getfunctions.openElement(path,idv)) {
                
                addToContentPane(path);
            } else {
                new fileEditor(path, getfunctions.readFile(path), this).setVisible(true);
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        controller = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        rightScrollPane = new javax.swing.JScrollPane();
        leftScrollPane = new javax.swing.JScrollPane();
        goaBack = new javax.swing.JButton();
        pathLabel = new javax.swing.JLabel();
        createFolder = new javax.swing.JButton();
        deleteItem = new javax.swing.JButton();
        renameItem = new javax.swing.JButton();
        createFile = new javax.swing.JButton();
        quit = new javax.swing.JButton();
        uploadFile = new javax.swing.JButton();
        uploadFolder = new javax.swing.JButton();
        download = new javax.swing.JButton();
        open = new javax.swing.JButton();
        share = new javax.swing.JButton();
        combo = new javax.swing.JComboBox<>();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        controller.setBackground(new java.awt.Color(51, 255, 153));

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        goaBack.setText("<");
        goaBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goaBackActionPerformed(evt);
            }
        });

        pathLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pathLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(leftScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rightScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(goaBack, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(leftScrollPane))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(pathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(goaBack, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(rightScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout controllerLayout = new javax.swing.GroupLayout(controller);
        controller.setLayout(controllerLayout);
        controllerLayout.setHorizontalGroup(
            controllerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controllerLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        controllerLayout.setVerticalGroup(
            controllerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        createFolder.setText("Créer dossier");
        createFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createFolderActionPerformed(evt);
            }
        });

        deleteItem.setText("Supprimer");
        deleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItemActionPerformed(evt);
            }
        });

        renameItem.setText("Renommer");
        renameItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameItemActionPerformed(evt);
            }
        });

        createFile.setText("Créer fichier");
        createFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createFileActionPerformed(evt);
            }
        });

        quit.setText("Quitter");
        quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitActionPerformed(evt);
            }
        });

        uploadFile.setText("Upload file");
        uploadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadFileActionPerformed(evt);
            }
        });

        uploadFolder.setText("Upload folder");
        uploadFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadFolderActionPerformed(evt);
            }
        });

        download.setText("Télécharger");
        download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadActionPerformed(evt);
            }
        });

        open.setText("Ouvrir");
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });

        share.setText("Partager");
        share.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shareActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(controller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(quit, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(open, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(renameItem, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createFile, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uploadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uploadFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(download, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(share, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controller, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(share, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(download, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uploadFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uploadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createFile, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(renameItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(open, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {createFile, createFolder, deleteItem, open, quit, renameItem, uploadFile});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goaBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goaBackActionPerformed
        String laaabel = pathLabel.getText();
        int lastIndex = laaabel.lastIndexOf("\\");
        if (lastIndex != -1) {
            laaabel = laaabel.substring(0, lastIndex);
        }
        try {
            addToContentPane(defaultPath + laaabel);
            refresh();
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_goaBackActionPerformed

    private void createFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFolderActionPerformed
        String folderName = JOptionPane.showInputDialog(this, "Entrez le nom du dossier que vous souhaitez créer");
        if (folderName != null) {
            String newFolder = defaultPath + pathLabel.getText() + "\\" + folderName;
            String news =  defaultPath + pathLabel.getText() ;
            try {
                int status = getfunctions.createFolder(news,newFolder,idv);
                if (status == 0) {
                    JOptionPane.showMessageDialog(this, "Fichier du meme nom existe déja");
                } else if (status == 3) {
                    JOptionPane.showMessageDialog(this, "Dossier existe déja");

                } else if (status == 2) {
                    JOptionPane.showMessageDialog(this, "Échec de création du Dossier");
                } else {
                    refresh();
                }

            } catch (RemoteException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_createFolderActionPerformed

    private void deleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemActionPerformed
        String itemName = ((JLabel) selectedContentElementBoxes.getFirst().getComponent(1)).getText();
        String itemPath = defaultPath + pathLabel.getText() + "\\" + itemName;
        try {
            boolean status = getfunctions.deleteElement(itemPath,1,false,0);
            if (status) {
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Echec de la suppression");
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deleteItemActionPerformed

    private void renameItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameItemActionPerformed
        String itemName = ((JLabel) selectedContentElementBoxes.getFirst().getComponent(1)).getText();
        String currentItemPath = defaultPath + pathLabel.getText() + "\\" + itemName;
        String newName = JOptionPane.showInputDialog(this, "Entrez le nouveau nom", itemName);
        if (newName != null) {
            String newItemPath = defaultPath + pathLabel.getText() + "\\" + newName;
            try {
                boolean status = getfunctions.renameElement(currentItemPath, newItemPath,1);
                if (!status) {
                    JOptionPane.showMessageDialog(this, "Échec");
                } else {
                    refresh();
                }
            } catch (RemoteException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_renameItemActionPerformed

    private void createFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFileActionPerformed
        String fileName = JOptionPane.showInputDialog(this, "Entrez le nom du fichier que vous souhaitez créer");
        String filePath = defaultPath + pathLabel.getText() + "\\" + fileName;
        String paths = defaultPath + pathLabel.getText();
        try {
            int status = getfunctions.createFile(paths,filePath,idv);
            if (status == 0) {
                JOptionPane.showMessageDialog(this, "Fichier exists déja");
            } else if (status == 2) {
                JOptionPane.showMessageDialog(this, "Échec de création de fichier");
            } else {
                refresh();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_createFileActionPerformed

    private void quitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitActionPerformed
        this.dispose();
    }//GEN-LAST:event_quitActionPerformed

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
        String fileName = ((JLabel) selectedContentElementBoxes.getFirst().getComponent(1)).getText();
        String filePath = defaultPath + pathLabel.getText() + "\\" + fileName;
        try {
            if (getfunctions.openElement(filePath,idv)) {
                addToContentPane(filePath);
            } else {
                new fileEditor(filePath, getfunctions.readFile(filePath), this).setVisible(true);
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openActionPerformed

    private void uploadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadFileActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        byte[] pixels = null;
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            File chosenFile = selectedFile;
            String fileName = chosenFile.getName();
            try (FileInputStream in = new FileInputStream(chosenFile)) {
                pixels = new byte[in.available()];
                in.read(pixels);
                getfunctions.ulpoad(pathLabel.getText(), fileName, pixels);
                refresh();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("No file selected.");
        }
    }//GEN-LAST:event_uploadFileActionPerformed

    private void uploadFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadFolderActionPerformed
        JFileChooser folderChooser = new JFileChooser();
        byte[] pixels = null;
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = folderChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = folderChooser.getSelectedFile();
            Path selectedFolderPath = selectedFolder.toPath();
            String folderName = selectedFolder.getName();
            Path zipPath = Paths.get(System.getProperty("java.io.tmpdir") + "tempFolderToUpload.zip");
            try {
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
                Files.walkFileTree(selectedFolderPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        String entryName = selectedFolderPath.relativize(file).toString();
                        zos.putNextEntry(new ZipEntry(folderName + "/" + entryName));
                        Files.copy(file, zos);
                        zos.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }
                });
                zos.close();
                FileInputStream in = new FileInputStream(new File(zipPath.toString()));
                pixels = new byte[in.available()];
                in.read(pixels);
                getfunctions.uploadFolder(pathLabel.getText(), folderName, pixels);
                refresh();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("No folder selected.");
        }

    }//GEN-LAST:event_uploadFolderActionPerformed

    private void downloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadActionPerformed
        String itemName = ((JLabel) selectedContentElementBoxes.getFirst().getComponent(1)).getText();
        String itemPath = pathLabel.getText() + "\\" + itemName;
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Destination");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                Path extractPath = selectedFile.toPath();
                if (getfunctions.isDirectory(defaultPath + "\\" + itemPath)) {
                    byte[] pixels = getfunctions.download(itemPath);
                    File outputFile = new File(System.getProperty("java.io.tmpdir"), "tempFolderToUpload.zip");
                    FileOutputStream out = new FileOutputStream(outputFile);
                    byte[] pxl = pixels;
                    out.write(pxl);
                    out.close();
                    ZipInputStream zis = new ZipInputStream(new FileInputStream(outputFile));
                    ZipEntry zipEntry = zis.getNextEntry();
                    while (zipEntry != null) {
                        Path newPath = extractPath.resolve(zipEntry.getName());
                        if (zipEntry.isDirectory()) {
                            Files.createDirectories(newPath);
                        } else {
                            Files.createDirectories(newPath.getParent());
                            try (OutputStream os = Files.newOutputStream(newPath)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = zis.read(buffer)) > 0) {
                                    os.write(buffer, 0, len);
                                }
                            }
                        }
                        zipEntry = zis.getNextEntry();
                    }
                    zis.closeEntry();
                } else {
                    File outputFile = new File(selectedFile.getPath());
                    FileOutputStream out = new FileOutputStream(outputFile + "\\" + itemName);
                    byte[] pxl = getfunctions.download(itemPath);
                    out.write(pxl);
                    out.close();
                    System.out.println("File saved ");
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_downloadActionPerformed

    private void shareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shareActionPerformed
       String itemName = ((JLabel) selectedContentElementBoxes.getFirst().getComponent(1)).getText();
        String itemPath = pathLabel.getText() + "\\" + itemName;
        String filePath = defaultPath + itemPath;
        String paths = defaultPath + pathLabel.getText();
        /*   String path = pathLabel.getText();
        String elementName = ((JLabel) selectedContentElementBoxes.getFirst().getComponent(1)).getText();*/
  
        try{
           String r = combo.getSelectedItem()+"";
        getfunctions.partagefile(paths, filePath,r, idv);
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_shareActionPerformed

    private void refresh() throws RemoteException {
        
        try {
            addToContentPane(defaultPath + pathLabel.getText());
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                   
                try {
              
                    new Main(idv).setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JPanel controller;
    private javax.swing.JButton createFile;
    private javax.swing.JButton createFolder;
    private javax.swing.JButton deleteItem;
    private javax.swing.JButton download;
    private javax.swing.JButton goaBack;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JScrollPane leftScrollPane;
    private javax.swing.JButton open;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JButton quit;
    private javax.swing.JButton renameItem;
    private javax.swing.JScrollPane rightScrollPane;
    private javax.swing.JButton share;
    private javax.swing.JButton uploadFile;
    private javax.swing.JButton uploadFolder;
    // End of variables declaration//GEN-END:variables
}
