import argparse, os, sys, pathlib, json

def parse_data(json_dir):
    with open(json_dir, mode='r', encoding='utf8') as input_file:
        data = json.load(input_file)
    return data

if __name__ == "__main__":
    assert sys.version_info >= (3, 7), "Script requires Python 3.7+."
    here = pathlib.Path(__file__).parent

    json_dir = sys.argv[1]
    address = sys.argv[2]
    file_num = sys.argv[3]
    url_prefix = f"http://{address}/article"

    articles = parse_data(json_dir)
    print(len(articles))

    with open(os.path.join(here, f"urls/url_{file_num}.txt"), mode='w') as writer:
        for article in articles:
            url = url_prefix + f"/{article['title']}\n"
            writer.write(url)
    print("write done!")