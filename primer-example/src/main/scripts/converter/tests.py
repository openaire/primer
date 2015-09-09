import unittest
from converter import *
from os import path

class Tests(unittest.TestCase):
    def test_url2class(self):
        expected = 'eu/dnetlib/iis/citationmatching/main_workflow/oozie_app'
        actual = url2class(r'https://svn.driver.research-infrastructures.eu/driver/dnet11/modules/icm-iis-citationmatching/trunk/src/test/resources/eu/dnetlib/iis/citationmatching/main_workflow/oozie_app')
        self.assertEqual(actual, expected)
    
    def test_url2path(self):
        base = 'https://svn.driver.research-infrastructures.eu/driver/dnet11/modules/icm-iis-citationmatching/trunk/src/test/resources/'
        self.assertEqual(url2path(base + 'eu', 'eu', base + 'eu'), 'eu')
        self.assertEqual(url2path(base + 'eu/dnet', 'eu', base + 'eu'), path.join('eu', 'dnet'))
        self.assertEqual(url2path(base + 'eu/dnet', path.join('eu', 'dnetlib'), base + 'eu/dnetlib'), path.join('eu', 'dnet'))
        self.assertEqual(url2path(base + 'eu/dnet', path.join('eu', 'dnetlib'), base + 'eu/dnetlib'), path.join('eu', 'dnet'))
        self.assertEqual(url2path(base + 'eu/dnetlib/core', path.join(path.join('eu', 'dnetlib'), 'citations'), base + 'eu/dnetlib/citations'), path.join(path.join('eu', 'dnetlib'), 'core'))
        self.assertEqual(url2path(base + 'eu/dnet/core', path.join(path.join('eu', 'dnetlib'), 'citations'), base + 'eu/dnetlib/citations'), path.join(path.join('eu', 'dnet'), 'core'))

if __name__ == '__main__':
    unittest.main()