/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfa04;

import java.io.File;
import java.net.URL;
import javax.swing.text.DefaultCaret;

public class FrameMain extends javax.swing.JFrame {

    DFA dfa;
    public FrameMain() {
        initComponents();
        dfa = new DFA(outputArea);
        
        // set caret to end position to auto-scroll
        DefaultCaret caret = (DefaultCaret)outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); 
        
        fileComboBox.removeAllItems();
        File[] listOfFiles = (new File("data/")).listFiles();

        // scan dir for files, add them to combobox
        for (int i = 0; i < listOfFiles.length; i++)
          if (listOfFiles[i].getName().contains("dfa"))
              fileComboBox.addItem(listOfFiles[i].getName()); // ignore random files, only DFA
        
        fileComboBox.addItem("http://klayder.org/UMU/CSC450/dfa/dfa05.txt");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        singleStepButton = new javax.swing.JButton();
        runButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();
        speedSlider = new javax.swing.JSlider();
        speedLabel = new javax.swing.JLabel();
        rffButton = new javax.swing.JButton();
        fileComboBox = new javax.swing.JComboBox<>();
        fileTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        singleStepButton.setText("Single-Step");
        singleStepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleStepButtonActionPerformed(evt);
            }
        });

        runButton.setText("Run");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        outputArea.setEditable(false);
        outputArea.setColumns(20);
        outputArea.setRows(5);
        jScrollPane1.setViewportView(outputArea);

        speedSlider.setMaximum(999);
        speedSlider.setToolTipText("");
        speedSlider.setValue(500);
        speedSlider.setName(""); // NOI18N
        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                speedSliderStateChanged(evt);
            }
        });

        speedLabel.setText("Speed");

        rffButton.setText("Read From File");
        rffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rffButtonActionPerformed(evt);
            }
        });

        fileComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        fileComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fileComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(singleStepButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(runButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 18, Short.MAX_VALUE)
                        .addComponent(speedLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fileTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rffButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fileComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rffButton))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(singleStepButton)
                    .addComponent(resetButton)
                    .addComponent(runButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(speedLabel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        if (dfa.getPause()) {
            dfa.setPause(false);
            runButton.setText("Pause");
            outputArea.append("DFA started\n");
        }
        else {
            dfa.setPause(true);
            runButton.setText("Run");
            outputArea.append("DFA paused\n");
        }
    }//GEN-LAST:event_runButtonActionPerformed

    private void singleStepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleStepButtonActionPerformed
        if (dfa.getPause())
            dfa.singleStep();
    }//GEN-LAST:event_singleStepButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        outputArea.append("DFA reset\n");
        runButton.setText("Run");
        dfa.reset();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void speedSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_speedSliderStateChanged
        dfa.setSpeed(speedSlider.getValue());
    }//GEN-LAST:event_speedSliderStateChanged

    private void rffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rffButtonActionPerformed
        In in = null;
        if (!fileTextField.getText().contains("http")) { // check for web URL
            try {
                in = new In("data/" + fileTextField.getText());
            }
            catch (Exception e) {
                outputArea.append("Error in File System trying to open " + 
                fileTextField.getText() + "\n");
            }
            if (in != null && in.exists()) {
                outputArea.append("Reading data from file:\n");
                outputArea.append(in.readAll() + "\n");
            }
        } else {
            try {
                in = new In(new URL(fileTextField.getText()));
                outputArea.append(in.readAll());
            }
            catch (Exception e) {
                outputArea.append("Failed to read from " + fileTextField.getText() + "\n");
            }
        }
    }//GEN-LAST:event_rffButtonActionPerformed

    private void fileComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_fileComboBoxItemStateChanged
        fileTextField.setText((String)fileComboBox.getSelectedItem());
    }//GEN-LAST:event_fileComboBoxItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("a".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> fileComboBox;
    private javax.swing.JTextField fileTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea outputArea;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton rffButton;
    private javax.swing.JButton runButton;
    private javax.swing.JButton singleStepButton;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JSlider speedSlider;
    // End of variables declaration//GEN-END:variables
}
