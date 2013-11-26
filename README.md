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
###Views API:

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

Name | Type | Description 
-----|------|--------------
id| String | __Required__ unique id of the view
title| String | __Required__ the title of view
default| bool | __Optional__ indicates that view should be opened by default
description| String | __Optional__ the description of view

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

=====
####New folder

This method create a new folder for existing path:

    GET /vm/:id/newfolder

__Parameters__

Name | Type | Description 
-----|------|--------------
path| String | __Required__  path to the new folder resource (including a new folder name)

__Response__

        [None]

        


===
###Indexer database API:

The source code index database functionality is view specific. Therefore user have to avail to ask C++ indexer about Java classes or packages.

In an ideal case the "C/C++ explorer" should not provide an access to Java projects etc. 

=====
####Get calss information


This method returns an information about class (or it's methods or it's fields):

    GET /vm/:id/db/class/info|methods|fields
    
__Parameters__

Name | Type | Description 
-----|------|--------------
key| String | __Required__ the name of class
path| String | __Optional__ the path to the file which class belong to
package| String | __Optional__ the package which class belong to (for Java classes)

If path or package not defined then return all possible results


__Response__

    [{  name: "IContext",
        attr: 0x001,
        filepath: "/Project/folder/IContext.h", 
        methods:[{attr: 0x001, name: "method1", arguments:"int x1, bool x2", return: "void"},
        {attr: 0x001, name: "method2", arguments:"int x1, bool x2", return: "void"}],
        fields:[{attr: 0x001, name:"field1", type:"void*"}]
    },
    {  name: "IContext",
       filepath: "/Project/interfaces/IContext.h", 
       ...
    }]
     
Name | Type | Description 
-----|------|--------------
attr| int | __Required__ the visibility attributes according to [SourceNavigator's classification ](http://sourceforge.net/p/sourcenav/code/HEAD/tree/trunk/snavigator/hyper/sn.h)
name| String | __Required__ the name of class
filepath or package| String | __Required__ the package name or filepath
methods| Method | __Required__ the list of methods
fields| Field | __Required__ the list of fields




     
