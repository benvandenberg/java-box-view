import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.box.view.BoxViewClient;
import com.box.view.BoxViewException;
import com.box.view.Document;
import com.box.view.Session;

public class Examples {
    public static String apiKey = "YOUR_API_KEY";

    public static BoxViewClient boxView;

    public static Document document;

    public static Document document2;

    public static Session session;

    public static Session session2;

    public static Date start = new Date();

    public static void main(String[] args) throws ParseException {
        boxView = new BoxViewClient(apiKey);

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
        String sampleUrl = "http://crocodoc.github.io/java-box-view/examples/"
                            + "files/sample.doc";
        System.out.print("  Uploading... ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Sample File");

        try {
            document = boxView.upload(sampleUrl, params);

            System.out.println("success :)");
            System.out.println("  ID is " + document.getId() + ".");
        } catch (BoxViewException e) {
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
        System.out.print("  Checking metadata... ");

        try {
            Document documentDuplicate = boxView.getDocument(document.getId());

            System.out.println("success :)");
            System.out.println("  File ID is " + documentDuplicate.getId()
                               + ".");
            System.out.println("  File status is " + documentDuplicate.getStatus()
                               + ".");
            System.out.println("  File name is " + documentDuplicate.getName()
                               + ".");
            System.out.println("  File was created on "
                               + documentDuplicate.getCreatedAt().toString()
                               + ".");
        } catch (BoxViewException e) {
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
        File file = new File("./files/sample.doc");
        System.out.print("  Uploading... ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Sample File #2");
        ArrayList<String> thumbnails = new ArrayList<String>();
        thumbnails.add("100x100");
        thumbnails.add("200x200");
        params.put("thumbnails", thumbnails);
        params.put("nonSvg", true);

        try {
            document2 = boxView.upload(file, params);

            System.out.println("success :)");
            System.out.println("  ID is " + document2.getId() + ".");
        } catch (BoxViewException e) {
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
        System.out.print("  Checking metadata... ");

        try {
            Document documentDuplicate = boxView.getDocument(document2.getId());

            System.out.println("success :)");
            System.out.println("  File ID is " + documentDuplicate.getId()
                               + ".");
            System.out.println("  File status is " + documentDuplicate.getStatus()
                               + ".");
            System.out.println("  File name is " + documentDuplicate.getName()
                               + ".");
            System.out.println("  File was created on "
                               + documentDuplicate.getCreatedAt().toString()
                               + ".");
        } catch (BoxViewException e) {
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
        System.out.println("Example #5 - List the documents we've uploaded so"
                           + " far.");
        System.out.print("  Listing documents... ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("createdAfter", start);

        try {
            List<Document> documents = boxView.findDocuments(params);

            Document doc1 = documents.get(0);
            Document doc2 = documents.get(1);

            System.out.println("success :)");
            System.out.println("  File #1 ID is " + doc1.getId() + ".");
            System.out.println("  File #1 status is " + doc1.getStatus() + ".");
            System.out.println("  File #1 name is " + doc1.getName() + ".");
            System.out.println("  File #1 was created on "
                               + doc1.getCreatedAt().toString() + ".");
            System.out.println("  File #2 ID is " + doc2.getId() + ".");
            System.out.println("  File #2 status is " + doc2.getStatus() + ".");
            System.out.println("  File #2 name is " + doc2.getName() + ".");
            System.out.println("  File #2 was created on "
                               + doc2.getCreatedAt().toString() + ".");
        } catch (BoxViewException e) {
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
        System.out.print("  Waiting... ");

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
        }

        System.out.println("done.");
        System.out.print("  Checking statuses... ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("createdAfter", start);

        try {
            List<Document> documents = boxView.findDocuments(params);

            Document doc1 = documents.get(0);
            Document doc2 = documents.get(1);

            System.out.println("success :)");
            System.out.println("  Status for file #1 (id=" + doc1.getId()
                               + ") is " + doc1.getStatus() + ".");
            System.out.println("  Status for file #2 (id=" + doc2.getId()
                               + ") is " + doc2.getStatus() + ".");
        } catch (BoxViewException e) {
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
        System.out.print("  Deleting... ");

        try {
            Boolean deleted = document2.delete();

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  File was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (BoxViewException e) {
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
        System.out.print("  Updating... ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Updated Name");

        try {
            Boolean updated = document.update(params);

            if (updated) {
                System.out.println("success :)");
                System.out.println("  File ID is " + document.getId() + ".");
                System.out.println("  File status is " + document.getStatus()
                                   + ".");
                System.out.println("  File name is " + document.getName()
                                   + ".");
                System.out.println("  File was created on "
                                   + document.getCreatedAt().toString() + ".");
            } else {
                System.out.println("failed :(");
            }
        } catch (BoxViewException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }

    /*
     * Example #9
     *
     * Download the file we uploaded from Example #1 in its original file
     * format.
     */
    public static void example9() {
        System.out.println("Example #9 - Download a file in its original file"
                           + " format.");
        System.out.print("  Downloading... ");

        try {
            InputStream contents = document.download();
            String filename = "./files/test-original.doc";
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
        } catch (BoxViewException e) {
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
        System.out.print("  Downloading... ");

        try {
            InputStream contents = document.download("pdf");
            String filename = "./files/test.pdf";
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
        } catch (BoxViewException e) {
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
        System.out.print("  Downloading... ");

        try {
            InputStream contents = document.download("zip");
            String filename = "./files/test.zip";
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
        } catch (BoxViewException e) {
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
        System.out.print("  Downloading... ");

        try {
            InputStream contents = document.thumbnail(16, 16);
            String filename = "./files/test-thumbnail.png";
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
        } catch (BoxViewException e) {
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
        System.out.print("  Downloading... ");

        try {
            InputStream contents = document.thumbnail(250, 250);
            String filename = "./files/test-thumbnail-large.png";
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
        } catch (BoxViewException e) {
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
        System.out.print("  Creating... ");

        try {
            session = document.createSession();

            System.out.println("success :)");
            System.out.println("  Session id is " + session.getId() + ".");
            System.out.println("  Session expires on "
                               + session.getExpiresAt().toString() + ".");
            System.out.println("  Session view URL is " + session.getViewUrl()
                               + ".");
            System.out.println("  Session assets URL is "
                               + session.getAssetsUrl() + ".");
            System.out.println("  Session realtime URL is "
                               + session.getRealtimeUrl() + ".");
        } catch (BoxViewException e) {
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
        System.out.print("  Creating... ");

        Map<String, Object> params = new HashMap<String, Object>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10);
        params.put("expiresAt", cal.getTime());
        params.put("isDownloadable", true);
        params.put("isTextSelectable", false);

        try {
            session2 = document.createSession(params);

            System.out.println("success :)");
            System.out.println("  Session id is " + session.getId() + ".");
            System.out.println("  Session expires on "
                               + session2.getExpiresAt().toString() + ".");
            System.out.println("  Session view URL is " + session.getViewUrl()
                               + ".");
            System.out.println("  Session assets URL is "
                               + session.getAssetsUrl() + ".");
            System.out.println("  Session realtime URL is "
                               + session.getRealtimeUrl() + ".");
        } catch (ParseException e) {
            System.out.println("failed :(");
            System.out.println("  Error Message: " + e.getMessage());
        } catch (BoxViewException e) {
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
        System.out.print("  Deleting session #1... ");

        try {
            Boolean deleted = session.delete();

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  Session #1 was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (BoxViewException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.print("  Deleting session #2... ");

        try {
            Boolean deleted = session2.delete();

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  Session #2 was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (BoxViewException e) {
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
        System.out.print("  Deleting... ");

        try {
            Boolean deleted = document.delete();

            if (deleted) {
                System.out.println("success :)");
                System.out.println("  File was deleted.");
            } else {
                System.out.println("failed :(");
            }
        } catch (BoxViewException e) {
            System.out.println("failed :(");
            System.out.println("  Error Code: " + e.getCode());
            System.out.println("  Error Message: " + e.getMessage());
        }

        System.out.println();
    }
}
