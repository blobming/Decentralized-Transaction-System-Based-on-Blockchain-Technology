import pymysql

db = pymysql.connect("192.168.1.160", "root", "Supinf0!", "Cloud")
#db = pymysql.connect("criszz.top", "root", "Supinf0!", "Cloud")
cursor = db.cursor()

class File:
    def __init__(self, fileid):
        self.md5 = fileid
        sql = "SELECT * FROM File WHERE md5 = '" + str(fileid) + "'"
        print(sql)
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                self.filename = row[1]
                self.type = row[2]
                self.size = row[3]
        except Exception as e:
            print(e)

    @staticmethod
    def newFile(md5, filename, filetype, size):
        sql = """INSERT INTO `File` (`md5`, `filename`, `type`, `size`) VALUES (%s, %s, %s, %s)""" 
        try:
            cursor.execute(sql, (md5, filename, filetype, size))
            db.commit()
        except Exception as e:
            print(e)

    @staticmethod
    def checkFileExist(md5):
        sql = "SELECT md5 FROM File "
        cursor.execute(sql)
        results = cursor.fetchall()
        for row in results:
            if row[0] == md5:
                return True
        return False

    @staticmethod
    def getFilename(md5):
        sql = "SELECT filename FROM File WHERE md5 = '" + md5 + "'" 
        try:
            cursor.execute(sql)
            result = cursor.fetchone()
            return str(result[0])
        except Exception as e:
            print(e)
            return "-1"

'''

pair = {"A": "1",
        "B": "2",
        "C": "3"}
file.newFile(pair)
'''
