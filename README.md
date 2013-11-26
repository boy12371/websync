WebSync
=======

WebSync is an open HTTP interface for synchronization of web applicaiton with client's file system
or source code editor applications like Eclipse, NetBeans, SourceNavigator etc.

It is quite similar to the ["RSE DStore Developer Guide"](http://help.eclipse.org/indigo/index.jsp?nav=%2F52) except SSL support and provides the following functionality:
1. File tree naviagration (root, subfolders)
2. File hierarchy managment (add/remove/move file/folder)
3. Content managment save/open/update/remove
4. Source code index database access

The first integration of WebSync interface will be done for UmlSync project.

### View API:


For the Eclipse-like tools special view abstraction was created. The view is like an Eclipse view:  "Project explorer", "Package explorer" etc
And each view has own tree hierarchy.

#### Get the list of views

This method returns the list of available views:

    GET /vm/views
    
#### Parameters

Name | Type | Description 
-----|------|--------------
default| bool | __Optional__ get the default view only

#### Response

    {'id':'cp', 'title': 'C/C++ Explorer', 'description':'View for C/C++ source code navigation and diagrams managment.'},
    {'id':'java', 'title': 'Java Explorer', 'description':'View for JAVA source code navigation and diagrams managment.'},
    {'id':'un', 'title': 'Projects Explorer', 'description':'View for source code navigation and diagrams managment.', 'isdefault':true}



#### Get view capability

This method returns the list of view's capabilities:

    GET /vm/:id/capabilities

#### Response

    {'id':'cp', 'title': 'C/C++ Explorer', 'description':'View for C/C++ source code navigation and diagrams managment.'},
    {'id':'java', 'title': 'Java Explorer', 'description':'View for JAVA source code navigation and diagrams managment.'},
    {'id':'un', 'title': 'Projects Explorer', 'description':'View for source code navigation and diagrams managment.', 'isdefault':true}


The curent repository contains Eclipse plugins which implements websync API.
And prototype for CDT engine database access.




