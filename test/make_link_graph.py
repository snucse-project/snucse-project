import json
import os, pathlib, sys


def DFS(graph, start_node):
    ## deque 패키지 불러오기
    from collections import deque
    visited = []
    need_visited = deque()
    
    ##시작 노드 설정해주기
    need_visited.append(start_node)
    
    ## 방문이 필요한 리스트가 아직 존재한다면
    while need_visited:
        ## 시작 노드를 지정하고
        node = need_visited.pop()
 
        ##만약 방문한 리스트에 없다면
        if node not in visited:
 
            ## 방문 리스트에 노드를 추가
            visited.append(node)
            ## 인접 노드들을 방문 예정 리스트에 추가
            need_visited.extend(graph[node])
                
    return visited


if __name__ == "__main__":
    here = pathlib.Path(__file__).parent

    address = sys.argv[1]
    url_prefix = f"http://{address}/article"

    graph = dict()
    urls = []
    with open('data/link_parsed.json', 'r') as articles:
        articles = json.load(articles)

    titles = set()
    for a in articles:
        titles.add(a['title'])

    for a in articles:
        graph[a['title']] = [art for art in a['links'] if art in titles]

    called = set()
    with open(os.path.join(here, 'link_url.txt'), mode='w') as writer:
        for title in titles:
            title_queries = DFS(graph, title)
            for t in title_queries:
                if t not in called:
                    url = url_prefix + f"/{t}\n"
                    writer.write(url)
            called.update(title_queries)
