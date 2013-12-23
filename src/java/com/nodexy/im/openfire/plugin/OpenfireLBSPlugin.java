package com.nodexy.im.openfire.plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;

/**
 * Openfire LBS plugin
 * 
 * @author Chris, node@github
 * 
 */
public class OpenfireLBSPlugin implements Plugin {

	private static final String MODULE_NAME_LOCATION = "im.location";

	private XMPPServer server;
	Connection openfireDBConn;
	private LocationHandler locationHandler;

	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		try {
			openfireDBConn = DbConnectionManager.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		locationHandler = new LocationHandler(MODULE_NAME_LOCATION,
				openfireDBConn);
		server = XMPPServer.getInstance();
		server.getIQRouter().addHandler(locationHandler);
	}

	@Override
	public void destroyPlugin() {
		server = XMPPServer.getInstance();

		if (locationHandler != null) {
			server.getIQRouter().removeHandler(locationHandler);
			locationHandler = null;
		}
		try {
			openfireDBConn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
