WebSync interface description
===

* List views
* Get tree
* Create content
* Get content
* Edit content

-

### <> List views ###

View is an abstraction over the file system and file data.<br>
In case of Eclipse the are a lot of view types: Project Explorer, Package explorer, C/C++ view etc

```
GET /vm/views
```

<h3>Parameters</h3>
type - `all`, `default`, `%language%`. Default: `all`


### <> Get tree ###

Get data tree for view. For example: for package tree it is the list of packages and files in that packages.

```
GET /vm/view_id/tree
```

<h3>Parameters</h3>
path - optional. Return root list if empty


### <> Create content ###

Create new file.

```
GET /vm/view_id/create
```

<h3>Parameters</h3>
path - Required. Full path to the new file.
content - Optional. Create an empty file if content not provided.


### <> Get content ###

Get file content.

```
GET /vm/view_id/get
```

<h3>Parameters</h3>
path - Required. Full path to the content file.


### <> Edit content ###

Update file content.

```
GET /vm/view_id/update
```

<h3>Parameters</h3>
path - Required. Full path to the content file.
content - Required. Updated content of file.
