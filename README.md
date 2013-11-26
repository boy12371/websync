WebSync
=======

WebSync is an open HTTP interface for synchronization of web applicaiton with client's file system
or source code editor applications like Eclipse, NetBeans, SourceNavigator etc.

It is quite similar to the Eclipse DStore (see ["RSE DStore Developer Guide"](http://help.eclipse.org/indigo/index.jsp?nav=%2F52) ) except SSL support and provides the following functionality:

1. File tree naviagration (root, subfolders)
2. File hierarchy managment (add/remove/move file/folder)
3. Content managment save/open/update/remove
4. Source code index database access

The first integration of WebSync interface will be done for UmlSync project.

===
###View API:

For the Eclipse-like tools special view abstraction was created. The view is like an Eclipse view:  "Project explorer", "Package explorer" etc

And of course each view has its own tree hierarchy.


=====
####Get the list of views


This method returns the list of available views:

    GET /vm/views
    
__Parameters__

Name | Type | Description 
-----|------|--------------
default| bool | __Optional__ get the default view only

__Response__

    {'id':'cp', 'title': 'C/C++ Explorer', 'description':'View for C/C++ source code navigation and diagrams managment.'},
    {'id':'java', 'title': 'Java Explorer', 'description':'View for JAVA source code navigation and diagrams managment.'},
    {'id':'un', 'title': 'Projects Explorer', 'description':'View for source code navigation and diagrams managment.', 'isdefault':true}



=====
####Get view capabilities

This method returns the list of view's capabilities:

    GET /vm/:id/capabilities


The major idea is to create an extension point for the element context menu based on these capabilities.

__Response__

    [TBD]
    


=====
####Get view's tree hierarchy


This method returns the list of tree-hirarchy in the dynatree compatible :

    GET /vm/:id/list

__Parameters__

Name | Type | Description 
-----|------|--------------
path| String | __Required__  path to the resource ("/" - for the root)

__Response__

        [{'isLazy': true, 'isFolder': true, 'title': 'BBBB','addClass':'jproject'},
        {'isLazy': true, 'isFolder': true, 'title': 'Diagrams','addClass':'project'}]

Name | Type | Description 
-----|------|--------------
isLazy| Boolean | __Required__  indicates if it is possible to open sub-tree. For the file it could be possible to show internal classes.
isFolder| Boolean | __Required__ dynatree specific. Indicates that resource is folder
title| String | __Required__ the title of resource
addClass| String | __Optional__ CSS class for the resource which indicates it's specific (class, namespace, interface, package)



The curent repository contains Eclipse plugins which implements websync API.
And prototype for CDT engine database access.




=====
####Get content

This method returns the content of file:

    GET /vm/:id/open

__Parameters__

Name | Type | Description 
-----|------|--------------
path| String | __Required__  path to the file resource

__Response__

        {'encoding':'base64', 'data': '%data%'}

Name | Type | Description 
-----|------|--------------
encoding| String | __Required__  base64 decoding by default.
data| String | __Required__ base64 encoded content


=====
####Save content

This method update an existing content or create a new one:

    GET /vm/:id/save

Note: file should not be created if path-dirs does not exist

__Parameters__

Name | Type | Description 
-----|------|--------------
path| String | __Required__  path to the file resource
data| String | __Required__  content 
description| String | __Optional__  update|new uses to prevent overwriting an existing content

__Response__

        [None]


=====
####Remove content

This method remove an existing content:

    GET /vm/:id/remove

__Parameters__

Name | Type | Description 
-----|------|--------------
path| String | __Required__  path to the file resource

__Response__

        [None]

        
