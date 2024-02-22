


import com.sun.jdi.connect.spi.Connection;
import interfaces.interfacefile;
import java.rmi.Naming;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Authentification extends javax.swing.JFrame {

    public Authentification() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        user = new javax.swing.JTextField();
        pass = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153)));

        user.setFont(new java.awt.Font("Arial Unicode MS", 0, 15)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Eras Medium ITC", 0, 16)); // NOI18N
        jLabel1.setText("Username");

        jButton1.setBackground(new java.awt.Color(0, 204, 204));
        jButton1.setFont(new java.awt.Font("Tempus Sans ITC", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Connexion");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Eras Demi ITC", 0, 16)); // NOI18N
        jLabel3.setText("X");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Eras Demi ITC", 0, 16)); // NOI18N
        jLabel4.setText("-");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(0, 204, 204));
        jSeparator1.setForeground(new java.awt.Color(0, 204, 204));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setFont(new java.awt.Font("Eras Medium ITC", 0, 16)); // NOI18N
        jLabel5.setText("Password");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(user)
                            .addComponent(pass)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        this.setState(1);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Récupérer le nom d'utilisateur et le mot de passe
        String username = user.getText();
        char[] password = pass.getPassword();
        int c = 0;
    //    String port = jTextField2.getText();

        // Vérifier si c'est un administrateur
    //    if ("Admin".equals(getRole(username, password))) {

           // System.out.println("Administrateur connecté!");
            // Ferme la fenêtre d'authentification
            
            try {
               interfacefile getfunctions = (interfacefile) Naming.lookup("rmi://localhost:1099/frameoperations");
               String pas = new String(password);
               c = getfunctions.loginuser(username, pas);
                if(c!=-1 && c!=0){
                    System.out.println("id: "+c);
                    Main log = new Main(c);
                    log.setVisible(true);
                    this.dispose();
                }else{
                    JOptionPane.showMessageDialog(this, "user doesn't exist");
                }
                // Ouvre la page principale en fonction du rôle
                //ouvrirMainPage(username, password);
            } catch (Exception ex) {
                Logger.getLogger(Authentification.class.getName()).log(Level.SEVERE, null, ex);
            }
        /* else if ("Utilisateur1".equals(getRole(username, password))) {
            // Utilisateur connecté
            System.out.println("Utilisateur connecté!");
            try {
                // Ouverture de la page principale pour l'utilisateur avec son nom spécifique
                ouvrirMainPage(username, password);
            } catch (Exception ex) {
                Logger.getLogger(Authentification.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if ("Utilisateur2".equals(getRole(username, password))) {
            // Utilisateur connecté
            System.out.println("Utilisateur connecté!");
            try {
                // Ouverture de la page principale pour l'utilisateur avec son nom spécifique
                ouvrirMainPage(username, password);
            } catch (Exception ex) {
                Logger.getLogger(Authentification.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if ("Utilisateur3".equals(getRole(username, password))) {
            // Utilisateur connecté
            System.out.println("Utilisateur connecté!");
            try {
                // Ouverture de la page principale pour l'utilisateur avec son nom spécifique
                ouvrirMainPage(username, password);
            } catch (Exception ex) {
                Logger.getLogger(Authentification.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if ("Utilisateur4".equals(getRole(username, password))) {
            // Utilisateur connecté
            System.out.println("Utilisateur connecté!");
            try {
                // Ouverture de la page principale pour l'utilisateur avec son nom spécifique
                ouvrirMainPage(username, password);
            } catch (Exception ex) {
                Logger.getLogger(Authentification.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Utilisateur mal connecté!");
        }*/
        // Dans Authentification.java, après une vérification réussie des identifiants

    }//GEN-LAST:event_jButton1ActionPerformed
   /* private String getRole(String username, char[] password) {
        // Vérifiez les informations d'authentification et renvoyez le rôle
        // Remplacez cela par une vérification appropriée en fonction de votre implémentation réelle.
        if (username.equals("admin") && Arrays.equals(password, "admin123".toCharArray())) {
            return "Admin";
        } else if (username.equals("fati") && Arrays.equals(password, "fati123".toCharArray())) {
            return "Utilisateur1";
        } else if (username.equals("abdeljalil") && Arrays.equals(password, "abdeljalil123".toCharArray())) {
            return "Utilisateur2";
        } else if (username.equals("yassir") && Arrays.equals(password, "yassir123".toCharArray())) {
            return "Utilisateur3";
        } else if (username.equals("pedro") && Arrays.equals(password, "pedro123".toCharArray())) {
            return "Utilisateur4";
        } else {
            return "Inconnu";
        }

    }

    private void ouvrirMainPage(String username, char[] password) throws Exception {
        String role = getRole(username, password);

        if (role.equals("Admin")) {
            // Ouvrez la page principale pour l'administrateur avec tous les dossiers
            Main mainPage = new Main(id);
            mainPage.setVisible(true);
        } else if (role.equals("Utilisateur1")) {
            // Ouvrez la page principale pour l'utilisateur 1 avec le dossier Fatima-ezzahra
        //  Main mainPage = new Main("Fatima-ezzahra");
            // mainPage.setVisible(true);
           // new Main().setVisible(true);
           String userFolder = getFolderNameByUsername(username);
        // Ouvrez la page principale pour l'utilisateur avec son dossier spécifique
          Main mainPage = new Main(id);
          mainPage.setVisible(true);
          
        } else if (role.equals("Utilisateur2")) {
            // Ouvrez la page principale pour l'utilisateur 2 avec le dossier Badr
            // Main mainPage = new Main("Badr");
            // mainPage.setVisible(true);
            new Main(id).setVisible(true);
        } else if (role.equals("Utilisateur3")) {
            // Ouvrez la page principale pour l'utilisateur 3 avec le dossier Yassir
            // Main mainPage = new Main("Yassir");
            // mainPage.setVisible(true);
            new Main(id).setVisible(true);
        } else if (role.equals("Utilisateur4")) {
            // Ouvrez la page principale pour l'utilisateur 4 avec le dossier Abdeljalil
            //Main mainPage = new Main("Abdeljalil");
            //mainPage.setVisible(true);
            new Main(id).setVisible(true);
        } else {
            // Affichez un message d'erreur pour un utilisateur inconnu
            System.out.println("Utilisateur inconnu!");
        }

        // Fermez la fenêtre d'authentification
        this.dispose();
    }
    private String getFolderNameByUsername(String username) {
    // Ajoutez la logique nécessaire pour mapper les utilisateurs à leurs dossiers
    if ("fati".equals(username)) {
         return System.getProperty("user.dir") + "\\ServerStorage"+"Fatima-ezzahra";
    } else if ("abdeljalil".equals(username)) {
        return "Abdeljalil";
    } else if ("yassir".equals(username)) {
        return "Yassir";
    } else if ("pedro".equals(username)) {
        return "Badr";
    } else {
        // Définissez un dossier par défaut pour les autres utilisateurs si nécessaire
        return "DossierParDefaut";
    }
}

    private boolean isAdmin(String username, char[] password) {
        // Remplacez ces informations par celles de votre administrateur
        String adminUsername = "admin";
        String adminPassword = "admin123";

        // Vérifier les informations de connexion
        return username.equals(adminUsername) && Arrays.equals(password, adminPassword.toCharArray());
    }*/

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.metal.windowsLookAndFell");
        } catch (Exception e) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Authentification().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPasswordField pass;
    private javax.swing.JTextField user;
    // End of variables declaration//GEN-END:variables
}
