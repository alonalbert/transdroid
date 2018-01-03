/* 
 * Copyright 2010-2013 Eric Kok et al.
 * 
 * Transdroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Transdroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Transdroid.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.transdroid.core.gui.remoterss;


import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ActionMenuView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import java.util.ArrayList;
import java.util.List;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.transdroid.R;
import org.transdroid.core.gui.lists.LocalTorrent;
import org.transdroid.core.gui.log.Log;
import org.transdroid.core.gui.remoterss.data.RemoteRssItem;
import org.transdroid.core.gui.remoterss.data.RemoteRssSupplier;
import org.transdroid.daemon.DaemonException;
import org.transdroid.daemon.task.DaemonTaskSuccessResult;

/**
 * Fragment that shows a list of RSS items from the server and allows the user
 * to download remotely, without having to set up RSS feeds on the Android device.
 * @author Twig
 */
@EFragment(R.layout.fragment_remoterss)
public class RemoteRssFragment extends Fragment {
	@Bean
	protected Log log;

	// Local data
	@InstanceState
	protected ArrayList<RemoteRssItem> remoteRssItems;

	// Views
	@ViewById
	protected View detailsContainer;
	@ViewById(R.id.contextual_menu)
	protected ActionMenuView contextualMenu;
	@ViewById
	protected SwipeRefreshLayout swipeRefreshLayout;
	@ViewById
	protected ListView torrentsList;
	@ViewById
	protected TextView remoterssNoFilesMessage;

	protected RemoteRssItemsAdapter adapter;

	@AfterViews
	protected void init() {

		// Inject menu options in the actions toolbar
		setHasOptionsMenu(true);

//		// On large screens where this fragment is shown next to the torrents list, we show a continues grey vertical
//		// line to separate the lists visually
//		if (!NavigationHelper_.getInstance_(getActivity()).isSmallScreen()) {
//			if (SystemSettings_.getInstance_(getActivity()).useDarkTheme()) {
//				detailsContainer.setBackgroundResource(R.drawable.details_list_background_dark);
//			} else {
//				detailsContainer.setBackgroundResource(R.drawable.details_list_background_light);
//			}
//		}

		// Set up details adapter
		adapter = new RemoteRssItemsAdapter(getActivity());
		torrentsList.setAdapter(adapter);
		torrentsList.setFastScrollEnabled(true);

		// Restore the fragment state (on orientation changes et al.)
		if (remoteRssItems != null) {
			updateRemoteItems(remoteRssItems);
		}
	}

	/**
	 * Updates the UI with a new list of RSS items.
	 */
	public void updateRemoteItems(List<RemoteRssItem> remoteItems) {
		remoteRssItems = new ArrayList<>(remoteItems);
		adapter.updateItems(remoteRssItems);
		torrentsList.smoothScrollToPosition(0);

		// Show/hide a nice message if there are no items to show
		remoterssNoFilesMessage.setVisibility(remoteRssItems.size() > 0 ? View.GONE : View.VISIBLE);
	}

	/**
	 * When the user clicks on an item, prepare to download it.
     */
	@ItemClick(resName = "torrents_list")
	protected void detailsListClicked(int position) {
		RemoteRssItem item = (RemoteRssItem) adapter.getItem(position);
		downloadRemoteRssItem(item);
	}

	/**
	 * Download the item in a background thread and display success/fail accordingly.
     */
	@Background
	protected void downloadRemoteRssItem(RemoteRssItem item) {
		final RemoteRssActivity activity = (RemoteRssActivity) getActivity();
		final RemoteRssSupplier supplier = (RemoteRssSupplier) activity.getCurrentConnection();

		try {
			supplier.downloadItem(log, item, activity.getChannel(item.getSourceName()));
			onTaskSucceeded(null, getString(R.string.result_added, item.getTitle()));
		} catch (DaemonException e) {
			onTaskFailed(getString(LocalTorrent.getResourceForDaemonException(e)));
		}
	}

	@UiThread
	protected void onTaskSucceeded(DaemonTaskSuccessResult result, String successMessage) {
		SnackbarManager.show(Snackbar.with(getActivity()).text(successMessage));
	}

	@UiThread
	protected void onTaskFailed(String message) {
		SnackbarManager.show(Snackbar.with(getActivity())
			 .text(message)
			 .colorResource(R.color.red)
			 .type(SnackbarType.MULTI_LINE)
		);
	}
}
