import sqlite3
import codecs

lines = []

jwordLabels = []
jwordsEntries = []
with codecs.open("autoGenCSV.csv","r","utf-8") as f:
    for line in f:
        lines.append(line)
        print(line)

    jwordLabels = lines[0].rstrip().split(',')
    jwordsEntries = lines[1:]

print(jwordLabels)

conn = sqlite3.connect("Test2.db")
c = conn.cursor()
c.execute("DROP TABLE IF EXISTS jwords")
c.execute("DROP TABLE IF EXISTS android_metadata")

c.execute('''CREATE TABLE android_metadata ("locale" TEXT DEFAULT 'en_US')''')
c.execute('''INSERT INTO android_metadata VALUES ('en_US')''')

vars = "(_id INTEGER PRIMARY KEY,"
names = "(_id,"
for item in jwordLabels[:-1]:
    vars = vars + item + " TEXT,"
    names = names + item + ","
vars = vars + jwordLabels[-1] + " TEXT"
names = names + item + ")"
vars = vars + ")"
print(vars)
c.execute("CREATE TABLE jwords " + vars)

print("***********")
print("***********")

id = 0
for entryO in jwordsEntries:

    entry = entryO.rstrip().split(',')
    print(entry)
    info = "(" + str(id) + ","
    id = id + 1
    for item in entry[:-1]:
        info = info +"'"+ item + "',"
    info = info + "'" + entry[-1]
    info = info + "')"
    print(info)
    c.execute("INSERT INTO jwords "+names+" VALUES " + info)

conn.commit()
#c.execute("")


