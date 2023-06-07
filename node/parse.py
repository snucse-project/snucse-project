# The output will be a json file in the following format:
# [{"title": string, "contributor": string, "start": int, "end": int}, {}, {}, ...]
import json
import os
import sys

file_path = sys.argv[1]
output_dir = os.path.dirname(file_path)+'/'
file_name = os.path.basename(file_path)
file_name = os.path.splitext(file_name)[0] # file name w/o extension

parsed_data = []

with open(file_path, mode='r', encoding='utf8') as input_file:
	title_found = False

	start = input_file.tell()
	line = input_file.readline()
	while line:
		if ("\"title\"" in line) and (not title_found):
			title = line.split(':')[1].strip().rstrip(',').strip('\"')
			title_found = True
		if ("\"username\"" in line or "\"ip\"" in line) and title_found:
			contributor = line.split(':')[1].strip().rstrip(',').strip('\"')
		if ("\"#text\":" in line) and title_found:
			start += 25
			end = input_file.tell()-1
			parsed_data.append({"title": title, "contributor": contributor, "start": start, "end": end})
			title_found = False

		start = input_file.tell()
		line = input_file.readline()

# print(json.dumps(parsed_data, indent=4))
with open(output_dir+file_name+'_parsed.json', mode='w', encoding='utf8') as output_file:
	json.dump(parsed_data, output_file, indent=4)
