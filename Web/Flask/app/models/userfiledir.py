import pymysql

db = pymysql.connect("192.168.1.160", "root", "Supinf0!", "Cloud")
#db = pymysql.connect("criszz.top", "root", "Supinf0!", "Cloud")
cursor = db.cursor()


class userfiledir:
    def __init__(self, ufdid):
        self.ufdid = ufdid
        sql = "SELECT * FROM `UserFileDir` WHERE ufdid=" + str(ufdid)
        try:
            cursor.execute(sql)
            results = cursor.fetchall()
            for row in results:
                self.username = row[1]
                self.type = row[2]
                self.dirName = row[3]
                self.fileid = row[4]
                self.fatherUfdid = row[5]
        except Exception as e:
            print(e)

    @staticmethod
    def newUserFileDir(username, dfType, dirName, fileid, fatherUfdid):
        sql = """INSERT INTO `UserFileDir` (`username`, `type`, `dirName`, `fileid`, `fatherUfdid`) VALUES (%s, %s, %s, %s, %s)"""
        try:
           # print(sql % (username, dfType, dirName, fileid, fatherUfdid))
            cursor.execute(sql, (username, dfType, dirName, fileid, fatherUfdid))
            db.commit()
        except Exception as e:
            print(e)

    @staticmethod
    def renameUserFileDir(ufdid, name):
        sql = "UPDATE `UserFileDir` SET dirName ='" + name + "' WHERE ufdid=" + str(ufdid)
        try:
            cursor.execute(sql)
            db.commit()
        except Exception as e:
            print(e)

    @staticmethod
    def findNextDir(fatherid):
        sql = "SELECT * FROM  UserFileDir WHERE fatherUfdid = %s"
        result = None
        try:
            cursor.execute(sql, fatherid)
            result = cursor.fetchall()
        except Exception as e:
            print(e)
        return result

    @staticmethod
    def findRootId(username):
        sql = "SELECT ufdid FROM  UserFileDir WHERE fatherUfdid is NULL AND username = '" + username + "'"
        try:
            cursor.execute(sql)
            result = cursor.fetchone()
            return str(result[0])
        except Exception as e:
            print(e)
            return "-1"

    @staticmethod
    def deleteDir(ufdid):
        sql = "DELETE FROM UserFileDir WHERE ufdid=" + str(ufdid)
        try:
            cursor.execute(sql)
            db.commit()
        except Exception as e:
            print(e)

    @staticmethod
    def changeFather(newFatherid, folderId):
        sql = "UPDATE UserFileDir SET fatherUfdid=" + str(newFatherid) + " WHERE ufdid=" + str(folderId)
      #  print(sql)
        try:
            cursor.execute(sql)
            db.commit()
        except Exception as e:
            print(e)

    @staticmethod
    def findLastDir(ufdid):
        sql = "SELECT fatherUfdid FROM UserFileDir WHERE ufdid=" + str(ufdid)
        try:
            cursor.execute(sql)
            result = cursor.fetchone()
          #  print(result)
            return str(result[0])
        except Exception as e:
            print(e)
            return "-1"

#get id as soon as insert
    @staticmethod
    def getId():
        sql = " SHOW TABLE STATUS where name ='UserFileDir'"
        try:
            cursor.execute(sql)
            result = cursor.fetchone()
            return int(result[10]) - 1
        except Exception as e:
            print(e)

# userfiledir.changeFather(11, 19)
# userfiledir.updateUserFileDir("dirName", "myPictures", 2)
#userfiledir.newUserFileDir(11, "f", None, "musicididididididi", 4)
