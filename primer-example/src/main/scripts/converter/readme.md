svn:externals -> import.txt converter

Running:
python converter.py dir

What does it do?
 - Traverses recursively specified directory
 - If directory with svn:externals set is encountered:
   - Generates import.txt file and marks it to be added to repo
   - Removes svn:externals property
   - Recursively traverses paths referenced by svn:externals

Assumptions:
 - svn:externals shall always reference paths within .../src/main/resources or .../src/test/resources (e.g. https://svn.driver.research-infrastructures.eu/driver/dnet11/modules/icm-iis-citationmatching/trunk/src/main/resources/eu/dnetlib/iis/citationmatching/coansys )
 - Referenced path is also checked out, with appropriate relative location (i.e. it is stored in a location that can be infered using local directory path, its remote URL and svn:externals URL)
