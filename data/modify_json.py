import json
import os
import sys
import uuid

file_path = sys.argv[1]
file_name = os.path.basename(file_path)
file_name = os.path.splitext(file_name)[0] # file name w/o extension
articles = []
site_id = uuid.uuid1().hex

#siteinfo_required_keys = ["sitename", "dbname", "base", "generator", "case", "namespaces"]
page_required_keys = ["title", "ns", "id", "redirect", "revision"]
revision_required_keys = ["id", "parentid", "timestamp", "contributor", "comment", "model", "format", "text"]


with open(file_path, mode='r', encoding='utf8') as input_file:
    data = json.load(input_file)
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


with open(file_name+'_siteinfo.json', mode='w', encoding='utf8') as output_file:
    json.dump(data, output_file, indent=4)
with open(file_name+'_articles.json', mode='w', encoding='utf8') as output_file:
    json.dump(articles, output_file, indent=4)
