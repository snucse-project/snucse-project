import json
import os
import sys
import xmltodict
import uuid


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
    file_name = os.path.basename(xmlfile)
    file_name = os.path.splitext(file_name)[0] # file name w/o extension
    output_dir = 'data/'
    articles = []
    site_id = uuid.uuid1().hex

    #siteinfo_required_keys = ["sitename", "dbname", "base", "generator", "case", "namespaces"]
    page_required_keys = ["title", "ns", "id", "redirect", "revision"]
    revision_required_keys = ["id", "parentid", "timestamp", "contributor", "comment", "model", "format", "text"]

    if not os.path.isfile(xmlfile):
        usage(sys.argv, 'Not found or not a file: {}'.format(xmlfile))
    with open(xmlfile, 'rb') as xmlf:
        data = xmltodict.parse(xmlf, item_depth=0, attr_prefix='', item_callback=handle_xml)
        data["mediawiki"]["siteinfo"]["siteinfo_id"] = site_id

        for page in data["mediawiki"]["page"]:
            for key in page.copy().keys():
                if key not in page_required_keys:
                    del page[key]
            if "revision" in page.keys():
                revision = page["revision"]
                for content_key in revision.copy().keys():
                    if content_key not in revision_required_keys:
                        del revision[content_key]
                    elif content_key == "text" and ("xml:space" in revision[content_key].keys()):
                        del revision[content_key]["xml:space"]
            page["siteinfo_id"] = site_id
            articles.append(page)

        del data["mediawiki"]["page"]
    # with open(output_dir + file_name +'_siteinfo.json', mode='w', encoding='utf8') as output_file:
    #     json.dump(data, output_file, indent=4)
    with open(output_dir + file_name + '_articles.json', mode='w', encoding='utf8') as output_file:
        json.dump(articles, output_file, indent=4)
