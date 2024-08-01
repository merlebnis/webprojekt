import shutil

with open("uebersetzungen.csv") as datei:
    pfad = "src/main/resources/messages_{sprache}.properties"
    messagesTemplate = "{variablenname}={uebersetzung}\n"

    propertys = datei.readline()
    propertys = propertys.strip().split(";")
    propertys = propertys[1:]
    for sprache in propertys:
        pfad.format(sprache=sprache)
        with open(pfad.format(sprache=sprache), "w") as neuedatei:
            neuedatei.write("")
        

    for i in range(len(propertys)):
        with open(pfad.format(sprache=propertys[i]) , "a") as messages:
            datei.seek(0)
            for zeile in datei:
                zeile = zeile.strip().split(";")

                if zeile[0].startswith("#") or len(zeile) is not len(propertys) + 1:
                    continue

                else:
                    messages.write(messagesTemplate.format(variablenname=zeile[0],uebersetzung=zeile[i+1]))

    shutil.copy(pfad.format(sprache=propertys[0]), "src/main/resources/messages.properties")