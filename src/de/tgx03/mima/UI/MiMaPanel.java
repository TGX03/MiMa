package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;

public interface MiMaPanel {

    /**
     * Changes the current MiMa instance of this panel to the one provided
     * @param newInstance The new MiMa
     */
    void updateMiMa(MiMa newInstance);

    /**
     * Notifies a panel that the MiMa has changed
     */
    void updateMiMa();
}
