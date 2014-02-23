### How to install WebSync Plugin

1. Launch Eclipse
2. Go to Help -> Install New Software…
3. click on "Add" button
3.2 Name: WebSync
3.3 Location: https://raw2.github.com/UmlSync/websync/master/websync-eclipse-feature-site/site.xml
4. Click "Ok" button
5. Select "Websync plug-in for Eclipse" - for tree naviagration and content managment (open/update/remove/add)
6. Select "Websync_eclipse_cdt_feature" for CDT C++ indexing support (applicable for CDT only)

### How to use WebSync Plugin:

1. click on ![icon](./websync-eclipse/icons/sample.gif "icon")
2. Select host and port (localhost by default)
3. secure key(Last name): - some unique magic sequence for source verification
4. Click "Start" to start service (or run with new parameters)
5. Click "Stop" to stop service
6. "Cancel" - to cancel user actions

### How to sync Eclipse and umlsync.org

1. Open the umlsync.org/editor
2. Click "Eclipse" on the left top corner (available for loged-in users only)
3. Copy port/host and secret key from steps 2. and 3. above
4. Click "Connect" to start session and "Cancel" to hide dialog without actions
