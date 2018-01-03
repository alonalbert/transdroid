package org.transdroid.core.gui.remoterss.data;

import java.util.ArrayList;
import org.transdroid.core.gui.log.Log;
import org.transdroid.daemon.DaemonException;

/**
 * Interface for daemon adapters if they support remote RSS management.
 *
 * @author Twig
 */
public interface RemoteRssSupplier {
    ArrayList<RemoteRssChannel> getRemoteRssChannels(Log log) throws DaemonException;

    void downloadItem(Log log, RemoteRssItem rssItem, RemoteRssChannel rssChannel) throws DaemonException;
}
