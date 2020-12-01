package de.tgx03.mima.ui;

/**
 * What the parent of a panel needs to be able to do
 */
public interface PanelParent {

    /**
     * Notify the parent that the MiMa was changed through a panel
     */
    void update();

}
