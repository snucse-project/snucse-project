from urllib.parse import urlparse
from threading import Thread
import http.client as httplib
import sys
from queue import Queue
import time

thread_count = 1000 #32000 
concurrent = 1000000

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

q = Queue(concurrent * 2)
start = time.time()
for i in range(thread_count):
    t = Thread(target=doWork)
    t.daemon = True
    t.start()
try:
    for url in open('test/url/url_49.txt'):
        q.put(url.strip())
    q.join()
except KeyboardInterrupt:
    sys.exit(1)

end = time.time()
print(f"{end - start:.5f} sec")