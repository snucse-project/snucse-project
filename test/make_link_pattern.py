import json
import os, pathlib, sys


def DFS(graph, start_node):
    from collections import deque
    visited = []
    need_visited = deque()
    
    need_visited.append(start_node)
    while need_visited:
        node = need_visited.pop()
        if node not in visited:
            visited.append(node)
            need_visited.extend(graph[node])
                
    return visited


if __name__ == "__main__":
    here = pathlib.Path(__file__).parent

    address = sys.argv[1]
    url_prefix = f"http://{address}/article"

    for file_num in range(1, 11):
        graph = dict()
        urls = []
        with open(f'data/parsed_link/enwiki-latest-pages-articles_{file_num}_parsed_link.json', 'r') as articles:
            articles = json.load(articles)

        titles = set()
        for a in articles:
            titles.add(a['title'])

        for a in articles:
            graph[a['title']] = [art for art in a['links'] if art in titles]

        called = set()
        with open(os.path.join(here, f'link_urls/url_{file_num}.txt'), mode='w', encoding='utf8') as writer:
            for title in titles:
                title_queries = DFS(graph, title)
                for t in title_queries:
                    if t not in called:
                        url = url_prefix + f"/{t}\n"
                        writer.write(url)
                called.update(title_queries)
