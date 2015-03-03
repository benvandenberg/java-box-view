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

### Install with Maven

Coming soon

### Install without Maven

Coming soon

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

To run these examples, open up `examples/examples.php` and change this line to show your API key:

```java
public static String apiKey = "YOUR_API_KEY"
```

Save the file, make sure the `examples/files` directory is writeable, and then run `examples/Examples.java`:

You should see 17 examples run with output in your terminal.
You can inspect the `examples/Examples.java` code to see each API call being used.

### Your Code

To start using `java-box-view` in your code, set your API key:

```java
Client.setApiKey('YOUR_API_KEY');
```

And now you can start using the methods in `Document` and `Session`.

## Tests

Coming soon

## Support

Please use GitHub's issue tracker for API library support.

## Usage

### Errors

Errors are handled by throwing exceptions.
We throw instances of com.box.view.Exception.

Note that any Box View API call can throw an exception.
When making API calls, put them in a try/catch block.
You can see `examples/Examples.java` to see working code for each method using try/catch blocks.

### Document

#### Upload from File

https://developers.box.com/view/#post-documents  
To upload a document from a local file, use Document.upload().
Pass in a file resource, and also an optional key-value pair of other params.
This function returns a key-value pair representing the metadata of the file.

```java
// without options
File file = new File(filePath);
Map<String, Object> metadata = Document.upload(file);

// with options
File file = new File(filePath);

Map<String, Object> params = new HashMap<String, Object>();
params.put("name",  "Test File");
ArrayList<String> thumbnails = new ArrayList<String>();
thumbnails.add("100x100");
thumbnails.add("200x200");
params.put("thumbnails", thumbnails);
params.put("nonSvg", true);

Map<String, Object> metadata = Document.upload(file, params);
```

The response looks like this:

```java
{
  type=document,
  id=386bd56cd42a4256b9b25342d6ba986d,
  status=queued,
  name=Sample File,
  created_at=2015-03-03T03:04:46.143Z
}
```

#### Upload by URL

https://developers.box.com/view/#post-documents  
To upload a document by a URL, use `Document.upload()`.
Pass in the URL of the file you want to upload, and also an optional key-value pair of other params.
This function returns a key-value pair representing the metadata of the file.

```java
// without options
Map<String, Object> metadata = Document.upload(url);

// with options
Map<String, Object> params = new HashMap<String, Object>();
params.put("name",  "Test File");
ArrayList<String> thumbnails = new ArrayList<String>();
thumbnails.add("100x100");
thumbnails.add("200x200");
params.put("thumbnails", thumbnails);
params.put("nonSvg", true);

Map<String, Object> metadata = Document.upload(url, params);
```

The response looks like this:

```java
{
  type=document,
  id=386bd56cd42a4256b9b25342d6ba986d,
  status=queued,
  name=Sample File,
  created_at=2015-03-03T03:04:46.143Z
}
```

#### Metadata

https://developers.box.com/view/#get-documents-id  
To get a document's metadata, use `Document.metadata()`.
Pass in the ID of the file you want to check the metadata of.
This function returns a key-value pair representing the metadata of the file.

```java
Map<String, Object> metadata = Document.metadata(documentId);
```

The response looks like this:

```java
{
  type=document,
  id=386bd56cd42a4256b9b25342d6ba986d,
  status=queued,
  name=Sample File,
  created_at=2015-03-03T03:04:46.143Z
}
```

#### List

https://developers.box.com/view/#get-documents  
To get a list of documents you've uploaded, use `Document.list()`.
Pass an optional key-value pair of parameters you want to filter by.
This function returns an array of files matching the request.

```java
// without options
Map<String, Object> documents = Document.list();

// with options
Calendar start = Calendar.getInstance();
start.add(Calendar.WEEK_OF_YEAR, -2);

Calendar end = Calendar.getInstance();
end.add(Calendar.WEEK_OF_YEAR, -1);

Map<String, Object> options = new Map<String, Object>();
options.add("limit", 10);
options.add("createdAfter", start.getTime());
options.add("createdBefore", end.getTime());

Map<String, Object> documents = Document.list(options);
```

The response looks like this:

```java
{
  document_collection={
    total_count=2.0,
    entries=[
      {
        type=document,
        id=4f6cede66dc14bd080a405b1377ec66d,
        status=processing,
        name=Sample File #2,
        created_at=2015-03-03T03:19:55Z
      },
      {
        type=document,
        id=0d1e7cca94fa4db3b976f193f9f9b2d0,
        status=done,
        name=Sample File,
        created_at=2015-03-03T03:19:55Z
       }
     ]
   }
 }
```

#### Download

https://developers.box.com/view/#get-documents-id-content  
To download a document, use `Document.download()`.
Pass in the ID of the file you want to download.
This function returns the contents of the downloaded file.

```java
InputStream contents = Document.download(id);
String filename = "/files/new-file.doc";
File file = new File(filename);

try {
    FileUtils.copyInputStreamToFile(contents, file);
} catch (IOException e) {
    // do something
}
```

The response is an InputStream object representing the data of the file.

#### Thumbnail

https://developers.box.com/view/#get-documents-id-thumbnail  
To download a document, use `Document.thumbnail()`.
Pass in the ID of the file you want to download, and also the width and height in pixels of the thumbnail you want to download.
This function returns the contents of the downloaded thumbnail.

```java
InputStream thumbnailContents = Document.thumbnail(id, 100, 100);
String filename = "/files/new-thumbnail.png";
File file = new File(filename);

try {
    FileUtils.copyInputStreamToFile(thumbnailContents, file);
} catch (IOException e) {
    // do something
}
```

The response is an InputStream object representing the data of the file.

#### Update

https://developers.box.com/view/#put-documents-id  
To update the metadata of a document, use `Document.update()`.
Pass in the ID of the file you want to update, and also the fields you want to update.
Right now, only the name field is supported.
This function returns a key-value pair representing the metadata of the file.

```java
Map<String, Object> params = new Map<String, Object>();
params.add("name", "Updated Name");

Map<String, Object> metadata = Document.update(documentId, params);
```

The response looks like this:

```java
{
  type=document,
  id=386bd56cd42a4256b9b25342d6ba986d,
  status=queued,
  name=Sample File,
  created_at=2015-03-03T03:04:46.143Z
}
```

#### Delete

https://developers.box.com/view/#delete-documents-id  
To delete a document, use `Document.delete()`.
Pass in the ID of the file you want to delete.
This function returns a boolean of whether the file was deleted or not.

```java
Boolean deleted = Document.delete(documentId);

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

#### Create

https://developers.box.com/view/#post-sessions  
To create a session, use `Session.create()`.
Pass in the ID of the file you want to create a session for, and also an optional key-value pair of other params.
This function returns a key-value pair representing the metadata of the session.

```java
// without options
Map<String, Object> session = Session.create(documentId);

// with options
Calendar expiresAt = Calendar.getInstance();
expiresAt.add(Calendar.MINUTE, 10);

Map<String, Object> params = new Map<String, Object>();
params.put("expiresAt", expiresAt.getTime());
params.put("isDownloadable", true);
params.put("isTextSelectable", false);

Map<String, Object> session = Session.create(documentId, params);
```

The response looks like this:

```java
{
  type=session,
  id=31d04397460c48f2881e84a2928cf869,
  document={
    type=document,
    id=6649388e766b439c9ddbd78d0e4f924c,
    status=done,
    name=Updated Name,
    created_at=2015-03-03T03:30:33Z
  },
  expires_at=2015-03-03T04:30:50.434Z,
  urls={
    view=https://view-api.box.com/1/sessions/31d04397460c48f2881e84a2928cf869/view,
    assets=https://view-api.box.com/1/sessions/31d04397460c48f2881e84a2928cf869/assets/,
    realtime=https://view-api.box.com/sse/31d04397460c48f2881e84a2928cf869
  }
}
```

#### Delete

https://developers.box.com/view/#delete-sessions-id  
To delete a session, use `Session.delete()`.
Pass in the ID of the session you want to delete.
This function returns a boolean of whether the session was deleted or not.

```java
Boolean deleted = Session.delete(sessionId);

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
