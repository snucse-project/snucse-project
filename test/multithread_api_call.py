from urllib.parse import urlparse
from threading import Thread
import http.client as httplib
import sys
from queue import Queue
import time, argparse, pathlib, os
import numpy as np

def doWork():
    while True:
        url = q.get()
        status, url = getStatus(url)
        doSomethingWithResult(status, url)
        q.task_done()

def getStatus(ourl):
    try:
        url = urlparse(ourl)
        conn = httplib.HTTPConnection(url.netloc)   
        conn.request("HEAD", url.path)
        res = conn.getresponse()
        conn.close()
        return res.status, ourl
    except:
        return "error", ourl

def doSomethingWithResult(status, url):
    # pass
    print(url)


def parse_url(url):
    url = url.strip()
    title_slash = url[33:].rfind('/')
    if title_slash != -1:
        url = url[:title_slash+33] + '%2F' + url[title_slash+34:]
    return url


if __name__ == "__main__":
    here = pathlib.Path(__file__).parent
    parser = argparse.ArgumentParser()
    parser.add_argument('-t', '--type', choices=["random", "zipf", "link"])
    parser.add_argument('-m', '--mode', choices=["demo", "test"])
    args = parser.parse_args()

    thread_count = 1000 #32000 
    concurrent = 1000000

    q = Queue(concurrent * 2)
    for i in range(thread_count):
        t = Thread(target=doWork)
        t.daemon = True
        t.start()

    try:
        if args.type == "random":
            import random
            # while True:
            urls = []
            for i in range(1, 11) if args.mode == "test" else range(1, 2):
                for url in open(os.path.join(here, f'urls/url_{i}.txt' if args.mode == "test" else f'urls/demo_url_{i}.txt')):
                    urls.append(parse_url(url))
        
            random_urls = []
            for _ in range(len(urls)):
                random_urls.append(random.choice(urls))
            start = time.time()
            # print(f"total urls: {len(random_urls)}")
            if args.mode == "test":
                for url in random_urls[:len(random_urls)//100]:
                    q.put(url)
            else:
                while True:
                    for url in random_urls:
                        q.put(url)
            q.join()
            end = time.time()
            # print(f"total urls: {len(random_urls[:len(random_urls)])}")
        elif args.type == "zipf":
            import random
            total_zipf_urls = []
            for i in range(1, 11) if args.mode == "test" else range(1, 2):
                zipf_urls = []
                for url in open(os.path.join(here, f'zipf_urls/url_{i}.txt' if args.mode == "test" else f'zipf_urls/demo_url_{i}.txt')):
                    zipf_urls.append(parse_url(url))
                random.shuffle(zipf_urls)
                total_zipf_urls.extend(zipf_urls[:len(zipf_urls)//100] if args.mode == "test" else zipf_urls[:len(zipf_urls)//10])
            start = time.time()
            print(f"total urls: {len(total_zipf_urls)}")
            for url in total_zipf_urls:
                q.put(url)
            q.join()
            end = time.time()
        elif args.type == "link":
            total_link_urls = []
            for i in range(1, 11) if args.mode == "test" else range(1, 2):
                link_urls = []
                for url in open(os.path.join(here, f'link_urls/url_{i}.txt')):
                    link_urls.append(parse_url(url))
                total_link_urls.append(link_urls[:5000] if args.mode == "test" else link_urls)
            start = time.time()
            # total_link_urls = np.array(total_link_urls)
            # print(total_link_urls)
            print(f"total urls: {len(np.array(total_link_urls).flatten())}")
            while len(np.array(total_link_urls).flatten()) != 0:
                # print(len(np.array(total_link_urls).flatten()))
                for i in range(10) if args.mode == "test" else range(0, 1):
                    if len(total_link_urls[i]) == 0:
                        continue
                    url = total_link_urls[i][0]
                    total_link_urls[i] = np.delete(total_link_urls[i], [0], axis=0)
                    q.put(url)
            end = time.time()
    except KeyboardInterrupt:
        sys.exit(1)

    print(f"{end - start:.5f} sec")