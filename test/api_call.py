import requests
import time
import argparse
from zipf.factories import ZipfFromText
import asyncio
import aiohttp
from aiohttp import ClientSession, ClientConnectorError
import time
import os

async def fetch_html(url: str, session: ClientSession, **kwargs) -> tuple:
    try:
        resp = await session.request(method="GET", url=url, **kwargs)
    except ClientConnectorError:
        return (url, 404)
    return (url, resp.status)


async def make_requests(urls: set, **kwargs) -> None:
    async with ClientSession() as session:
        tasks = []
        for url in urls:
            tasks.append(
                fetch_html(url=url, session=session, **kwargs)
            )
        results = await asyncio.gather(*tasks)

    # for result in results:
    #     print(f'{result[1]} - {str(result[0])}')


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
    import pathlib
    import sys

    assert sys.version_info >= (3, 7), "Script requires Python 3.7+."
    here = pathlib.Path(__file__).parent

    parser = argparse.ArgumentParser()
    parser.add_argument('-j', '--json_dir') # wikipedia articles (json)
    parser.add_argument('-t', '--type')
    parser.add_argument('-p', '--parallel')
    parser.add_argument('-a', '--address')
    args = parser.parse_args()

    json_dir = args.json_dir
    type = args.type
    address = args.address
    url_prefix = f"http://{address}:3000/article"
    parallel = int(args.parallel)

    articles = parse_data(json_dir)
    print(len(articles))

    for p in range(parallel):
        with open(os.path.join(here, f"url/url_{p}.txt"), mode='w') as writer:
            bundle = articles[p::][::parallel]
            for article in bundle:
                url = url_prefix + f"/{article['title']}\n"
                writer.write(url)

    urls = []
    
    for p in range(parallel):
        with open(os.path.join(here, f"url/url_{p}.txt"), mode='r') as reader:
            urls.append(set(map(str.strip, reader)))

    start = time.time()
    for p in range(parallel):
        asyncio.run(make_requests(urls=urls[p]))
    end = time.time()
    
    print(f"{end - start:.5f} sec")
