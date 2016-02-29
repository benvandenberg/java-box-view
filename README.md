# java-box-view

## Introduction

java-box-view is a Java wrapper for the Box View API.
The Box View API lets you upload documents and then generate secure and customized viewing sessions for them.
Our API is based on REST principles and generally returns JSON encoded responses,
and in Java are converted to key-value pairs unless otherwise noted.

For more information about the Box View API, see the [API docs at developers.box.com](https://developers.box.com/view/).

## Installation

### Requirements

* Java 1.6 or newer

### Install

Add box-view.jar to your classpath after downloading or building the JAR.

#### Download a fat JAR file

As one option, the library can be downloaded. You can download the JAR here: http://crocodoc.github.io/java-box-view/box-view.jar
This is a fat JAR file, which contains all of the classes for the dependencies.

#### Build a non-fat JAR file

As another option, the library can be built locally. Clone the repository, and run with Maven 3.x

```
mvn install
```

This installs the JAR to your local Maven repository at ``/path/to/.m2/repository/com/box/view/box-view/1.0/box-view-1.0.jar``.

When uploading to a Maven repository, the URL can be specified from the command line with the ``-Drepo.releases.url`` or ``-Drepo.snapshots.url`` options. Refer to the pom.xml file.

## Getting Started

### Get an API Key

[Create a Box Application](https://app.box.com/developers/services/edit/) to get an API key.
Enter your application name, click the option for `Box View`, and click `Create Application`.
Then click `Configure your application`.

You can find your API key where it says `View API Key`.

In the future, if you need to return to this page, go to [Box Developers > My Applications](https://app.box.com/developers/services) and click on any of your Box View apps.

### Examples

You can see a number of examples on how to use this library in `examples/Examples.java`.
These examples are interactive and you can run this file to see `java-box-view` in action.

To run these examples, open up `examples/Examples.java` and change this line to show your API key:

```java
public static String apiKey = "YOUR_API_KEY";
```

Save the file, make sure the `examples/files` directory is writeable, and then run `examples/Examples.java`:

You should see 17 examples run with output in your terminal.
You can inspect the `examples/Examples.java` code to see each API call being used.

### Your Code

To start using `java-box-view` in your code, set your API key:

```java
BoxViewClient boxView = new BoxViewClient("YOUR_API_KEY");
```

And now you can start using the methods in `Document` and `Session`.

## Support

Please use GitHub's issue tracker for API library support.

## Usage

### Fields

All fields are accessed using getters.
You can find a list of these fields below in their respective sections.

### Errors

Errors are handled by throwing exceptions.
We throw instances of `BoxViewException`.

Note that any Box View API call can throw an exception.
When making API calls, put them in a try/catch block.
You can see `examples/Examples.java` to see working code for each method using try/catch blocks.

### Document

#### Fields

Field     | Getter
--------- | ------
id        | document.getId()
createdAt | document.getCreatedAt()
name      | document.getName()
status    | document.getStatus()

#### Upload from File

https://developers.box.com/view/#post-documents
To upload a document from a local file, use `boxView.upload()`.
Pass in a file resource, and also an optional key-value pair of other params.
This function returns a `Document` object.

```java
// without options
File file = new File(filePath);
Document document = boxView.upload(file);

// with options
File file = new File(filePath);

Map<String, Object> params = new HashMap<String, Object>();
params.put("name", "Test File");
ArrayList<String> thumbnails = new ArrayList<String>();
thumbnails.add("100x100");
thumbnails.add("200x200");
params.put("thumbnails", thumbnails);
params.put("nonSvg", true);

Document document = boxView.upload(file, params);
```

The response looks like this:

```java
com.box.view.Document@a21d23b[
  id=386bd56cd42a4256b9b25342d6ba986d,
  createdAt=Fri Apr 17 22:21:17 PDT 2015,
  name=Sample File,
  status=queued
]
```

#### Upload by URL

https://developers.box.com/view/#post-documents
To upload a document by a URL, use `boxView.upload()`.
Pass in the URL of the file you want to upload, and also an optional key-value pair of other params.
This function returns a `Document` object.

```java
// without options
Document document = boxView.upload(url);

// with options
Map<String, Object> params = new HashMap<String, Object>();
params.put("name", "Test File");
ArrayList<String> thumbnails = new ArrayList<String>();
thumbnails.add("100x100");
thumbnails.add("200x200");
params.put("thumbnails", thumbnails);
params.put("nonSvg", true);

Document document = boxView.upload(url, params);
```

The response looks like this:

```java
com.box.view.Document@a21d23b[
  id=386bd56cd42a4256b9b25342d6ba986d,
  createdAt=Fri Apr 17 22:21:17 PDT 2015,
  name=Sample File,
  status=queued
]
```

#### Get Document

https://developers.box.com/view/#get-documents-id
To get a document, use `boxView.getDocument()`.
Pass in the ID of the document you want to get.
This function returns a `Document` object.

```java
Document document = boxView.getDocument(documentId)`
```

The response looks like this:

```java
com.box.view.Document@a21d23b[
  id=386bd56cd42a4256b9b25342d6ba986d,
  createdAt=Fri Apr 17 22:21:17 PDT 2015,
  name=Sample File,
  status=queued
]
```

#### Find

https://developers.box.com/view/#get-documents
To get a list of documents you've uploaded, use `boxView.findDocuments()`.
Pass an optional key-value pair of parameters you want to filter by.
This function returns an array of `Document` objects matching the request.

```java
// without options
List<Document> documents = boxView.findDocuments();

// with options
Calendar start = Calendar.getInstance();
start.add(Calendar.WEEK_OF_YEAR, -2);

Calendar end = Calendar.getInstance();
end.add(Calendar.WEEK_OF_YEAR, -1);

Map<String, Object> options = new Map<String, Object>();
options.add("limit", 10);
options.add("createdAfter", start.getTime());
options.add("createdBefore", end.getTime());

List<Document> documents = boxView.findDocuments(options);
```

The response looks like this:

```java
[
  com.box.view.Document@a21d23b[
    id=386bd56cd42a4256b9b25342d6ba986d,
    createdAt=Fri Apr 17 22:21:17 PDT 2015,
    name=Sample File,
    status=queued
  ],
  com.box.view.Document@5d17c0eb[
    id=0971e7674469406dba53254fcbb11d05,
    createdAt=Fri Apr 17 22:21:17 PDT 2015,
    name=Sample File #2,
    status=queued
  ]
]
```

#### Download

https://developers.box.com/view/#get-documents-id-content
To download a document, use `document.download()`.
This function returns the contents of the downloaded file.

```java
InputStream contents = document.download();
String filename = "/files/new-file.doc";
File file = new File(filename);

try {
    FileUtils.copyInputStreamToFile(contents, file);
} catch (IOException e) {
    // do something
}
```

The response is an `InputStream` object representing the data of the file.

#### Thumbnail

https://developers.box.com/view/#get-documents-id-thumbnail
To download a document, use `document.thumbnail()`.
Pass in the width and height in pixels of the thumbnail you want to download.
This function returns the contents of the downloaded thumbnail.

```java
InputStream thumbnailContents = document.thumbnail(100, 100);
String filename = "/files/new-thumbnail.png";
File file = new File(filename);

try {
    FileUtils.copyInputStreamToFile(thumbnailContents, file);
} catch (IOException e) {
    // do something
}
```

The response is an `InputStream` object representing the data of the file.

#### Update

https://developers.box.com/view/#put-documents-id
To update the metadata of a document, use `document.update()`.
Pass in the fields you want to update.
Right now, only the `name` field is supported.
This function returns a boolean of whether the file was updated or not.

```java
Map<String, Object> params = new Map<String, Object>();
params.add("name", "Updated Name");

Boolean updated = document.update(params);

if (updated) {
    // do something
} else {
    // do something else
}
```

The response looks like this:

```java
true
```

#### Delete

https://developers.box.com/view/#delete-documents-id
To delete a document, use `document.delete()`.
This function returns a boolean of whether the file was deleted or not.

```java
Boolean deleted = document.delete();

if (deleted) {
    // do something
} else {
    // do something else
}
```

The response looks like this:

```java
true
```

### Session

#### Fields

Field       | Getter
----------- | ------
id          | session.getId()
document    | session.getDocument()
expiresAt   | session.getExpiresAt()
assetsUrl   | session.getAssetsUrl()
realtimeUrl | session.getRealtimeUrl()
viewUrl     | session.getViewUrl()

#### Create

https://developers.box.com/view/#post-sessions
To create a session, use `document.createSession()`.
Pass in an optional key-value pair of other params.
This function returns a `Session` object.

```java
// without options
Session session = document.createSession();

// with options
Calendar expiresAt = Calendar.getInstance();
expiresAt.add(Calendar.MINUTE, 10);

Map<String, Object> params = new Map<String, Object>();
params.put("expiresAt", expiresAt.getTime());
params.put("isDownloadable", true);
params.put("isTextSelectable", false);

Session session = document.createSession(params);
```

The response looks like this:

```java
com.box.view.Session@3f406eb6[
  id=d1b8c35a69da43fbb2e978e99589114a,
  document=com.box.view.Document@5d17c0eb,
  expiresAt=Fri Apr 17 22:21:17 PDT 2015,
  urls=[
    view=https://view-api.box.com/1/sessions/31d04397460c48f2881e84a2928cf869/view,
    assets=https://view-api.box.com/1/sessions/31d04397460c48f2881e84a2928cf869/assets/,
    realtime=https://view-api.box.com/sse/31d04397460c48f2881e84a2928cf869
  ]
]
```

#### Delete

https://developers.box.com/view/#delete-sessions-id
To delete a session, use `session.delete()`.
This function returns a boolean of whether the session was deleted or not.

```java
Boolean deleted = session.delete();

if (deleted) {
    // do something
} else {
    // do something else
}
```

The response looks like this:

```java
true
```
