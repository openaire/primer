#!/usr/bin/env python

__author__ = "Mateusz Fedoryszak"

import os
import re
import string
import argparse

from subprocess import check_output, check_call, CalledProcessError
from collections import deque

# SVN functions

def svn_get_externals(dir):
    out = check_output(['svn', 'propget', 'svn:externals', dir])
    return [tuple(line.strip().split(None, 1)) for line in out.split('\n') if line.strip()]

def svn_get_url(dir):
    return svn_get_info(dir)['URL']

def svn_get_info(dir):
    out = check_output(['svn', 'info', dir])
    return dict(tuple(map(string.strip, line.split(':', 1))) for line in out.split('\n') if ':' in line)

def svn_clean_externals(dir):
    check_call(['svn', 'propdel', 'svn:externals', dir])

def svn_add(file):
    try:
        check_call(['svn', 'add', file])
    except CalledProcessError as e:
        print(e) 


def externals2import(exts, filename):
    with open(filename, 'w') as f:
        for k, v in exts:
            f.write(k + ' cp ' + url2class(v) + '\n')

def url2class(url):
    pattern = r'.*/src/((main)|(test))/resources/(?P<class>.+)'
    regex = re.compile(pattern)
    m = regex.match(url)
    return m.groupdict()['class']
    
def url2path(url, my_path, my_url):
    if url == my_url: return my_path

    url = url + '/'
    my_url = my_url + '/'
    
    idx = 0
    minlen = min(len(url), len(my_url))
    while idx < minlen and url[idx] == my_url[idx]:
        idx += 1
    idx = url[0:idx].rfind('/')
    base = url[0:idx]

    reminder = url[idx:]
    my_reminder = my_url[idx:]
    elem_no = len(filter(None, my_reminder.split('/')))
    path = my_path
    for i in range(0, elem_no):
        (path, _) = os.path.split(path)
    path = reduce(lambda x, y: x + os.sep + y, filter(None, reminder.split('/')), path)

    return path

def process_dir(dir):
    exts = svn_get_externals(dir)
    if not exts: return []
    import_file = os.path.join(dir, 'import.txt')
    externals2import(exts, import_file)
    svn_add(import_file)

    svn_clean_externals(dir)
    return [(x[0], url2path(x[1], dir, svn_get_url(dir))) for x in exts]

def parse_args():
    parser = argparse.ArgumentParser(description='Convert svn:externals-style links into import.txt files.')
    parser.add_argument('dir', help='a directory where the processing should start')

    return parser.parse_args()

if __name__ == "__main__":
    args = parse_args()
    visited = set()
    to_visit = deque([args.dir])

    while to_visit:
        rootdir = to_visit.popleft()
        for root, subFolders, _ in os.walk(rootdir):
            if root in visited:
                del subFolders[:]
                continue

            visited.add(root)
            exts = process_dir(root)
            (ext_dirs, ext_paths) = ([], [])
            if exts: (ext_dirs, ext_paths) = zip(*exts)
            to_visit.extend(ext_paths)

            if '.svn' in subFolders:
                subFolders.remove('.svn')
            for dir in ext_dirs:
                subFolders.remove(dir)
