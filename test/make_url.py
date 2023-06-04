import argparse, os, sys, pathlib

def parse_data(json_dir):
    parsed_data = []
    with open(json_dir, mode='r', encoding='utf8') as input_file:
        title_found = False
        line = input_file.readline()
        while line:
            if ("\"title\"" in line) and (not title_found):
                title = line.split(':')[1].strip().rstrip(',').strip('\"')
                title_found = True
            if ("\"username\"" in line or "\"ip\"" in line) and title_found:
                contributor = line.split(':')[1].strip().rstrip(',').strip('\"')
            if ("\"#text\":" in line) and title_found:
                text = line.split(':')[1].strip().rstrip(',').strip('\"')
                parsed_data.append({"title": title, "contributor": contributor, "text": text})
                title_found = False
            line = input_file.readline()
    return parsed_data


if __name__ == "__main__":
    assert sys.version_info >= (3, 7), "Script requires Python 3.7+."
    here = pathlib.Path(__file__).parent

    json_dir = sys.argv[1]
    address = sys.argv[2]
    url_prefix = f"http://{address}/article"

    articles = parse_data(json_dir)
    print(len(articles))

    with open(os.path.join(here, f"url.txt"), mode='w') as writer:
        for article in articles:
            url = url_prefix + f"/{article['title']}\n"
            writer.write(url)
    print("write done!")