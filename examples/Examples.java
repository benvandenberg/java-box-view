import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.box.view.Client;
import com.box.view.Document;
import com.box.view.Session;

public class Examples {
//    public static String apiKey = "YOUR_API_KEY";
    public static String apiKey = "01iet7esk486i0ujkopk0vowbcp99rgo";

    public static Map<String, Object> document;

    public static Map<String, Object> document2;

    public static Map<String, Object> session;

    public static Map<String, Object> session2;

    public static Date start = new Date();

    public static void main(String[] args) throws ParseException {
        Client.setApiKey(apiKey);
        example1();
        example2();
        example3();
        example4();
        example5();
        example6();
        example7();
        example8();
        example9();
        example10();
        example11();
        example12();
        example13();
        example14();
        example15();
        example16();
        example17();
    }

    /*
     * Example #1
     *
     * Upload a file. We're uploading a sample file by URL.
     */
    public static void example1() {
        System.out.println("Example #1 - Upload sample file by URL.");
        String sampleUrl = "http://crocodoc.github.io/php-box-view/examples/"
                            + "files/sample.doc";
        System.out.println("  Uploading... ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Sample File");

        try {
            document = Document.upload(sampleUrl, params);

            System.out.println("success :)");
            System.out.println("  ID is " + document.get("id") + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #2
     *
     * Check the metadata of the file from Example #1.
     */
    public static void example2() {
        System.out.println("Example #2 - Check the metadata of the file we just"
                           + " uploaded.");
        System.out.println("  Checking metadata... ");

        ArrayList<String> fields = new ArrayList<String>();
        fields.add("id");
        fields.add("type");
        fields.add("status");
        fields.add("name");
        fields.add("created_at");

        String id = document.get("id").toString();

        try {
            Map<String, Object> metadata = Document.metadata(id, fields);

            System.out.println("success :)");
            System.out.println("  File ID is " + metadata.get("id") + ".");
            System.out.println("  File type is " + metadata.get("type") + ".");
            System.out.println("  File status is " + metadata.get("status")
                               + ".");
            System.out.println("  File name is " + metadata.get("name") + ".");
            System.out.println("  File was created on "
                               + metadata.get("created_at") + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #3
     *
     * Upload another file. We're uploading a sample .doc file from the local
     * filesystem using all options.
     */
    public static void example3() {
        System.out.println("Example #3 - Upload a sample .doc as a file using"
                           + " all options.");
        File file = new File("./examples/files/sample.doc");
        System.out.println("  Uploading... ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name",  "Sample File #2");
        params.put("thumbnails", "100x100,200x200");
        params.put("nonSvg", true);

        try {
            document2 = Document.upload(file, params);
            System.out.println(document2);

            System.out.println("success :)");
            System.out.println("  ID is " + document2.get("id") + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #4
     *
     * Check the metadata of the file from Example #3.
     */
    public static void example4() {
        System.out.println("Example #4 - Check the metadata of the file we just"
                           + " uploaded.");
        System.out.println("  Checking metadata... ");

        ArrayList<String> fields = new ArrayList<String>();
        fields.add("id");
        fields.add("type");
        fields.add("status");
        fields.add("name");
        fields.add("created_at");

        String id = document2.get("id").toString();

        try {
            Map<String, Object> metadata = Document.metadata(id, fields);

            System.out.println("success :)");
            System.out.println("  File ID is " + metadata.get("id") + ".");
            System.out.println("  File type is " + metadata.get("type") + ".");
            System.out.println("  File status is " + metadata.get("status")
                               + ".");
            System.out.println("  File name is " + metadata.get("name") + ".");
            System.out.println("  File was created on "
                               + metadata.get("created_at") + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #5
     *
     * List the documents we've uploaded since starting these examples.
     */
    public static void example5() throws ParseException {
        System.out.println("Example #5 - List the documents we uploaded so"
                           + " far.");
        System.out.println("  Listing documents... ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("createdAfter", start);

        try {
            Map<String, Object> documents = Document.list(params);

            @SuppressWarnings("unchecked")
            Map<String, Object> documentCollection =
                    (Map<String, Object>) documents.get("document_collection");
            @SuppressWarnings("unchecked")
            ArrayList<Map<String, Object>> entries =
                            (ArrayList<Map<String, Object>>) documentCollection
                                                             .get("entries");

            Map<String, Object> doc1 = entries.get(0);
            Map<String, Object> doc2 = entries.get(1);

            System.out.println("success :)");
            System.out.println("  File #1 ID is " + doc1.get("id") + ".");
            System.out.println("  File #1 type is " + doc1.get("type") + ".");
            System.out.println("  File #1 status is " + doc1.get("status")
                               + ".");
            System.out.println("  File #1 name is " + doc1.get("name") + ".");
            System.out.println("  File #1 was created on "
                               + doc1.get("created_at") + ".");
            System.out.println("  File #2 ID is " + doc2.get("id") + ".");
            System.out.println("  File #2 type is " + doc2.get("type") + ".");
            System.out.println("  File #2 status is " + doc2.get("status")
                               + ".");
            System.out.println("  File #2 name is " + doc2.get("name") + ".");
            System.out.println("  File #2 was created on "
                               + doc2.get("created_at") + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #6
     *
     * Wait ten seconds and check the status of both files.
     */
    public static void example6() throws ParseException {
        System.out.println("Example #6 - Wait ten seconds and check the status"
                           + " of both files.");
        System.out.println("  Waiting... ");

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
        }

        System.out.println("done.");
        System.out.println("  Checking statuses... ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("createdAfter", start);

        try {
            Map<String, Object> documents = Document.list(params);

            @SuppressWarnings("unchecked")
            Map<String, Object> documentCollection =
                    (Map<String, Object>) documents.get("document_collection");
            @SuppressWarnings("unchecked")
            ArrayList<Map<String, Object>> entries =
                            (ArrayList<Map<String, Object>>) documentCollection
                                                             .get("entries");

            Map<String, Object> doc1 = entries.get(0);
            Map<String, Object> doc2 = entries.get(1);

            System.out.println("success :)");
            System.out.println("  Status for file #1 (id=" + doc1.get("id")
                               + ") is " + doc1.get("status") + ".");
            System.out.println("  Status for file #2 (id=" + doc2.get("id")
                               + ") is " + doc2.get("status") + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #7
     *
     * Delete the file we uploaded from Example #1.
     */
    public static void example7() {
        System.out.println("Example #7 - Delete the second file we uploaded.");
        System.out.println("  Deleting... ");

        try {
            Boolean deleted = Document.delete(document2.get("id").toString());

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  File was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #8
     *
     * Update the name of the file from Example #1.
     */
    public static void example8() {
        System.out.println("Example #8 - Update the name of a file.");
        System.out.println("  Updating... ");

        String id = document.get("id").toString();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Updated Name");

        try {

            Map<String, Object> metadata = Document.update(id, params);

            System.out.println("success :)");
            System.out.println("  File ID is " + metadata.get("id") + ".");
            System.out.println("  File type is " + metadata.get("type") + ".");
            System.out.println("  File status is " + metadata.get("status")
                               + ".");
            System.out.println("  File name is " + metadata.get("name") + ".");
            System.out.println("  File was created on "
                               + metadata.get("created_at") + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #9
     *
     * Download the file we uploaded from Example #1 in its original file format.
     */
    public static void example9() {
        System.out.println("Example #9 - Download a file in its original file"
                           + " format.");
        System.out.println("  Downloading... ");

        String id = document.get("id").toString();

        try {
            InputStream contents = Document.download(id);
            String filename = "./examples/files/test-original.doc";
            File file = new File(filename);

            try {
                FileUtils.copyInputStreamToFile(contents, file);
                System.out.println("success :)");
                System.out.println("  File was downloaded to " + filename
                                   + ".");
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #10
     *
     * Download the file we uploaded from Example #1 as a PDF.
     */
    public static void example10() {
        System.out.println("Example #10 - Download a file as a PDF.");
        System.out.println("  Downloading... ");

        String id = document.get("id").toString();

        try {
            InputStream contents = Document.download(id, "pdf");
            String filename = "./examples/files/test.pdf";
            File file = new File(filename);

            try {
                FileUtils.copyInputStreamToFile(contents, file);
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            System.out.println("success :)");
            System.out.println("  File was downloaded to " + filename + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #11
     *
     * Download the file we uploaded from Example #1 as a zip file.
     */
    public static void example11() {
        System.out.println("Example #11 - Download a file as a zip.");
        System.out.println("  Downloading... ");

        String id = document.get("id").toString();

        try {
            InputStream contents = Document.download(id, "zip");
            String filename = "./examples/files/test.zip";
            File file = new File(filename);

            try {
                FileUtils.copyInputStreamToFile(contents, file);
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            System.out.println("success :)");
            System.out.println("  File was downloaded to " + filename + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #12
     *
     * Download the file we uploaded from Example #1 as a small thumbnail.
     */
    public static void example12() {
        System.out.println("Example #12 - Download a small thumbnail from a"
                           + " file.");
        System.out.println("  Downloading... ");

        String id = document.get("id").toString();

        try {
            InputStream contents = Document.thumbnail(id, 16, 16);
            String filename = "./examples/files/test-thumbnail.png";
            File file = new File(filename);

            try {
                FileUtils.copyInputStreamToFile(contents, file);
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            System.out.println("success :)");
            System.out.println("  File was downloaded to " + filename + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #13
     *
     * Download the file we uploaded from Example #1 as a large thumbnail.
     */
    public static void example13() {
        System.out.println("Example #13 - Download a large thumbnail from a"
                           + " file.");
        System.out.println("  Downloading... ");

        String id = document.get("id").toString();

        try {
            InputStream contents = Document.thumbnail(id, 250, 250);
            String filename = "./examples/files/test-thumbnail-large.png";
            File file = new File(filename);

            try {
                FileUtils.copyInputStreamToFile(contents, file);
            } catch (IOException e) {
                System.out.println("failed :(");
                System.out.println("  Error Message: " + e.getMessage());
            }

            System.out.println("success :)");
            System.out.println("  File was downloaded to " + filename + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #14
     *
     * Create a session for the file we uploaded from Example #1 with default
     * options.
     */
    public static void example14() {
        System.out.println("Example #14 - Create a session for a file with"
                           + " default options.");
        System.out.println("  Creating... ");

        try {
            session = Session.create(document.get("id").toString());

            @SuppressWarnings("unchecked")
            Map<String, Object> urls = (Map<String, Object>) session
                                                             .get("urls");

            System.out.println("success :)");
            System.out.println("  Session id is " + session.get("id") + ".");
            System.out.println("  Session expires on "
                               + session.get("expires_at") + ".");
            System.out.println("  Session view URL is "
                               + urls.get("view") + ".");
            System.out.println("  Session assets URL is " + urls.get("assets")
                               + ".");
            System.out.println("  Session realtime URL is "
                               + urls.get("realtime") + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #15
     *
     * Create a session for the file we uploaded from Example #1 all of the
     * options.
     */
    public static void example15() {
        System.out.println("Example #15 - Create a session for a file with more"
                           + " of the options.");
        System.out.println("  Creating... ");

        Map<String, Object> params = new HashMap<String, Object>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10);
        params.put("expiresAt", cal.getTime());
        params.put("isDownloadable", true);
        params.put("isTextSelectable", false);

        try {
            session2 = Session.create(document.get("id").toString(), params);

            @SuppressWarnings("unchecked")
            Map<String, Object> urls = (Map<String, Object>) session2
                                                             .get("urls");

            System.out.println("success :)");
            System.out.println("  Session id is " + session2.get("id") + ".");
            System.out.println("  Session expires on "
                               + session2.get("expires_at") + ".");
            System.out.println("  Session view URL is "
                                   + urls.get("view") + ".");
            System.out.println("  Session assets URL is " + urls.get("assets")
                                   + ".");
            System.out.println("  Session realtime URL is "
                               + urls.get("realtime") + ".");
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #16
     *
     * Delete the two sessions.
     */
    public static void example16() {
        System.out.println("Example #16 - Delete the two sessions.");
        System.out.println("  Deleting session #1... ");

        try {
            Boolean deleted = Session.delete(session.get("id").toString());

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  Session #1 was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println("  Deleting session #2... ");

        try {
            Boolean deleted = Session.delete(session2.get("id").toString());

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  Session #2 was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #17
     *
     * Delete the file we uploaded from Example #1.
     */
    public static void example17() {
        System.out.println("Example #17 - Delete the first file we uploaded.");
        System.out.println("  Deleting... ");

        try {
            Boolean deleted = Document.delete(document.get("id").toString());

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  File was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (com.box.view.Exception e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }
}
