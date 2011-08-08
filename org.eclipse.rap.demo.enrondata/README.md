Complex data example for RAP
============================

This bundle contributes an example page to the RAP demo that demonstrates how to display complex
data in a RAP Tree widget.

The dataset
-----------

The Enron dataset [1] is a collection of corporate emails that is available in the public domain.
The dataset is contained in an archive file named enron_mail_20110402.tgz (423MB).  This file can
be downloaded from [1].  The extracted size is ~ 2.6GB.

Usage
-----

* To use this demo, you have to download this file and extract it to a local directory.  Make sure
  that the demo has write access to the dataset directory, because it needs to add index files.

* The default dataset location is /data/enron/maildir.  To change this location, set the system
  property org.eclipse.rap.demo.enronDatasetDirectory to point to the directory where you extracted
  the archive file.

* On the first run, the demo will create index files in this directory to be able to provide fast
  access to the data.  Depending on your file system speed, the indexing process will take some
  seconds up to a minute.

[1] http://www.cs.cmu.edu/~enron/
