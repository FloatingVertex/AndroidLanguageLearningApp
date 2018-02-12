import pickle
from bs4 import BeautifulSoup as soup
import requests
import csv
import re

data = pickle.load(open("data_old.p",'rb'))

def customCVSWriter(file,list):
    for element in list[:-1]:
        file.write(('"'+element+'",').encode("utf-8"))
    file.write(('"'+list[-1]+'"\n').encode("utf-8"))

with open("autoGenCSV.csv",'wb') as myfile:
    #wr = csv.writer(myfile,quoting=csv.QUOTE_ALL,dialect='excel')
    customCVSWriter(myfile,["romaji","hiragana","definition","priority","type","japanese"])
    wordNum = 0
    fails = 0
    for set in data:
        wordNum = wordNum + 1
        if set == None:
            print("Word number " + str(wordNum) + "is missing")
            continue
        print("")
        print("-----------------------------------------------------------------------------------------------------------------------------------------------")
        japanese = set["word"]

        nogo = True
        try:
            relaventPart = re.search(r'<span class="mw-headline" id="Japanese">Japanese</span>(.*)',set["page"].content.decode("utf-8"),re.S).group(1)
            hiraganaObj = re.search(r'<i>hiragana</i>.*?<b class="Jpan" lang="ja".*?>.*?<a href=".*?">(.*?)</a></b>',relaventPart,re.S)
            hiragana = japanese
            if(hiraganaObj == None):
                print("is already in hiragana")
            else:
                print("isn't already in hiragana")
                hiragana = hiraganaObj.group(1)
            #print(re.search(r'(.*)<i>romaji</i>(.*)',relaventPart,re.S).group(2))
            print(str(wordNum) + japanese)
            #romanjiObj = re.search(r'<i>romaji</i>[^<]*<b class="Jpan" lang="ja"><a href="[^>]*" title="[^>]*">([^<]*)</a></b>', relaventPart, re.S)
            #if romanjiObj == None:
            romanjiObj = re.match(r'.*?class="mw-headline" id="(.*?)<i>rōmaji</i>.*?<b class=.*?" lang="ja".*?>.*?<a href=".*?">(.*?)</a>.*?</b>(.*)', relaventPart, re.S)
            #romanjiObj2 = re.match(r'.*class="mw-headline" id="(.*?)<i>rōmaji</i>.*?<b class=.*?" lang="ja".*?>.*?<a href=".*?">(.*?)</a>.*?</b>(.*)', relaventPart, re.S)

            print("Hiragana: " + hiragana)
            type = ""
            typeInfo = romanjiObj.group(1)
            try:
                type = re.match(r'.*<span class="mw-headline" id=".*?">(.*?)</span>.*?',typeInfo,re.S).group(1)
            except:
                type = "Unknown"
            print(type)
            #type = re.search(r'.*?>(.*?)<').group(0)
            romanji = romanjiObj.group(2)
            restOfString = romanjiObj.group(3)
            #print(restOfString)
            definitionObj = re.match(r'.*?<ol>.*?<li>(.*?)</li>',restOfString,re.S)
            if definitionObj == None:
                print("Defintion was not found")
                definition = ""
            else:
                definition = definitionObj.group(1)
                definition = re.sub(r'<.*?>',"",definition)
                definition = re.sub(r'"', "", definition)
                definition = definition.split('\n', 1)[0]
                definitionObj = re.search(r'.*?:(.*)', definition,re.S)
                if(definitionObj != None):
                    definition = definitionObj.group(1)
                definition = definition.strip()
                definition = definition.replace('"','')
                print(definition)

            print("Romaji: " + romanji)
            nogo = False

        except:
            fails = fails + 1
            print("**********************************************Error at word num*************************************************" + str(wordNum))
            #wr.writerow(["Error","","",str(wordNum),"",""])
            #exit(1)
        if nogo:
            customCVSWriter(myfile,["Error", "", "", str(wordNum), "", ""])
        else:
            customCVSWriter(myfile,[romanji, hiragana, str(definition), str(wordNum), type, japanese])
        #exit(0)

print("Failed: " + str(fails))
