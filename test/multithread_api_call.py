from urllib.parse import urlparse
from threading import Thread
import http.client as httplib
import sys
from queue import Queue
import time, argparse

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
    print(status, url)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('-t', '--type', choices=["random", "zipf", "link"])
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
            for url in open('test/url.txt'):
                urls.append(url.strip())
            
            random_urls = []
            for _ in range(len(urls)):
                random_urls.append(random.choice(urls))
            start = time.time()
            for url in random_urls:
                q.put(url)
            q.join()
            end = time.time()
        elif args.type == "zipf":
            import random
            zipf_urls = []
            for url in open('test/zipf_url.txt'):
                zipf_urls.append(url.strip())
            random.shuffle(zipf_urls)
            start = time.time()
            for url in zipf_urls:
                q.put(url)
            q.join()
            end = time.time()
    except KeyboardInterrupt:
        sys.exit(1)

    print(f"{end - start:.5f} sec")