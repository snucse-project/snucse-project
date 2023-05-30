#!/usr/bin/env python3
"""
Converts an XML file with a single outer list element
and a repeated list member element to JSON on stdout.
Processes large XML files with minimal memory using the
streaming feature of https://github.com/martinblech/xmltodict
which is required ("pip install xmltodict").

Expected input structure (element names are just examples):
  <mylist attr="a">
    <myitem name="foo"></myitem>
    <myitem name="bar"></myitem>
    <myitem name="baz"></myitem>
  </mylist>

Output:
  {
    "mylist": {
      "attr": "a",
      "myitem": [
        {
          "name": "foo"
        },
        {
          "name": "bar"
        },
        {
          "name": "baz"
        }
      ]
    }
  }
"""
import json
import os
import sys
import xmltodict


def handle_xml():
    return True


def usage(args, err=None):
    """
    Emits a message and exits.
    """
    if err:
        print("{}: {}".format(args[0], err), file=sys.stderr)
    print("Usage: {} <xml-file-name>".format(args[0]), file=sys.stderr)
    sys.exit()


if __name__ == '__main__':
    if len(sys.argv) != 2:
        usage(sys.argv)
    xmlfile = sys.argv[1]
    json_path = '/home/reverg/Downloads'
    if not os.path.isfile(xmlfile):
        usage(sys.argv, 'Not found or not a file: {}'.format(xmlfile))
    with open(xmlfile, 'rb') as f:
        # Set item_depth to turn on the streaming feature
        # Do not prefix attribute keys with @
        jsonString = json.dumps(xmltodict.parse(f, item_depth=0, attr_prefix='', item_callback=handle_xml), indent=3, ensure_ascii=False)
    with open(os.path.join(json_path, f"{xmlfile}.json"), 'w',encoding='utf8') as f:
      f.write(jsonString)