import requests
import pickle
from bs4 import BeautifulSoup as soup
import time

def getRequest(url):
    searching = True
    time.sleep(2)
    while(searching):
        try:
            r = requests.get(url)
            if (len(r.content) > 1000):
               return r
            print("sleeping")
            time.sleep(300)
        except:
            print("failed connection attempt")
            #time.sleep(500)
            return None

data = []
mainPage = getRequest("https://en.wiktionary.org/wiki/Wiktionary:Frequency_lists/Japanese")
souped_main_page = soup(mainPage.content,"html.parser")
linkList = souped_main_page.find("ol").find_all("li")
linksM = [x.find("a") for x in linkList]
words = []
links = []
for x in linksM:
    if x == None:
        print(x)
        links.append(None)
    else:
        links.append(x.get("href"))
        words.append(x.text)
        print(words[-1])

print()
print()
index = 0
i = 0
for link in links:
    if(link == None):
        data.append(None)
        continue
    page = getRequest("https://en.wiktionary.org"+link)
    data.append({"link":link,"page":page,"word":words[i]})
    print(words[i])
    print(i)
    i = i + 1
    index = index+1
    if index == 500:
        print("Saving*******************************************************")
        index = 0
        with open("data.p", "wb") as fp:
            pickle.dump(data, fp)
    #print(link)

with open("data.p","wb") as fp:
    pickle.dump(data,fp)
print(links)
print(len(data))