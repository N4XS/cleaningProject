package userInterface;

import controller.ApplicationController;
import exceptions.connexion.EndConnectionException;
import exceptions.connexion.SingletonConnexionException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class MainJFrame extends JFrame {
    private JPanel panel;
    private ApplicationController controller;

    public MainJFrame() {
        try {
            controller = new ApplicationController();
            panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.decode("#ccd5ae"));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Gestion du Personnel");
            setSize(800, 600);
            setupMenuBar();
            add(panel, BorderLayout.CENTER);

            try {
                panel.add(new StaffMemberPanel(controller), BorderLayout.CENTER);
            } catch (Exception ex) {
                showError("Erreur lors du chargement de la liste du personnel : " + ex.getMessage());
            }

            setVisible(true);
            this.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    try {
                        controller.endConnection();
                    } catch (EndConnectionException ex) {
                        showError(ex.getMessage());
                    }
                    e.getWindow().dispose();
                }
            });
        } catch (SingletonConnexionException ex) {
            showError(ex.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        createMenu(menuBar, "Personnels", StaffMemberPanel.class);
        createMenu(menuBar, "Clients", ClientPanel.class);
        createMenu(menuBar, "Absences", AbsencePanel.class);
        createMenu(menuBar, "Villes", CityPanel.class);
        createMenu(menuBar, "Contrats", ContractPanel.class);
        createMenu(menuBar, "Sites", SitePanel.class);
        createMenu(menuBar, "Machines", MachineryPanel.class);
        createMenu(menuBar, "Services de Nettoyage", CleaningServicePanel.class);
        createMenu(menuBar, "Commandes", MaterialsOrderPanel.class);
        createMenu(menuBar, "Produits", ProductPanel.class);
        createMenu(menuBar, "Remplacements", ReplacementPanel.class);
        createMenu(menuBar, "Équipes", TeamPanel.class);
        createMenu(menuBar, "Avertissements", WarningPanel.class);

        setJMenuBar(menuBar);
    }

    private void createMenu(JMenuBar menuBar, String title, Class<?> panelClass) {
        JMenu menu = new JMenu(title);
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.removeAll();
                try {
                    JPanel newPanel = (JPanel) panelClass.getConstructor(ApplicationController.class).newInstance(controller);
                    panel.add(newPanel, BorderLayout.CENTER);
                } catch (InvocationTargetException | NoSuchMethodException ex) {
                    Throwable cause = ex.getCause();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ouverture de Commandes : " + cause, "Erreur", JOptionPane.ERROR_MESSAGE);
                    cause.printStackTrace();
                } catch (InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                panel.revalidate();
                panel.repaint();
            }
        });
        menuBar.add(menu);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
