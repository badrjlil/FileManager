
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

public class Main extends javax.swing.JFrame {

    String defaultPath = System.getProperty("user.dir") + "\\ServerStorage";
    int arrowWidth = 8;

    JPanel contentPane = new JPanel();
    JPanel navigationPane = new JPanel();

    ArrayList<String> folderUrls = new ArrayList<>();
    private static ArrayList<JPanel> selectedElementBoxes = new ArrayList<>();
    private static ArrayList<JPanel> selectedContentElementBoxes = new ArrayList<>();

    public Main() {
        initComponents();
        this.setLocationRelativeTo(null);
        contentPane.setLayout(new GridLayout(ERROR, 1));
        navigationPane.setLayout(new GridLayout(ERROR, 1));
        addToNavigationPane(defaultPath, "", 0, 0);
        addToContentPane(defaultPath);
    }

    private void addToNavigationPane(String path, String indexID, int rowIndex, int columnIndex) {
        File[] files = new File(path).listFiles(file -> file.isDirectory());
        sortByFolders(files);
        int x = 1;
        for (File f : files) {
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

            if (f.listFiles(file -> file.isDirectory()).length > 0) {
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
                        // Expand
                        addToNavigationPane(filePath, indexID, fileIndex + 1, columnIndex + 1);
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
                    handleSelection(elementBox);
                }
            });

            navigationPane.add(elementBox, rowIndex);
            rowIndex++;
            leftScrollPane.setViewportView(navigationPane);

        }
    }

    private void sortByFolders(File[] files) {
        if (files != null) {
            java.util.Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    if (file1.isDirectory() && !file2.isDirectory()) {
                        return -1;
                    } else if (!file1.isDirectory() && file2.isDirectory()) {
                        return 1;
                    } else {
                        return file1.getName().compareToIgnoreCase(file2.getName());
                    }
                }
            });
        }
    }

    private void addToContentPane(String path) {
        contentPane.removeAll();
        contentPane.revalidate();
        contentPane.repaint();
        rightScrollPane.setViewportView(contentPane);
        File[] directoryItems = new File(path).listFiles();
        sortByFolders(directoryItems);
        for (File f : directoryItems) {

            JPanel elementBox = new JPanel();
            JLabel elementIcon = new JLabel();
            JLabel elementName = new JLabel();

            ImageIcon image;
            if (f.isDirectory()) {
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
                        // Perform double-click action
                        openSelectedItem(elementBox);
                    } else {
                        // Handle normal selection
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

    private void handleSelection(JPanel elementBox) {
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
            selectedBox.setBackground(null); // Reset background to deselect
            selectedBox.putClientProperty("selected", false);
        }
        selectedContentElementBoxes.clear();

        Boolean isSelected = (Boolean) elementBox.getClientProperty("selected");
        if (isSelected != null && isSelected) {
            elementBox.setBackground(null);
        } else {
            elementBox.setBackground(Color.GRAY); // Change to desired selection color
            selectedContentElementBoxes.add(elementBox);
        }
        elementBox.putClientProperty("selected", isSelected != null ? !isSelected : true);
    }

    private void openSelectedItem(JPanel elementBox) {
        String path = (String) ((JLabel) elementBox.getComponent(1)).getClientProperty("path");
        File f = new File(path);
        if (f.isDirectory()) {
            addToContentPane(path);
        } else if (f.isFile()) {
            new fileEditor(path).setVisible(true);
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
        open = new javax.swing.JButton();

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

        open.setText("Ouvrir");
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(controller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(createFile, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(deleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(createFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(renameItem, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(quit, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(open, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controller, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(createFile, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(createFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(renameItem, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(open, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(quit, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goaBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goaBackActionPerformed
        String laaabel = pathLabel.getText();
        int lastIndex = laaabel.lastIndexOf("\\");
        if (lastIndex != -1) {
            laaabel = laaabel.substring(0, lastIndex);
        }
        addToContentPane(defaultPath + laaabel);
    }//GEN-LAST:event_goaBackActionPerformed

    private void createFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFolderActionPerformed
        String folderName = JOptionPane.showInputDialog(this, "Entrez le nom du dossier que vous souhaitez créer");
        if (folderName != null) {
            String newFolder = defaultPath + pathLabel.getText() + "\\" + folderName;
            File folder = new File(newFolder);
            if (!folder.exists()) {
                boolean created = folder.mkdir();
                if (created) {
                    addToContentPane(defaultPath + pathLabel.getText());
                    System.out.println(newFolder + " Folder created");
                } else {
                    JOptionPane.showMessageDialog(this, "Échec de la création du dossier");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Le dossier existe déjà");
            }
        }
    }//GEN-LAST:event_createFolderActionPerformed

    private void deleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemActionPerformed
        String itemName = ((JLabel) selectedContentElementBoxes.getFirst().getComponent(1)).getText();
        String itemPath = defaultPath + pathLabel.getText() + "\\" + itemName;
        File file = new File(itemPath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println(defaultPath + pathLabel.getText() + "\\" + itemName + " Deleted");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la suppression d'élement");
            }
            addToContentPane(defaultPath + pathLabel.getText());
        }
    }//GEN-LAST:event_deleteItemActionPerformed

    private void renameItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameItemActionPerformed
        String itemName = ((JLabel) selectedContentElementBoxes.getFirst().getComponent(1)).getText();
        String currentItemPath = defaultPath + pathLabel.getText() + "\\" + itemName;
        String newName = JOptionPane.showInputDialog(this, "Entrez le nouveau nom");

        String newItemPath = defaultPath + pathLabel.getText() + "\\" + newName;
        File currentFile = new File(currentItemPath);
        File newFile = new File(newItemPath);
        boolean renamed = currentFile.renameTo(newFile);
        if (renamed) {
            System.out.println(currentItemPath + " renamed to " + newItemPath);
        } else {
            System.out.println("Failed to rename " + currentItemPath);
        }
        addToContentPane(defaultPath + pathLabel.getText());
    }//GEN-LAST:event_renameItemActionPerformed

    private void createFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFileActionPerformed
        String fileName = JOptionPane.showInputDialog(this, "Entrez le nom du fichier que vous souhaitez créer");
        String filePath = defaultPath + pathLabel.getText() + "\\" + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.close();
                System.out.println(filePath + " File created");
            } catch (IOException e) {
                e.printStackTrace();
            }
            addToContentPane(defaultPath + pathLabel.getText());
        }else{
            JOptionPane.showMessageDialog(this, "Fichier exists déja");
        }
    }//GEN-LAST:event_createFileActionPerformed

    private void quitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitActionPerformed
        this.dispose();
    }//GEN-LAST:event_quitActionPerformed

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
        String fileName = ((JLabel) selectedContentElementBoxes.getFirst().getComponent(1)).getText();
        String filePath = defaultPath + pathLabel.getText() + "\\" + fileName;
        File f = new File(filePath);
        if (f.isDirectory()) {
            addToContentPane(filePath);
        } else if (f.isFile()) {
            new fileEditor(filePath).setVisible(true);
        }
    }//GEN-LAST:event_openActionPerformed

    public static void main(String args[]) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controller;
    private javax.swing.JButton createFile;
    private javax.swing.JButton createFolder;
    private javax.swing.JButton deleteItem;
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
    // End of variables declaration//GEN-END:variables
}
