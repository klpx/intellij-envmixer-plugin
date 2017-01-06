package com.github.klpx.envmixer;

import com.github.klpx.envmixer.ui.EnvConfigTree;
import com.github.klpx.envmixer.ui.EnvVarsEditorComponent;

import javax.swing.*;

/**
 * Created by hsslbch on 1/5/17.
 */
public class ConfigurationForm {
    private JPanel panel1;
    private EnvConfigTree tree1;
    private JButton newMixinGroupButton;
    private EnvVarsEditorComponent envEditor;
    private JTextPane groupHelpPane;
    private JTextPane globalTextPane;


    public JPanel getPanel1() {
        return panel1;
    }

    public EnvConfigTree getTree1() {
        return tree1;
    }

    public EnvVarsEditorComponent getEnvEditor() {
        return envEditor;
    }

    public JTextPane getGroupHelpPane() {
        return groupHelpPane;
    }

    public JTextPane getGlobalTextPane() {
        return globalTextPane;
    }
}
