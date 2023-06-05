# The output will be a json file in the following format:
# [{"title": string, "contributor": string, "start": int, "end": int}, {}, {}, ...]
import json
import sys
import re
from collections import Counter

file_path = sys.argv[1]
page_num = sys.argv[2]
parsed_data = []

with open(file_path, mode='r', encoding='utf8') as input_file:
	title_found = False

	start = input_file.tell()
	line = input_file.readline()
	while line:
		if ("\"title\"" in line) and (not title_found):
			title = line.split(':')[1].strip().rstrip(',').strip('\"')
			title_found = True
		if ("\"#text\":" in line) and title_found:
			linked_titles = re.findall(r"(?<=\[\[).+?(?=\]\]|\|.*\]\]|#.*\]\])", line)
			link_dict = dict(Counter(linked_titles))
			parsed_data.append({"title": title, "links": sorted(link_dict, key=link_dict.get, reverse=True)[:5]})
			title_found = False

		line = input_file.readline()

print(json.dumps(parsed_data, indent=4))
with open(f'link_parsed_{page_num}.json', mode='w', encoding='utf8') as output_file:
   json.dump(parsed_data, output_file, indent=4)